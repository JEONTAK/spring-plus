package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Todo

interface TodoRepositoryCustom {
    fun findByIdWithUser(todoId: Long): Todo?
}
