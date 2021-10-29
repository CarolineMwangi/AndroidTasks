package com.hfad.myvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL_DATA = "https://api.github.com/search/users?q=language:java+location:nairobi";
    //RecyclerView
    private RecyclerView recyclerView;
    //Adapter
    private DevelopersAdapter developersAdapter;
    //list
    private List<DeveloperList> developerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize developerList which willhold the contents of our remote json and pass it to the recyclerview
        developerList = new ArrayList<>();
        loadUrlData();

    }

    private void loadUrlData() {
      final ProgressDialog progressDialog = new ProgressDialog(this);
      progressDialog.setMessage("Loading...");
      progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("res", "Response:" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        DeveloperList developers = new DeveloperList(jo.getString("login"), jo.getString("avatar_url"), jo.getString("html_url"));
                        developerList.add(developers);
                        Log.d("res", "developers" + developers);
                    }
                    developersAdapter = new DevelopersAdapter(developerList, getApplicationContext());
                    recyclerView.setAdapter(developersAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Tag", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
    });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}