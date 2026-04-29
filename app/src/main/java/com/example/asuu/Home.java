package com.example.asuu;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ImageButton buttonMenu;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi Views
        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.etMessage);
        buttonSend = findViewById(R.id.btnSend);
        buttonMenu = findViewById(R.id.btnMenu);

        // Set listener untuk tombol Kirim
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToDatabase();
            }
        });

        // Set listener untuk tombol Menu (optional)
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan aksi untuk menu, misalnya menampilkan dialog
                Toast.makeText(Home.this, "Menu clicked", Toast.LENGTH_SHORT).show();
                // Atau buka activity lain
                // Intent intent = new Intent(Home.this, MenuActivity.class);
                // startActivity(intent);
            }
        });

        // Load dan tampilkan messages
        loadAndDisplayMessages();
    }

    // Method untuk mengirim pesan ke database
    private void sendMessageToDatabase() {
        String message = editTextMessage.getText().toString().trim();

        // Validasi: pastikan pesan tidak kosong
        if (message.isEmpty()) {
            Toast.makeText(this, "Pesan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan ke database
        boolean isInserted = databaseHelper.insertMessage(message);

        if (isInserted) {
            Toast.makeText(this, "Pesan berhasil dikirim!", Toast.LENGTH_SHORT).show();
            editTextMessage.setText(""); // Kosongkan EditText

            // Refresh tampilan untuk menampilkan pesan baru
            loadAndDisplayMessages();

            // Scroll ke item terakhir
            listViewMessages.smoothScrollToPosition(adapter.getCount() - 1);
        } else {
            Toast.makeText(this, "Gagal mengirim pesan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAndDisplayMessages() {
        // Ambil semua messages dari database
        ArrayList<String> messages = databaseHelper.getAllMessages();

        // Buat adapter untuk ListView
        if (adapter == null) {
            adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    messages
            );
            listViewMessages.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(messages);
            adapter.notifyDataSetChanged();
        }

        // Tampilkan pesan jika tidak ada data
        if (messages.isEmpty()) {
            Toast.makeText(this, "Belum ada pesan. Silakan tulis pesan!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data setiap kali activity diresume
        loadAndDisplayMessages();
    }
}