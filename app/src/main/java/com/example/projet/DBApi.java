package com.example.projet;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DBApi {

    @GET("dbapi.json")
    Call<RestDBResponse> getDBResponse();


}
