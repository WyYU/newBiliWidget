package com.example.dell.biliwidget;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szysky.customize.siv.SImageView;

import java.net.URL;
import java.util.List;

/**
 * Created by dell on 2018/7/30 0030.
 */

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {
    List<Bangumi.Result.Seasons> seasonsList;
    OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView index;
        SImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            index = (TextView) itemView.findViewById(R.id.index);
            cover = (SImageView) itemView.findViewById(R.id.cover);

        }
    }

    public SeasonAdapter(List<Bangumi.Result.Seasons> list) {
        this.seasonsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangumi, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Bangumi.Result.Seasons seasons = seasonsList.get(position);
        holder.title.setText(seasons.getTitle());
        holder.time.setText(seasons.getPub_time());
        holder.index.setText(seasons.getPub_index());
        holder.cover.setImageUrls(seasons.getCover());

        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return seasonsList.size();
    }

}
