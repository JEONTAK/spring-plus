package org.example.expert.domain.todo.dto.response

import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.dto.response.UserResponse
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val contents: String,
    val weather: String,
    val user: UserResponse,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
){
    companion object{
        fun of(todo: Todo, userResponse: UserResponse): TodoResponse {
            return TodoResponse(
                todo.id,
                todo.title,
                todo.contents,
                todo.weather,
                userResponse,
                todo.createdAt,
                todo.modifiedAt
            )
        }
    }
}
