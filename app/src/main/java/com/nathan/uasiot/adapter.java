package com.nathan.uasiot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.core.KeyFieldFilter;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.kegiatanViewHolder> {
    private final ArrayList<dataKegiatan> dataList;
    public adapter(ArrayList<dataKegiatan> dataList){
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public adapter.kegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_kegiatan, viewGroup, false);
        return new kegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.kegiatanViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.tempat.setText(dataList.get(position).getTempat());
        holder.deskripsi.setText(dataList.get(position).getDeskripsi());
        holder.tanggal.setText(dataList.get(position).getTanggal());
        holder.waktu.setText(dataList.get(position).getWaktu());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size():0;
    }

    public static class kegiatanViewHolder extends RecyclerView.ViewHolder{
        private final TextView nama, tempat, deskripsi, tanggal, waktu;

        public kegiatanViewHolder (@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.list_namaKegiatan);
            tempat = itemView.findViewById(R.id.list_lokasiKegiatan);
            deskripsi = itemView.findViewById(R.id.list_KeteranganKegiatan);
            tanggal = itemView.findViewById(R.id.list_TanggalKegiatan);
            waktu = itemView.findViewById(R.id.list_JamKegiatan);

        }
    }
}
