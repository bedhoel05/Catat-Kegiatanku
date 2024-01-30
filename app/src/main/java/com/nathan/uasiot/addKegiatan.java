package com.nathan.uasiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class addKegiatan extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText datePicker, Edit_Time ;
    Button btnAddKegiatan;
    EditText nama, tempat, tanggal, waktu, deskripsi;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kegiatan);


        btnAddKegiatan = findViewById(R.id.btnSaveAddKegiatan);
        datePicker=findViewById(R.id.addTanggalKegiatan);
        Edit_Time =findViewById(R.id.addWaktuKegiatan);
        nama = findViewById(R.id.addNamaKegiatan);
        tempat = findViewById(R.id.addTempatKegiatan);
        tanggal = findViewById(R.id.addTanggalKegiatan);
        waktu = findViewById(R.id.addWaktuKegiatan);
        deskripsi = findViewById(R.id.addDeskripsiKegiatan);
        db = FirebaseFirestore.getInstance();


        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addKegiatan.this, date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();;
            }
        });

        Edit_Time.setOnClickListener(v->{
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(addKegiatan.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    Edit_Time.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        btnAddKegiatan.setOnClickListener(v->{
            String namaKegiatan = nama.getText().toString();
            String tempatKegiatan = tempat.getText().toString();
            String tanggalKegiatan = tanggal.getText().toString();
            String waktuKegiatan = waktu.getText().toString();
            String deksripsiKegiatan = deskripsi.getText().toString();
            String id = UUID.randomUUID().toString();

            saveToFirestore(id, namaKegiatan, tempatKegiatan, tanggalKegiatan, waktuKegiatan, deksripsiKegiatan);
            Intent saveAgdenda = new Intent(addKegiatan.this, MainActivity.class);
            startActivity(saveAgdenda);
        });

    }

    private void saveToFirestore(String id, String namaKegiatan, String tempatKegiatan, String tanggalKegiatan, String waktuKegiatan, String deksripsiKegiatan) {
        if (!namaKegiatan.isEmpty()){
            HashMap<String , Object> map = new HashMap<>();
            map.put("id", id);
            map.put("nama", namaKegiatan);
            map.put("tempat", tempatKegiatan);
            map.put("tanggal", tanggalKegiatan);
            map.put("waktu", waktuKegiatan);
            map.put("deskripsi", deksripsiKegiatan);

            db.collection("kegiatan").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(addKegiatan.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(addKegiatan.this, "Data Failed", Toast.LENGTH_SHORT).show();
                    String errorMessage = "Terjadi kesalahan: " + e.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("MyApp", "Error:", e);
                }
            });

        }else {
            Toast.makeText(this, "Nilai tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLabel() {
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        datePicker.setText(dateFormat.format(myCalendar.getTime()));
    }
}