package com.example.polling.model;

import java.util.List;

public class Poll {
    private int id;
    private String author;
    private String title;
    private String description;
    private List<PollField> fields;

    public Poll(int id, String author, String title, String description, List<PollField> fields) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.fields = fields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PollField> getFields() {
        return fields;
    }

    public void setFields(List<PollField> fields) {
        this.fields = fields;
    }
}
