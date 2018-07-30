package com.example.dell.biliwidget;

import android.app.Activity;

import android.media.MediaCas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {
    List<Bangumi.Result.Seasons> seasonsList=new ArrayList<>();
    List<Bangumi.Result.Seasons> seasonsListres;
    Thread thread1;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this ,DividerItemDecoration.VERTICAL));
        connectwithOkhttp();

    }

    private void connectwithOkhttp() {
        thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request= new Request.Builder().url("https://bangumi.bilibili.com/web_api/timeline_global").build();
                try {
                    Response response=client.newCall(request).execute();
                    String data=response.body().string();
                    StringBuilder res=new StringBuilder();
                    res.append(data);


                    //GSON解析

                    parseJSONWithGSON(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

    }



    private void parseJSONWithGSON(String data) {
        Gson gson=new GsonBuilder().setDateFormat("MM-dd").create();
        Bangumi bangumi=gson.fromJson(data,new TypeToken<Bangumi>(){}.getType());
        List<Bangumi.Result> resultList=bangumi.getResult();
        for(Bangumi.Result result:resultList) {
            Log.i("Msg", result.toString());
            List<Bangumi.Result.Seasons> seasonsListres = result.getSeasons();
            if (result.getIs_today() == 1) { //判断是否是今天的番剧
                for (Bangumi.Result.Seasons seasons : seasonsListres) {
                    Log.i("Msg", seasons.toString());
                  //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
                    Bundle bundle=new Bundle();
                    bundle.putString("title",seasons.getTitle());;
                    bundle.putString("time",seasons.getPub_time());
                    bundle.putString("index",seasons.getPub_index());
                    Message msg=new Message();
                    msg.setData(bundle);
                    msg.what=1;
                    handler.sendMessage(msg);
                    Log.i("inf", "添加成功？");
                }
            }
        }
        // Log.i("msg2", String.valueOf(bangumi.getResult()));

    }
    private void inti() {
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle=msg.getData();
                    Log.i("list",seasonsList.toString());
                    seasonsList.add(new Bangumi.Result.Seasons(bundle.getString("title"), bundle.getString("time"), bundle.getString("index")));
                    SeasonAdapter seasonAdapter=new SeasonAdapter(seasonsList);
                    Log.i("sd","dsad");
                    seasonAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(seasonAdapter);
            }
        }
    };
    private void showres(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TextView textView = (TextView) findViewById(R.id.text);
               // textView.setText(s);
            }
        });
    }


}

