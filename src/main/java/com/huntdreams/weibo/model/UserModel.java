package com.huntdreams.weibo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 用户模型
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class UserModel implements Parcelable {

    public transient long timestamp = System.currentTimeMillis(); // Time when wrote to database

    private String nameWithRemark;

    // Json mapping fields
    public String id;
    public String screen_name;
    public String name;
    public String remark;
    public String province;
    public String city;
    public String location;
    public String description;
    public String url;
    public String profile_image_url;
    public String domain;
    public String gender;
    public String cover_image = "";
    public String cover_image_phone = "";
    public String created_at;
    public String avatar_large;
    public String verified_reason;
    public int followers_count = 0;
    public int friends_count = 0;
    public int statuses_count = 0;
    public int favourites_count = 0;
    public int verified_type = 0;
    public boolean following = false;
    public boolean allow_all_act_msg = false;
    public boolean geo_enabled = false;
    public boolean verified = false;
    public boolean allow_all_comment = false;
    public boolean follow_me = false;
    public int online_status = 0;
    public int bi_followers_count = 0;

    public String getName() {
        if (TextUtils.isEmpty(remark)){
            return screen_name == null ? name : screen_name;
        } else if (nameWithRemark == null){
            nameWithRemark = String.format("%s(%s)", (screen_name == null ? name : screen_name), remark);
        }
        return nameWithRemark;
    }

    public String getNameNoRemark() {
        return screen_name == null ? name : screen_name;
    }

    public String getCover() {
        return cover_image.trim().equals("") ? cover_image_phone : cover_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(screen_name);
        dest.writeString(name);
        dest.writeString(remark);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(profile_image_url);
        dest.writeString(domain);
        dest.writeString(gender);
        dest.writeString(created_at);
        dest.writeString(avatar_large);
        dest.writeString(verified_reason);
        dest.writeInt(followers_count);
        dest.writeInt(friends_count);
        dest.writeInt(statuses_count);
        dest.writeInt(favourites_count);
        dest.writeInt(verified_type);
        dest.writeInt(online_status);
        dest.writeInt(bi_followers_count);
        dest.writeString(cover_image_phone);
        dest.writeString(cover_image);
        dest.writeBooleanArray(new boolean[]{following, allow_all_act_msg, geo_enabled, verified, allow_all_comment});
    }
}
