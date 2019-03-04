package com.demotxt.myapp.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demotxt.myapp.myapplication.R;
import com.demotxt.myapp.myapplication.adapters.RecyclerViewAdapter;
import com.demotxt.myapp.myapplication.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String JSON_URL = "http://omdbapi.com/?s=can&y=2018&type=movie&apikey=9f4f767e";
    private JsonObjectRequest request ;
    private RequestQueue requestQueue ;
    private List<Movie> lstMovie;
    private RecyclerView recyclerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstMovie = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();
        Log.e("test",String.valueOf(lstMovie.size()));

    }


    private void jsonrequest(){
        request = new JsonObjectRequest
                (JSON_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject;
                            jsonArray = response.getJSONArray("Search");
                            for (int i = 0; i < jsonArray.length(); i++) {


                                jsonObject = jsonArray.getJSONObject(i) ;
                                Movie movie = new Movie() ;
                                movie.setName(jsonObject.getString("Title"));
                                movie.setCategorie(jsonObject.getString("Type"));
                                movie.setRating(jsonObject.getString("Year"));
                                movie.setStudio(jsonObject.getString("imdbID"));
                                movie.setImage_url(jsonObject.getString("Poster"));
                                reqDescription(jsonObject.getString("imdbID"),i);
                                Log.e("",jsonObject.getString("Title"));
                                lstMovie.add(movie);
                            }
                        }catch (Exception ignored){}
                        requestDecription();
                        setuprecyclerview(lstMovie);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);


    }

    private void requestDecription(){
        String URL1 = "http://www.omdbapi.com/?i=";
        String URL2 = "&plot=full&apikey=9f4f767e";
        String id,url ;

        for (int i = 0; i< lstMovie.size(); i++){
            final int num = i;

            id = lstMovie.get(i).getStudio();
            url =URL1 + id + URL2;
            request = new JsonObjectRequest
                    (url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Movie movie = new Movie();
                                movie.setDescription(response.getString("Plot"));
                                lstMovie.add(movie);
                            }catch (Exception ignored){}

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

        }


    }

    private void reqDescription(String id,final int i){
        String URL1 = "http://www.omdbapi.com/?i=";
        String URL2 = "&plot=full&apikey=9f4f767e";
        String url = URL1 + id + URL2;
//        String Description;

        request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Movie movie = new Movie();
                    movie.setDescription(response.getString("Plot"));
                    lstMovie.add(i,movie);
                }catch (Exception ignored){}

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setuprecyclerview(List<Movie> lstMovie) {


        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this, lstMovie) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }
}
