package com.example.umnu_hub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostinganSaya extends AppCompatActivity {

    RecyclerView rvPostingan;
    ImageView btnBack;

    ArrayList<Postingan> listPostingan;

    PostinganAdapter adapter;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postingan_saya);

        rvPostingan=findViewById(R.id.rvPostingan);
        btnBack=findViewById(R.id.btnBack);

        queue= Volley.newRequestQueue(this);

        listPostingan=new ArrayList<>();

        adapter=new PostinganAdapter(

                PostinganSaya.this,
                listPostingan,

                postingan -> likePostingan(postingan.getId()),

                postingan -> {
                    // nanti bisa buka halaman komentar
                }

        );

        rvPostingan.setLayoutManager(
                new LinearLayoutManager(this)
        );

        rvPostingan.setAdapter(adapter);

        loadPostingan();

        btnBack.setOnClickListener(v->finish());

    }

    private void loadPostingan(){

        SharedPreferences prefs=
                getSharedPreferences("UserData",MODE_PRIVATE);

        String userId=
                prefs.getString("USER_ID","0");

        String url=
                "http://192.168.0.110/umnu_hub_api/get_postingan_user.php";

        StringRequest request=
                new StringRequest(                        Request.Method.POST,
                        url,

                        response -> {

                            try{

                                JSONArray jsonArray =
                                        new JSONArray(response);

                                listPostingan.clear();

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject obj =
                                            jsonArray.getJSONObject(i);

                                    listPostingan.add(

                                            new Postingan(

                                                    obj.getString("id"),
                                                    obj.getString("text"),
                                                    obj.optString("image",""),
                                                    obj.optInt("total_like",0),
                                                    obj.optInt("total_komentar",0)

                                            )

                                    );

                                }

                                adapter.notifyDataSetChanged();

                            }catch(Exception e){

                                e.printStackTrace();

                                Toast.makeText(
                                        PostinganSaya.this,
                                        e.toString(),
                                        Toast.LENGTH_LONG
                                ).show();

                            }

                        },

                        error -> Toast.makeText(
                                PostinganSaya.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ){

                    @Override
                    protected Map<String,String> getParams(){

                        SharedPreferences prefs =
                                getSharedPreferences("UserData",MODE_PRIVATE);

                        String userId =
                                prefs.getString("USER_ID","0");

                        Map<String,String> params =
                                new HashMap<>();

                        params.put("id_user",userId);

                        return params;

                    }

                };

        request.setShouldCache(false);
        queue.getCache().clear();
        queue.add(request);

    }

    private void likePostingan(String idPost){

        SharedPreferences prefs =
                getSharedPreferences("UserData",MODE_PRIVATE);

        String userId =
                prefs.getString("USER_ID","0");

        String url =
                "http://192.168.0.110/umnu_hub_api/like.php";

        StringRequest request =
                new StringRequest(

                        Request.Method.POST,
                        url,

                        response -> loadPostingan(),

                        error -> Toast.makeText(
                                PostinganSaya.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ){

                    @Override
                    protected Map<String,String> getParams(){

                        Map<String,String> params =
                                new HashMap<>();

                        params.put("id_post",idPost);
                        params.put("id_user",userId);

                        return params;

                    }

                };

        queue.add(request);

    }

}