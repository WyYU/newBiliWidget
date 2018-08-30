package com.example.dell.biliwidget;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2018/8/9 0009.
 */

public class DbResult extends DataSupport {
    private Date date;
    private long date_ts;
    private int day_of_week;
    private int is_today;
    private List<Bangumi.Result.Seasons> seasons;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate_ts(long date_ts) {
        this.date_ts = date_ts;
    }

    public long getDate_ts() {
        return date_ts;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setIs_today(int is_today) {
        this.is_today = is_today;
    }

    public int getIs_today() {
        return is_today;
    }

    public void setSeasons(List<Bangumi.Result.Seasons> seasons) {
        this.seasons = seasons;
    }

    public List<Bangumi.Result.Seasons> getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "星期" + getDay_of_week() + " 日期" + getDate() + " 是否今天：" + getIs_today();
    }
}
