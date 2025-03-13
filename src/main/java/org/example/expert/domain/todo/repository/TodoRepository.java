package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import org.antlr.v4.runtime.atn.SemanticContext.OR;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    //Lv1-3
    @Query("""
            SELECT t FROM Todo t
            LEFT JOIN FETCH t.user u
            WHERE (:weather IS NULL OR t.weather = :weather)
            AND (:startDay IS NULL OR t.modifiedAt >= :startDay)
            AND (:endDay IS NULL OR t.modifiedAt <= :endDay)
            ORDER BY t.modifiedAt DESC""")
    Page<Todo> findAllByCondition(Pageable pageable, String weather, LocalDateTime startDay, LocalDateTime endDay);

}
