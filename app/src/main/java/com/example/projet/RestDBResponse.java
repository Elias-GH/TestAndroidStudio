package com.example.projet;

import java.util.List;

public class RestDBResponse {

    private List<PersonnageDB> name;

    public RestDBResponse(List<PersonnageDB> name) {
        this.name = name;
    }


    public List<PersonnageDB> getResults() {

        return name;
    }
}
