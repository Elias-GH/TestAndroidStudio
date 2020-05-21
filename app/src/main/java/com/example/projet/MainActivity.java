package com.example.projet;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.view.View;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import java.lang.reflect.Type;
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

         List<Pokemon> PokemonList = getDataFromCache();

         if(PokemonList != null) {
             showList(PokemonList);
         }
         else {
             makeApiCall();
         }



}

    public void showNotification(View view){


        NotificationManager notif = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(getApplicationContext()).setContentTitle("Projet ESIEA").setContentText("La recherche de Pokemon n'a pas encore été implémenté").setSmallIcon(R.drawable.ic_launcher_foreground).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0,notify);
        
    }

    private List<Pokemon> getDataFromCache() {
        String JsonPokemon = sharedPreferences.getString("jsonPokemonList", null);
        if(JsonPokemon == null)
        {
            return null;
        } else {
            Type listType = new TypeToken<List<Pokemon>>() {
            }.getType();
            return gson.fromJson(JsonPokemon, listType);
        }
    }

        private void showList(List<Pokemon> PokemonList) {

            setContentView(R.layout.activity_main);
            recyclerView = (RecyclerView) findViewById(recycler_view);

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new ListAdapter(PokemonList);
            recyclerView.setAdapter(mAdapter);
        }

    private static final String BASE_URL = "https://pokeapi.co/";

    private void makeApiCall() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PokemonAPI pokeAPI = retrofit.create(PokemonAPI.class);

        Call<RestPokemonResponse> call = pokeAPI.getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {
            @Override
            public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {
                if (response.isSuccessful() && response.body().getResults() != null ) {
                    List<Pokemon> pokemonList = response.body().getResults();
                   Toast.makeText(getApplicationContext(), "API Success", Toast.LENGTH_SHORT).show();
                   saveList(pokemonList);
                   showList(pokemonList);
                } else {
                    showError();
                }
            }

            private void saveList(List<Pokemon> pokemonList) {

                String jsonString = gson.toJson(pokemonList);
                sharedPreferences
                        .edit()
                        .putString("jsonPokemonList", jsonString)
                        .apply();

                Toast.makeText(getApplicationContext(), "List saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestPokemonResponse> call, Throwable t) {

                showError();
            }
        });
    }
        private void showError () {
            Toast.makeText(getApplicationContext(), "API ERROR", Toast.LENGTH_SHORT).show();

        }


}
