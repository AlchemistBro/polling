package com.example.polling.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class VoteRequest {

    @NotNull(message = "pollId обязателен")
    private Integer pollId;

    @NotBlank(message = "username обязателен")
    private String username;

    @NotBlank(message = "optionTitle обязателен")
    private String optionTitle;


    public VoteRequest(Integer pollId, String username, String optionTitle) {
        this.pollId = pollId;
        this.username = username;
        this.optionTitle = optionTitle;
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

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }
}
