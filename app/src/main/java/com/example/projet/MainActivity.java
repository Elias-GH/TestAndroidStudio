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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showList();
        makeApiCall();
    }

   /*private List<PersonnageDB> getDataFromCache() {

       String JsonPersonnageDB = sharedPreferences.getString("cle_string", null);
       if(JsonPersonnageDB == null)
       {
           return null;
       } else {
           Type listType = new TypeToken<List<PersonnageDB>>() {
           }.getType();
           return gson.fromJson(JsonPersonnageDB, listType);
       }
    } */

    private void showList() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> input = new ArrayList<>();
        for(int i =0; i < 100; i++) {

            input.add("Test"+i);
        }

        mAdapter = new ListAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    private static final String BASE_URL = "https://pokeapi.co/";

    private void makeApiCall() {

      Gson gson = new GsonBuilder()
                .setLenient()
                .create();

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
                   //saveList(personnageDBList);
               //     showList(personnageDBList);
                } else {
                    showError();
                }
            }
/*
            private void saveList(List<PersonnageDB> personnageDBList) {

                String jsonString = gson.toJson(personnageDBList);
                sharedPreferences
                        .edit()
                        .putString("xcle_string", "monString")
                        .apply();

                Toast.makeText(getApplicationContext(), "List saved", Toast.LENGTH_SHORT).show();
            }
*/
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
