package com.example.polling.dto;

public class VotedPollDto {
    private long id;
    private String title;
    private String selectedOption;


    public VotedPollDto(long id, String title, String selectedOption) {
        this.id = id;
        this.title = title;
        this.selectedOption = selectedOption;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}
