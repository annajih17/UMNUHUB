package com.example.umnu_hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostinganAdapter extends RecyclerView.Adapter<PostinganAdapter.ViewHolder> {

    Context context;
    ArrayList<Postingan> list;

    OnLikeClickListener likeListener;
    OnKomentarClickListener komentarListener;

    public interface OnLikeClickListener{
        void onLikeClick(Postingan postingan);
    }

    // BARU
    public interface OnKomentarClickListener{
        void onKomentarClick(Postingan postingan);
    }

    public PostinganAdapter(
            Context context,
            ArrayList<Postingan> list,
            OnLikeClickListener likeListener,
            OnKomentarClickListener komentarListener
    ){
        this.context = context;
        this.list = list;
        this.likeListener = likeListener;
        this.komentarListener = komentarListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Postingan postingan = list.get(position);

        holder.tvUsername.setText("Anonim");
        holder.tvPesan.setText(postingan.getText());

        if(postingan.getMedia().isEmpty()){

            holder.imgPost.setVisibility(View.GONE);

        }else{

            holder.imgPost.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load("http://192.168.0.110/umnu_hub_api/upload/" + postingan.getMedia())
                    .into(holder.imgPost);
        }

        holder.tvLike.setText("❤ " + postingan.getLike());
        holder.tvKomentar.setText("💬 " + postingan.getKomentar());

        holder.tvLike.setOnClickListener(v -> {
            likeListener.onLikeClick(postingan);
        });

        // BARU
        holder.tvKomentar.setOnClickListener(v -> {
            komentarListener.onKomentarClick(postingan);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername;
        TextView tvPesan;
        TextView tvLike;
        TextView tvKomentar;
        ImageView imgPost;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvUsername=itemView.findViewById(R.id.tvUsername);
            tvPesan=itemView.findViewById(R.id.tvPesan);
            tvLike=itemView.findViewById(R.id.tvLike);
            tvKomentar=itemView.findViewById(R.id.tvKomentar);
            imgPost=itemView.findViewById(R.id.imgPost);
        }
    }
}