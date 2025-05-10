package com.example.polling.model;

import java.util.ArrayList;
import java.util.List;

public class PollField {
    private String title;
    private List<String> votesListDb = new ArrayList<>();


    public PollField(String title, List<String> votesListDb) {
        this.title = title;
        this.votesListDb = votesListDb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getVotesListDb() {
        return votesListDb;
    }

    public void setVotesListDb(List<String> votesListDb) {
        this.votesListDb = votesListDb;
    }
}
