package com.example.umnu_hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Postingan> list;
    private OnTrendingClick listener;

    public interface OnTrendingClick {
        void onClick(Postingan postingan);
    }

    public TrendingAdapter(Context context,
                           ArrayList<Postingan> list,
                           OnTrendingClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_trending, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Postingan postingan = list.get(position);

        holder.tvRank.setText("🔥 #" + (position + 1));
        holder.tvText.setText(postingan.getText());
        holder.tvInfo.setText(
                "❤ " + postingan.getLike() +
                        "   💬 " + postingan.getKomentar()
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(postingan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRank, tvText, tvInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRank = itemView.findViewById(R.id.tvRank);
            tvText = itemView.findViewById(R.id.tvText);
            tvInfo = itemView.findViewById(R.id.tvInfo);
        }
    }
}