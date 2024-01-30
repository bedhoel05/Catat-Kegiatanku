package com.nathan.uasiot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private adapter adapter;
    private ArrayList<dataKegiatan> kegiatanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToAdd = findViewById(R.id.btn_addKegiatan);

        btnToAdd.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, addKegiatan.class);
            startActivity(intent);
        });

        db.collection("kegiatan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            kegiatanArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Ambil data dari setiap dokumen Firestore dan tambahkan ke ArrayList
                                dataKegiatan wisata = new dataKegiatan(
                                        document.getString("nama"),
                                        document.getString("tempat"),
                                        document.getString("deskripsi"),
                                        document.getString("tanggal"),
                                        document.getString("waktu")
                                );
                                kegiatanArrayList.add(wisata); // Tambahkan ke ArrayList
                            }
                            // Setelah selesai mengambil data, set adapter untuk RecyclerView
                            adapter = new adapter(kegiatanArrayList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity.this, "Eror geting dokument", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerKegiatan);
        adapter = new adapter(kegiatanArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}