package com.example.arabicyoungsterwriterapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.RequestParams;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StoryModel implements Parcelable{
    private String title;
    private String para;
    private String  image;
    private String audio;
    private String gif;
    private String tvColor;
    private String bgColor;
    private String storyID;
    private String genre;

    public StoryModel() {
    }

    public StoryModel(Parcel in) {
        title = in.readString();
        para = in.readString();
        image = in.readString();
        audio = in.readString();
        gif = in.readString();
        tvColor = in.readString();
        bgColor = in.readString();
        storyID = in.readString();
        genre = in.readString();

    }

    public StoryModel(String title, String para, String image, String audio, String gif, String tvColor, String bgColor, String storyID, String genre) {
        this.title = title;
        this.para = para;
        this.image = image;
        this.audio = audio;
        this.gif = gif;
        this.tvColor = tvColor;
        this.bgColor = bgColor;
        this.storyID = storyID;
        this.genre = genre;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static final Creator<StoryModel> CREATOR = new Creator<StoryModel>() {
        @Override
        public StoryModel createFromParcel(Parcel in) {
            return new StoryModel(in);
        }

        @Override
        public StoryModel[] newArray(int size) {
            return new StoryModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getTvColor() {
        return tvColor;
    }

    public void setTvColor(String tvColor) {
        this.tvColor = tvColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(para);
        dest.writeString(image);
        dest.writeString(audio);
        dest.writeString(gif);
        dest.writeString(tvColor);
        dest.writeString(bgColor);
        dest.writeString(storyID);
        dest.writeString(genre);
    }

    public Map<String, Object> getMap() throws IllegalAccessException {
        // Create a new Map object to hold the keys and values
        Map<String, Object> map = new HashMap<>();

        // Get the Class object for the object
        Class<?> cls = this.getClass();

        // Get all of the object's fields
        Field[] fields = cls.getDeclaredFields();

        // Loop through the fields and add their names and values to the Map
        for (Field field : fields) {
            field.setAccessible(true); // Make the field accessible
            String fieldName = field.getName(); // Get the field's name
            Object fieldValue = field.get(fieldName); // Get the field's value
            map.put(fieldName, fieldValue); // Add the name and value to the Map
        }
        return map;
    }
}
