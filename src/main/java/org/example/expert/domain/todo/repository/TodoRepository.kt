package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface TodoRepository : JpaRepository<Todo?, Long?>, TodoRepositoryCustom {
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    fun findAllByOrderByModifiedAtDesc(pageable: Pageable?): Page<Todo?>?

    @Query(
        """
            SELECT t FROM Todo t
            LEFT JOIN FETCH t.user u
            WHERE (:weather IS NULL OR t.weather = :weather)
            AND (:startDay IS NULL OR t.modifiedAt >= :startDay)
            AND (:endDay IS NULL OR t.modifiedAt <= :endDay)
            ORDER BY t.modifiedAt DESC
            """
    )
    fun findAllByCondition(
        pageable: Pageable?,
        weather: String?,
        startDay: LocalDateTime?,
        endDay: LocalDateTime?
    ): Page<Todo>
}
