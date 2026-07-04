package com.example.umnu_hub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPasswordReg;
    private EditText etConfirmPassword;
    private Button btnSubmitRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPasswordReg = findViewById(R.id.etPasswordReg);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);

        btnSubmitRegister.setOnClickListener(v -> {

            if (!validationForm()) {
                return;
            }

            registerUser();
        });
    }

    private void registerUser() {

        String url =
                "http://192.168.0.110/umnu_hub_api/register.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,

                response -> {

                    Log.d("REGISTER_RESPONSE", response);

                    String result = response.trim();

                    if (result.equals("success")) {

                        Toast.makeText(
                                Register.this,
                                "Registrasi Berhasil",
                                Toast.LENGTH_SHORT
                        ).show();

                        startActivity(
                                new Intent(
                                        Register.this,
                                        MainActivity.class
                                )
                        );

                        finish();

                    } else if (result.equals("email_exist")) {

                        Toast.makeText(
                                Register.this,
                                "Email sudah digunakan",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                Register.this,
                                "Response : " + result,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                },

                error -> {

                    Log.e("VOLLEY_ERROR", error.toString());

                    if (error.networkResponse != null) {

                        Log.e(
                                "VOLLEY",
                                "HTTP Code : " + error.networkResponse.statusCode
                        );

                        Toast.makeText(
                                Register.this,
                                "HTTP " + error.networkResponse.statusCode,
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                Register.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    error.printStackTrace();

                }

        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put(
                        "email",
                        etEmail.getText().toString().trim()
                );

                params.put(
                        "password",
                        etPasswordReg.getText().toString().trim()
                );

                return params;
            }
        };

        RequestQueue queue =
                Volley.newRequestQueue(Register.this);

        queue.add(request);
    }

    private boolean validationForm() {

        String email =
                etEmail.getText().toString().trim();

        String password =
                etPasswordReg.getText().toString().trim();

        String confirmPassword =
                etConfirmPassword.getText().toString().trim();

        if (email.isEmpty()) {

            etEmail.setError("Email harus diisi");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {

            etPasswordReg.setError("Password harus diisi");
            etPasswordReg.requestFocus();
            return false;
        }

        if (password.length() < 8) {

            etPasswordReg.setError("Password minimal 8 karakter");
            etPasswordReg.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {

            etConfirmPassword.setError("Konfirmasi password harus diisi");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {

            etConfirmPassword.setError("Password tidak sama");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}