package com.example.dell.biliwidget;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
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
    Gson gson;
    List<Bangumi.Result> resultList;
    int cureday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this ,DividerItemDecoration.VERTICAL));
        connectwithOkhttp();


        Button next=findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gonextday();
            }
        });
        final Button pre=findViewById(R.id.pre);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gopreday();
            }
        });

    }

    @SuppressLint("WrongConstant")
    private void gopreday() {
        seasonsList.clear();
        if(cureday==0){
            Toast.makeText(MainActivity.this,"已经到头啦！",0).show();
            return;
        }
        Bangumi.Result nextres= resultList.get(cureday - 1);
        Log.i("Msg", nextres.toString());
        List<Bangumi.Result.Seasons> seasonsListres = nextres.getSeasons();
        cureday=cureday-1;
        for (Bangumi.Result.Seasons seasons : seasonsListres) {
            Log.i("Msg", seasons.toString());
            //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
            Bundle bundle=new Bundle();
            bundle.putString("title",seasons.getTitle());;
            bundle.putString("time",seasons.getPub_time());
            bundle.putString("index",seasons.getPub_index());
            bundle.putString("url",seasons.getCover());
            Message msg=new Message();
            msg.setData(bundle);
            msg.what=1;
            handler.sendMessage(msg);

        }



    }

    @SuppressLint("WrongConstant")
    private void gonextday() {
             seasonsList.clear();
            if(cureday==resultList.toArray().length-1){
            Toast.makeText(MainActivity.this,"已经到头啦！",0).show();
            return;
            }

            Bangumi.Result nextres= resultList.get(cureday + 1);
              Log.i("Msg", nextres.toString());
            List<Bangumi.Result.Seasons> seasonsListres = nextres.getSeasons();
            cureday=cureday+1;
                for (Bangumi.Result.Seasons seasons : seasonsListres) {
                         Log.i("Msg", seasons.toString());
                    //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
                    Bundle bundle=new Bundle();
                    bundle.putString("title",seasons.getTitle());;
                    bundle.putString("time",seasons.getPub_time());
                    bundle.putString("index",seasons.getPub_index());
                    bundle.putString("url",seasons.getCover());
                    Message msg=new Message();
                    msg.setData(bundle);
                    msg.what=1;
                    handler.sendMessage(msg);

                    // Log.i("inf", "添加成功？");
                }



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
        gson=new GsonBuilder().setDateFormat("MM-dd").create();
        Bangumi bangumi=gson.fromJson(data,new TypeToken<Bangumi>(){}.getType());
       resultList=bangumi.getResult();
        for(Bangumi.Result result:resultList) {
             Log.i("Msg", result.toString());
            List<Bangumi.Result.Seasons> seasonsListres = result.getSeasons();
            if (result.getIs_today() == 1) { //判断是否是今天的番剧
                cureday=resultList.indexOf(result);
                Log.i("daynum", String.valueOf(cureday));
                for (Bangumi.Result.Seasons seasons : seasonsListres) {
               //     Log.i("Msg", seasons.toString());
                  //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
                    Bundle bundle=new Bundle();
                    bundle.putString("title",seasons.getTitle());;
                    bundle.putString("time",seasons.getPub_time());
                    bundle.putString("index",seasons.getPub_index());
                    bundle.putString("url",seasons.getCover());
                    Message msg=new Message();
                    msg.setData(bundle);
                    msg.what=1;
                    handler.sendMessage(msg);
                   // Log.i("inf", "添加成功？");
                }
            }
        }
        // Log.i("msg2", String.valueOf(bangumi.getResult()));

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle=msg.getData();
                    Log.i("bundle",bundle.getString("title"));
                    seasonsList.add(new Bangumi.Result.Seasons(bundle.getString("title"), bundle.getString("time"), bundle.getString("index"),bundle.getString("url")));
                    SeasonAdapter seasonAdapter=new SeasonAdapter(seasonsList);
                    seasonAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(seasonAdapter);
                    bundle.clear();


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

