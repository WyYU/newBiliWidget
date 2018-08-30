package com.example.dell.biliwidget;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaCas;
import android.net.Uri;
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

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {
    List<Bangumi.Result.Seasons> seasonsList = new ArrayList<>();
    List<Bangumi.Result.Seasons> seasonsListres;
    Thread thread1;
    RecyclerView recyclerView;
    Gson gson;
    Bangumi bangumi;
    List<Bangumi.Result> resultList;
    TextView data;
    int cureday;
    SeasonAdapter seasonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        seasonAdapter = new SeasonAdapter(seasonsList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        data = (TextView) findViewById(R.id.date);
        seasonAdapter.setOnItemClickListener(new SeasonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bangumi.Result.Seasons clseason=seasonsList.get(position);
                long ed_id=clseason.getEp_id();
                Intent intent=new Intent();
                String url="https://www.bilibili.com/bangumi/play/ep"+ed_id;
                intent.setData(Uri.parse(url));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        connectwithOkhttp();



        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gonextday();
            }
        });
        final Button pre = findViewById(R.id.pre);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gopreday();
            }
        });

    }

    private void insertDB() {


        for (Bangumi.Result r : resultList) {
            DbResult dbResult = new DbResult();
            dbResult.setDate(r.getDate());
            dbResult.setIs_today(r.getIs_today());
            dbResult.setDay_of_week(r.getDay_of_week());
            dbResult.save();


        }
        Bangumi B = new Bangumi();
        B.setResult(resultList);
        B.save();

    }

    private void createDB() {
        LitePal.getDatabase();
    }

    @SuppressLint("WrongConstant")
    private void gopreday() {
        seasonsList.clear();
        if (cureday == 0) {
            Toast.makeText(MainActivity.this, "已经到头啦！", 0).show();
            return;
        }
        Bangumi.Result nextres = resultList.get(cureday - 1);
        List<Bangumi.Result.Seasons> seasonsListres = nextres.getSeasons();
        cureday = cureday - 1;
        for (Bangumi.Result.Seasons seasons : seasonsListres) {
            //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
            Bundle bundle = new Bundle();
            bundle.putString("title", seasons.getTitle());
            ;
            bundle.putString("time", seasons.getPub_time());
            bundle.putString("index", seasons.getPub_index());
            bundle.putString("url", seasons.getCover());
            bundle.putInt("season_id",seasons.getSeason_id());
            bundle.putLong("ep_id",seasons.getEp_id());
            Message msg = new Message();
            ;
            msg.setData(bundle);
            msg.what = 1;
            handler.sendMessage(msg);

        }


    }

    @SuppressLint("WrongConstant")
    private void gonextday() {
        seasonsList.clear();
        if (cureday == resultList.toArray().length - 1) {
            Toast.makeText(MainActivity.this, "已经到头啦！", 0).show();
            return;
        }

        Bangumi.Result nextres = resultList.get(cureday + 1);
        List<Bangumi.Result.Seasons> seasonsListres = nextres.getSeasons();
        cureday = cureday + 1;
        for (Bangumi.Result.Seasons seasons : seasonsListres) {
            //  seasonsList.add(new Bangumi.Result.Seasons(seasons.getPub_index(), "更新时间" + seasons.getPub_time(), seasons.getTitle()));
            Bundle bundle = new Bundle();
            bundle.putString("title", seasons.getTitle());
            ;
            bundle.putString("time", seasons.getPub_time());
            bundle.putString("index", seasons.getPub_index());
            bundle.putString("url", seasons.getCover());
            bundle.putInt("season_id",seasons.getSeason_id());
            bundle.putLong("ep_id",seasons.getEp_id());
            Message msg = new Message();
            msg.setData(bundle);
            msg.what = 1;
            handler.sendMessage(msg);

            // Log.i("inf", "添加成功？");
        }


    }


    private void connectwithOkhttp() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://bangumi.bilibili.com/web_api/timeline_global").build();
                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    StringBuilder res = new StringBuilder();
                    res.append(data);
                    //GSON解析
                    parseJSONWithGSON(data);
                    createDB();
                    insertDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

    }


    private void parseJSONWithGSON(String data) {
        gson = new GsonBuilder().setDateFormat("MM-dd").create();
        bangumi = gson.fromJson(data, new TypeToken<Bangumi>() {
        }.getType());
        resultList = bangumi.getResult();
        for (Bangumi.Result result : resultList) {
            List<Bangumi.Result.Seasons> seasonsListres = result.getSeasons();
            if (result.getIs_today() == 1) { //判断是否是今天的番剧
                cureday = resultList.indexOf(result);
                for (Bangumi.Result.Seasons seasons : seasonsListres) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", seasons.getTitle());
                    bundle.putString("time", seasons.getPub_time());
                    bundle.putString("index", seasons.getPub_index());
                    bundle.putString("url", seasons.getCover());
                    bundle.putInt("season_id",seasons.getSeason_id());
                    bundle.putLong("ep_id",seasons.getEp_id());
                    bundle.putString("data", String.valueOf(result.getDay_of_week()));
                    Message msg = new Message();
                    msg.setData(bundle);
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    seasonsList.add(new Bangumi.Result.Seasons(bundle.getString("title"), bundle.getString("time"), bundle.getString("index"), bundle.getString("url"),bundle.getLong("ep_id")));

                    seasonAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(seasonAdapter);

                    String week=bundle.getString("data");
              //      Log.i("data",week);

                    bundle.clear();


            }
        }
    };


}

