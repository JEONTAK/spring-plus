package org.example.expert.domain.todo.dto.request

import jakarta.validation.constraints.NotBlank


class TodoSaveRequest (
    val title: @NotBlank String,
    val contents: @NotBlank String
)
