package com.example.umnu_hub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class KomentarActivity extends AppCompatActivity {

    RecyclerView rvKomentar;
    EditText etKomentar;
    Button btnKirim;

    ArrayList<Komentar> listKomentar;
    KomentarAdapter adapter;

    RequestQueue queue;

    String idPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);

        rvKomentar = findViewById(R.id.rvKomentar);
        etKomentar = findViewById(R.id.etKomentar);
        btnKirim = findViewById(R.id.btnKirim);

        queue = Volley.newRequestQueue(this);

        idPost = getIntent().getStringExtra("id_post");

        listKomentar = new ArrayList<>();

        adapter = new KomentarAdapter(this, listKomentar);

        rvKomentar.setLayoutManager(new LinearLayoutManager(this));
        rvKomentar.setAdapter(adapter);

        loadKomentar();

        btnKirim.setOnClickListener(v -> kirimKomentar());
    }

    private void loadKomentar() {

        String url =
                "http://192.168.0.110/umnu_hub_api/get_komentar.php?id_post=" + idPost;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,

                response -> {

                    try {

                        JSONArray array = new JSONArray(response);

                        listKomentar.clear();

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);

                            listKomentar.add(
                                    new Komentar(
                                            obj.getString("nama"),
                                            obj.getString("komentar")
                                    )
                            );

                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        Toast.makeText(
                                this,
                                e.toString(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                },

                error -> Toast.makeText(
                        this,
                        error.toString(),
                        Toast.LENGTH_LONG
                ).show()

        );

        request.setShouldCache(false);
        queue.getCache().clear();
        queue.add(request);

    }

    private void kirimKomentar() {

        String isiKomentar =
                etKomentar.getText().toString().trim();

        if (isiKomentar.isEmpty()) {

            etKomentar.setError("Komentar tidak boleh kosong");
            return;
        }

        SharedPreferences prefs =
                getSharedPreferences("UserData", MODE_PRIVATE);

        String userId =
                prefs.getString("USER_ID", "0");

        String url =
                "http://192.168.0.110/umnu_hub_api/tambah_komentar.php";

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        url,

                        response -> {

                            if (response.trim().equals("success")) {

                                etKomentar.setText("");

                                loadKomentar();

                            } else {

                                Toast.makeText(
                                        this,
                                        response,
                                        Toast.LENGTH_LONG
                                ).show();

                            }

                        },

                        error -> Toast.makeText(
                                this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ) {

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params =
                                new HashMap<>();

                        params.put("id_post", idPost);
                        params.put("id_user", userId);
                        params.put("komentar", isiKomentar);

                        return params;
                    }
                };

        queue.add(request);

    }
}