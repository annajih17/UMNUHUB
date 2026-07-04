package com.example.umnu_hub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvToRegister;
    EditText etNIMLogin, etPasswordLogin;
    RadioGroup rgRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        tvToRegister = findViewById(R.id.tvToRegister);
        etNIMLogin = findViewById(R.id.etNIMLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        rgRole = findViewById(R.id.rgRole);

        btnLogin.setOnClickListener(v -> loginUser());

        tvToRegister.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,
                        Register.class
                ))
        );
    }

    private void loginUser() {

        String email = etNIMLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        if(email.isEmpty()){
            etNIMLogin.setError("Email harus diisi");
            return;
        }

        if(password.isEmpty()){
            etPasswordLogin.setError("Password harus diisi");
            return;
        }

        String role = "user";

        int selectedRole = rgRole.getCheckedRadioButtonId();

        if(selectedRole == R.id.rbDosen){
            role = "dosen";
        }
        else if(selectedRole == R.id.rbAdmin){
            role = "admin";
        }

        String url = "http://192.168.0.110/umnu_hub_api/login.php";

        String finalRole = role;

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,

                response -> {

                    try {

                        JSONObject obj =
                                new JSONObject(response);

                        if(obj.getString("status")
                                .equals("success")) {

                            SharedPreferences prefs =
                                    getSharedPreferences(
                                            "UserData",
                                            MODE_PRIVATE
                                    );

                            SharedPreferences.Editor editor =
                                    prefs.edit();

                            editor.putString(
                                    "USER_ID",
                                    obj.getString("id")
                            );

                            editor.putString(
                                    "USER_EMAIL",
                                    obj.getString("email")
                            );

                            editor.putString(
                                    "ROLE",
                                    obj.getString("role")
                            );

                            if(obj.has("username")){
                                editor.putString(
                                        "USERNAME",
                                        obj.getString("username")
                                );
                            }

                            editor.apply();

                            Toast.makeText(
                                    MainActivity.this,
                                    "Login Berhasil",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            MainActivity.this,
                                            Home.class
                                    )
                            );

                            finish();

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Email atau Password Salah",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } catch (Exception e) {

                        Toast.makeText(
                                MainActivity.this,
                                "Error JSON : " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }
                },

                error -> {

                    Log.e("LOGIN_ERROR", error.toString());

                    if (error.getMessage() != null) {
                        Log.e("LOGIN_ERROR", error.getMessage());
                    }

                    Toast.makeText(
                            MainActivity.this,
                            error.toString(),
                            Toast.LENGTH_LONG
                    ).show();

                }

        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params =
                        new HashMap<>();

                params.put("email", email);
                params.put("password", password);
                params.put("role", finalRole);

                return params;
            }
        };

        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }
}