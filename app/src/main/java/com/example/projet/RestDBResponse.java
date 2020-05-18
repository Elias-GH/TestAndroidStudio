package com.example.projet;

import java.util.List;

public class RestDBResponse {

    private Integer count;
    private String next;
    private List<PersonnageDB> results;

    public Integer getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public List<PersonnageDB> getResults() {
        return results;
    }
}
