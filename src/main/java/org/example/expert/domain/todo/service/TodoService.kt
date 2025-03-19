package org.example.expert.domain.todo.service

import lombok.RequiredArgsConstructor
import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class TodoService(
    val todoRepository: TodoRepository,
    val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: AuthUser, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = User.fromAuthUser(authUser)

        val weather = weatherClient.todayWeather

        val newTodo = Todo(
            title = todoSaveRequest.title,
            contents = todoSaveRequest.contents,
            weather = weather,
            user = user
        )

        val savedTodo = todoRepository.save(newTodo)

        return TodoSaveResponse.of(
            savedTodo,
            UserResponse(user.id, user.email)
        )
    }


    fun getTodos(page: Int, size: Int, weather: String?, start: String?, end: String?): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDay = if (start != null) LocalDate.parse(start, formatter).atStartOfDay() else null
        val endDay = if (end != null) LocalDate.parse(end, formatter).atTime(23, 59, 59) else null
        val todos = todoRepository.findAllByCondition(pageable, weather, startDay, endDay)

        return todos.map {
            TodoResponse.of(
                it,
                UserResponse(it.user.id, it.user.email)
            )
        }
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo: Todo = todoRepository.findByIdWithUser(todoId) ?: throw InvalidRequestException("Todo not found")

        val user: User = todo.user

        return TodoResponse.of(
            todo,
            UserResponse(user.id, user.email)
        )
    }
}
