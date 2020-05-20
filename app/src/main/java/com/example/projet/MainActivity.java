package com.example.projet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.projet.R.id.recycler_view;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=  getSharedPreferences("application_esiea", Context.MODE_PRIVATE);
         gson = new GsonBuilder().setLenient().create();

         List<PersonnageDB> DBList = getDataFromCache();

         if(DBList != null) {
             showList(DBList);
         }
         else {
             makeApiCall();
         }

}

    private List<PersonnageDB> getDataFromCache() {
        String JsonPersonnageDB = sharedPreferences.getString("jsonPokemonList", null);
        if(JsonPersonnageDB == null)
        {
            return null;
        } else {
            Type listType = new TypeToken<List<PersonnageDB>>() {
            }.getType();
            return gson.fromJson(JsonPersonnageDB, listType);
        }
    }

        private void showList(List<PersonnageDB> DBList) {

            setContentView(R.layout.activity_main);
            // recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView = (RecyclerView) findViewById(recycler_view);
            // use this setting to
            // improve performance if you know that changes
            // in content do not change the layout size
            // of the
            recyclerView.setHasFixedSize(true);
            // use a lineaRecyclerViewr layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // define an adapter
            mAdapter = new ListAdapter(DBList);
            recyclerView.setAdapter(mAdapter);
        }

    private static final String BASE_URL = "https://pokeapi.co/";

    private void makeApiCall() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        DBApi animeAPI = retrofit.create(DBApi.class);

        Call<RestDBResponse> call = animeAPI.getDBResponse();
        call.enqueue(new Callback<RestDBResponse>() {
            @Override
            public void onResponse(Call<RestDBResponse> call, Response<RestDBResponse> response) {
                if (response.isSuccessful() && response.body().getResults() != null ) {
                    List<PersonnageDB> personnageDBList = response.body().getResults();
                   Toast.makeText(getApplicationContext(), "API Success", Toast.LENGTH_SHORT).show();
                   saveList(personnageDBList);
                   showList(personnageDBList);
                } else {
                    showError();
                }
            }

            private void saveList(List<PersonnageDB> personnageDBList) {

                String jsonString = gson.toJson(personnageDBList);
                sharedPreferences
                        .edit()
                        .putString("jsonPokemonList", jsonString)
                        .apply();

                Toast.makeText(getApplicationContext(), "List saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestDBResponse> call, Throwable t) {

                showError();
            }
        });
    }
        private void showError () {
            Toast.makeText(getApplicationContext(), "API ERROR", Toast.LENGTH_SHORT).show();

        }


    }
