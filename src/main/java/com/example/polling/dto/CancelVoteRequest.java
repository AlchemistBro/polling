package com.example.polling.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class CancelVoteRequest {

    @NotNull(message = "pollId обязателен")
    private Integer pollId;

    @NotBlank(message = "username обязателен")
    private String username;

    public CancelVoteRequest(Integer pollId, String username) {
        this.pollId = pollId;
        this.username = username;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
