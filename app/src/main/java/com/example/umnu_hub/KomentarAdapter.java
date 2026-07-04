package com.example.umnu_hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {

    Context context;
    ArrayList<Komentar> listKomentar;

    public KomentarAdapter(Context context, ArrayList<Komentar> listKomentar) {
        this.context = context;
        this.listKomentar = listKomentar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_komentar, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Komentar komentar = listKomentar.get(position);

        holder.tvNama.setText(komentar.getNama());
        holder.tvKomentar.setText(komentar.getKomentar());

    }

    @Override
    public int getItemCount() {
        return listKomentar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvKomentar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvKomentar = itemView.findViewById(R.id.tvKomentar);

        }
    }
}