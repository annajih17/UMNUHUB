package com.example.umnu_hub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.InputStream;
import java.io.ByteArrayOutputStream;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    RecyclerView rvPostingan;
    EditText etMessage;
    Button btnSend;
    ImageButton btnImage;
    ImageView IVprofil;
    RecyclerView rvTrending;

    ArrayList<Postingan> trendingList;

    TrendingAdapter trendingAdapter;

    Uri imageUri;

    ArrayList<Postingan> listPostingan;
    PostinganAdapter adapter;
    RequestQueue queue;

    private static final int PICK_IMAGE = 1;

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {

            loadPostingan();
            loadTrending();

            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // =========================
        // Inisialisasi View
        // =========================
        rvPostingan = findViewById(R.id.rvPostingan);
        rvTrending = findViewById(R.id.rvTrending);

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnImage = findViewById(R.id.btnImage);
        IVprofil = findViewById(R.id.IVprofil);

        // =========================
        // Volley
        // =========================
        queue = Volley.newRequestQueue(this);

        // =========================
        // RecyclerView Trending
        // =========================
        trendingList = new ArrayList<>();

        trendingAdapter = new TrendingAdapter(
                Home.this,
                trendingList,
                postingan -> {
                    // Nanti bisa buka komentar/detail posting
                }
        );

        rvTrending.setLayoutManager(
                new LinearLayoutManager(
                        Home.this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        rvTrending.setAdapter(trendingAdapter);

        // =========================
        // RecyclerView Postingan
        // =========================
        listPostingan = new ArrayList<>();

        adapter = new PostinganAdapter(
                Home.this,
                listPostingan,
                postingan -> likePostingan(postingan.getId()),
                postingan -> bukaKomentar(postingan.getId())
        );

        rvPostingan.setLayoutManager(
                new LinearLayoutManager(Home.this)
        );

        rvPostingan.setAdapter(adapter);

        // =========================
        // Load Data
        // =========================
        loadTrending();
        loadPostingan();

        // Auto Refresh
        handler.postDelayed(refreshRunnable, 3000);

        // =========================
        // Event Button
        // =========================
        btnImage.setOnClickListener(v -> pilihGambar());

        btnSend.setOnClickListener(v -> kirimPesan());

        IVprofil.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Profil.class));
        });
    }
    private void loadPostingan() {

        String url =
                "http://192.168.0.110/umnu_hub_api/get_postingan.php";

        StringRequest request =
                new StringRequest(
                        Request.Method.GET,
                        url,

                        response -> {

                            try {

                                JSONArray jsonArray =
                                        new JSONArray(response);

                                boolean diBawah =
                                        !rvPostingan.canScrollVertically(1);

                                listPostingan.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj =
                                            jsonArray.getJSONObject(i);

                                    listPostingan.add(
                                            new Postingan(
                                                    obj.getString("id"),
                                                    obj.getString("text"),
                                                    obj.optString("media",""),
                                                    obj.optInt("total_like",0),
                                                    obj.optInt("total_komentar",0)
                                            )
                                    );
                                }

                                adapter.notifyDataSetChanged();

                                if(diBawah && !listPostingan.isEmpty()){

                                    rvPostingan.scrollToPosition(
                                            listPostingan.size()-1
                                    );

                                }

                            } catch (Exception e) {

                                e.printStackTrace();

                                Toast.makeText(
                                        Home.this,
                                        e.toString(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        },

                        error -> Toast.makeText(
                                Home.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                );

        request.setShouldCache(false);

        queue.getCache().clear();

        queue.add(request);
    }
    private void pilihGambar() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(
                Intent.createChooser(intent, "Pilih Gambar"),
                PICK_IMAGE
        );
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if(requestCode == PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null){

            imageUri = data.getData();

            Toast.makeText(
                    this,
                    "Gambar berhasil dipilih",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void kirimPesan(){

        String pesan=etMessage.getText().toString().trim();

        SharedPreferences prefs=
                getSharedPreferences("UserData",MODE_PRIVATE);

        String userId=
                prefs.getString("USER_ID","0");

        String url="http://192.168.0.110/umnu_hub_api/post_message.php";

        VolleyMultipartRequest request=
                new VolleyMultipartRequest(
                        Request.Method.POST,
                        url,

                        response->{

                            String hasil=new String(response.data);

                            Toast.makeText(
                                    Home.this,
                                    hasil,
                                    Toast.LENGTH_SHORT
                            ).show();

                            etMessage.setText("");
                            imageUri=null;

                            loadPostingan();
                            loadTrending();

                        },

                        error-> Toast.makeText(
                                Home.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ){

                    @Override
                    protected Map<String,String> getParams(){

                        Map<String,String> params=
                                new HashMap<>();

                        params.put("id_user",userId);
                        params.put("text",pesan);

                        return params;
                    }

                    @Override
                    protected Map<String,DataPart> getByteData(){

                        Map<String,DataPart> params=
                                new HashMap<>();

                        if(imageUri!=null){

                            params.put(
                                    "image",
                                    new DataPart(
                                            System.currentTimeMillis()+".jpg",
                                            getFileDataFromUri(imageUri)
                                    )
                            );

                        }

                        return params;
                    }

                };

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

                        response -> {
                            loadPostingan();
                            loadTrending();
                        },

                        error -> Toast.makeText(
                                Home.this,
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

    private void loadTrending() {

        String url = "http://192.168.0.110/umnu_hub_api/get_trending.php";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,

                response -> {

                    try {

                        JSONArray jsonArray = new JSONArray(response);

                        trendingList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            trendingList.add(
                                    new Postingan(
                                            obj.getString("id"),
                                            obj.getString("text"),
                                            obj.optString("media",""),
                                            obj.optInt("total_like", 0),
                                            obj.optInt("total_komentar", 0)
                                    )
                            );
                        }

                        trendingAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(
                                Home.this,
                                e.toString(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                },

                error -> Toast.makeText(
                        Home.this,
                        error.toString(),
                        Toast.LENGTH_SHORT
                ).show()

        );

        request.setShouldCache(false);
        queue.getCache().clear();
        queue.add(request);
    }
    private void bukaKomentar(String idPost){

        Intent intent = new Intent(Home.this, KomentarActivity.class);
        intent.putExtra("id_post", idPost);
        startActivity(intent);

    }

    private byte[] getFileDataFromUri(Uri uri){

        try{

            InputStream inputStream=
                    getContentResolver().openInputStream(uri);

            ByteArrayOutputStream bos=
                    new ByteArrayOutputStream();

            byte[] buf=new byte[1024];

            int n;

            while((n=inputStream.read(buf))!=-1){

                bos.write(buf,0,n);

            }

            return bos.toByteArray();

        }catch(Exception e){

            e.printStackTrace();

            return null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable);
    }
}