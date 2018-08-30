/**
 * Copyright 2018 bejson.com
 */
package com.example.dell.biliwidget;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2018-07-29 19:38:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Bangumi extends DataSupport {

    private int code;
    private String message;
    private List<Result> result;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Result> getResult() {
        return result;
    }
    /**
     * Copyright 2018 bejson.com
     */
    /**
     * Auto-generated: 2018-07-29 19:38:59
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

    public static class Result extends DataSupport {

        private Date date;
        private long date_ts;
        private int day_of_week;
        private int is_today;
        private List<Seasons> seasons;

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

        public void setSeasons(List<Seasons> seasons) {
            this.seasons = seasons;
        }

        public List<Seasons> getSeasons() {
            return seasons;
        }

        @Override
        public String toString() {
            return "星期" + getDay_of_week() + " 日期" + getDate() + " 是否今天：" + getIs_today();
        }

        /**
         * Copyright 2018 bejson.com
         * Auto-generated: 2018-07-29 19:38:59
         *
         * @author bejson.com (i@bejson.com)
         * @website http://www.bejson.com/java2pojo/
         */
        public static class Seasons {

            private String cover;
            private int delay;
            private long ep_id;
            private long favorites;
            private int follow;
            private int is_published;
            private String pub_index;
            private String pub_time;
            private long pub_ts;
            private int season_id;
            private int season_status;
            private String square_cover;
            private String title;

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getCover() {
                return cover;
            }

            public void setDelay(int delay) {
                this.delay = delay;
            }

            public int getDelay() {
                return delay;
            }

            public void setEp_id(long ep_id) {
                this.ep_id = ep_id;
            }

            public long getEp_id() {
                return ep_id;
            }

            public void setFavorites(long favorites) {
                this.favorites = favorites;
            }

            public long getFavorites() {
                return favorites;
            }

            public void setFollow(int follow) {
                this.follow = follow;
            }

            public int getFollow() {
                return follow;
            }

            public void setIs_published(int is_published) {
                this.is_published = is_published;
            }

            public int getIs_published() {
                return is_published;
            }

            public void setPub_index(String pub_index) {
                this.pub_index = pub_index;
            }

            public String getPub_index() {
                return pub_index;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }

            public String getPub_time() {
                return pub_time;
            }

            public void setPub_ts(long pub_ts) {
                this.pub_ts = pub_ts;
            }

            public long getPub_ts() {
                return pub_ts;
            }

            public void setSeason_id(int season_id) {
                this.season_id = season_id;
            }

            public int getSeason_id() {
                return season_id;
            }

            public void setSeason_status(int season_status) {
                this.season_status = season_status;
            }

            public int getSeason_status() {
                return season_status;
            }

            public void setSquare_cover(String square_cover) {
                this.square_cover = square_cover;
            }

            public String getSquare_cover() {
                return square_cover;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            @Override
            public String toString() {
                return "番剧名:" + getTitle() + " " + getPub_index() + " 更新时间" + getPub_time() + "封面地址:" + getCover();
            }
            //测试爬取结果


            public Seasons(String title, String pub_index, String pub_time, String url) {
                this.pub_index = pub_index;
                this.pub_time = pub_time;
                this.title = title;
                this.cover = url;
            }
            public Seasons(String title, String pub_index, String pub_time, String url,long ep_id) {
                this.pub_index = pub_index;
                this.pub_time = pub_time;
                this.title = title;
                this.cover = url;
                this.ep_id=ep_id;
            }


        }
    }
}