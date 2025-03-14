package org.example.expert.domain.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.log.enums.SavedStatus;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestUrl;

    private Long todoId;

    private Long requestUserId;

    private Long savedManagerId;

    @Enumerated(EnumType.STRING)
    private SavedStatus status;

    private String message;


    public Log(String requestUrl, Long todoId, Long requestUserId, Long savedManagerId, SavedStatus status, String message) {
        this.requestUrl = requestUrl;
        this.todoId = todoId;
        this.requestUserId = requestUserId;
        this.savedManagerId = savedManagerId;
        this.status = status;
        this.message = message;
    }

    public void update(SavedStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
