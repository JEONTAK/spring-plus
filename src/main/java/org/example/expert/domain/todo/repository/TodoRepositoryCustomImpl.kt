package org.example.expert.domain.todo.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.example.expert.domain.todo.entity.QTodo
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.QUser
import java.util.*


class TodoRepositoryCustomImpl(em: EntityManager?) : TodoRepositoryCustom {
    private val queryFactory = JPAQueryFactory(em)

    override fun findByIdWithUser(todoId: Long): Todo? {
        val todo = QTodo.todo
        val user = QUser.user

        val result = queryFactory
            .selectFrom(todo)
            .leftJoin(todo.user, user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchOne()

        return result
    }
}
