package com.example.projet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PokemonAPI {

    @GET("/api/v2/pokemon")
    Call<RestPokemonResponse> getPokemonResponse();

}
