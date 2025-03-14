package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

//Lv2-8
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    //Lv3-10
    @Override
    public Page<TodoSearchResponse> findAllUsingQueryDSL(Pageable pageable, String title, LocalDateTime createdAt,
                                           String nickname) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QManager manager = QManager.manager;
        
        //조건에 따른 쿼리 내용 추가 위한 builder 선언
        BooleanBuilder builder = new BooleanBuilder();
        
        //제목이 존재한다면 builder에 추가
        if (title != null && !title.isBlank()) {
            builder.and(todo.title.containsIgnoreCase(title));
        }

        //작성일이 존재한다면 builder에 추가
        if (createdAt != null) {
            builder.and(todo.createdAt.goe(createdAt));
            builder.and(todo.createdAt.loe(createdAt.plusHours(24)));
        }

        //담당자 별명이 존재한다면 builder에 추가
        if (nickname != null && !nickname.isBlank()) {
            builder.and(user.nickname.containsIgnoreCase(nickname));
        }

        /**
         * query 생성
         * 
         * Projections를 통해 필요한 값인 id, title, manager수, 댓글 수만 가져올 수 있도록 구현
         * manager의 수를 알아야 하기 때문에 manager join
         * manager의 nickname을 알아야 하기 때문에 user join
         * 댓글 수를 알아야 하기 때문에 comment join
         * builder를 통해 조건 삽입
         * 할 일 당 매니저 수와 댓글 수를 count 해야하기 때문에, groupBy를 통해 할 일 별로 묶음
         * 생성일 기준 내림차순이어야 하기 때문에, orderBy 삽입
         * offset과 페이지 크기 설정
         */
        JPAQuery<TodoSearchResponse> query = queryFactory
                .select(Projections.constructor(TodoSearchResponse.class, todo.id, todo.title, manager.countDistinct(),
                        comment.count()))
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(user).on(manager.user.eq(user))
                .leftJoin(todo.comments, comment)
                .where(builder)
                .groupBy(todo.id, todo.title)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        /**
         * countQuery 생성
         *
         * Page 객체를 만드려면, 총 데이터 개수를 알아야하기 때문에, countQuery 작성
         * 따라서 builder 조건에 맞는 할일을 countDistinct로 개수를 가져와
         * 조건에 맞는 총 할 일의 개수를 가져옴.
         */
        JPAQuery<Long> countQuery = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(user).on(manager.user.eq(user))
                .where(builder);
        
        //실질적인 데이터를 fetch를 통해 가져옴
        List<TodoSearchResponse> results = query.fetch();
        long total = countQuery.fetchOne();
        return new PageImpl<>(results, pageable, total);
    }
}
