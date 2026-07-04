package com.example.umnu_hub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Profil extends AppCompatActivity {

    LinearLayout menuPostingan;
    LinearLayout menuTentang;
    LinearLayout menuKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        menuPostingan = findViewById(R.id.menuPostingan);
        menuTentang = findViewById(R.id.menuTentang);
        menuKeluar = findViewById(R.id.menuKeluar);

        // Buka halaman Postingan Saya
        menuPostingan.setOnClickListener(v -> {
            startActivity(new Intent(Profil.this, PostinganSaya.class));
        });

        // Buka halaman Tentang
        menuTentang.setOnClickListener(v -> {
            startActivity(new Intent(Profil.this, Tentang.class));
        });

        // Logout
        menuKeluar.setOnClickListener(v -> {

            SharedPreferences preferences =
                    getSharedPreferences("UserData", MODE_PRIVATE);

            preferences.edit().clear().apply();

            Intent intent =
                    new Intent(Profil.this, MainActivity.class);

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        });
    }
}