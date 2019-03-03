package com.demotxt.myapp.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

    private final String JSON_URL = "https://gist.githubusercontent.com/aws1994/f583d54e5af8e56173492d3f60dd5ebf/raw/c7796ba51d5a0d37fc756cf0fd14e54434c547bc/anime.json" ;
    private final String JSON_URL2 = "http://omdbapi.com/?s=can&y=2018&type=movie&apikey=9f4f767e";
    private JsonArrayRequest request ;
    private JsonObjectRequest nrequest ;
    private RequestQueue requestQueue ;
    private List<Movie> lstMovie;
    private RecyclerView recyclerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstMovie = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);
        njsonrequest();



    }


    private void njsonrequest(){
        nrequest = new JsonObjectRequest
                (JSON_URL2, null, new Response.Listener<JSONObject>() {

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
                                movie.setDescription(reqDescription(jsonObject.getString("imdbID")));
                                Log.e("",jsonObject.getString("Title"));
                                lstMovie.add(movie);
                            }
                        }catch (Exception e){}
                        requestDecription();
                        setuprecyclerview(lstMovie);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(nrequest);


    }

    private void requestDecription(){
        String URL1 = "http://www.omdbapi.com/?i=";
        String URL2 = "&plot=full&apikey=9f4f767e";
        String id,url ;
        JsonObjectRequest request;

        for (int i = 0; i< lstMovie.size(); i++){
            final int num = i;

            id = lstMovie.get(i).getStudio();
            url =URL1 + id + URL2;
            nrequest = new JsonObjectRequest
                    (url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                lstMovie.get(num).setDescription(response.getString("Plot"));
                            }catch (Exception ignored){}

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

        }


    }

    private String reqDescription(String id){
        String URL1 = "http://www.omdbapi.com/?i=";
        String URL2 = "&plot=full&apikey=9f4f767e";
        String url = URL1 + id + URL2;
//        String Description;

        nrequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String Description = response.getString("Plot") ;
                }catch (Exception e){}

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return null;
    }

    private void setuprecyclerview(List<Movie> lstMovie) {


        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this, lstMovie) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }
}
