package com.example.arabicyoungsterwriterapp.utills;

import com.example.arabicyoungsterwriterapp.StoryModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StoryRequests {
    public static boolean GETSTORIES = false;
    public static List<StoryModel> getStories() {
        List<StoryModel> myList = new ArrayList<>();
        System.out.println("Fetch stories");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Urls.STORY, new BaseJsonHttpResponseHandler<List<StoryModel>>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, List<StoryModel> response) {
                System.out.println("Successfully request made!");
                System.out.println(response.size());
                myList.addAll(response);
                GETSTORIES = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, List<StoryModel> errorResponse) {
                System.out.println("Something Happened!");
                GETSTORIES = true;
            }

            @Override
            protected List<StoryModel> parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                // Parse the response as a JSONArray
                JSONArray jsonArray = new JSONArray(rawJsonData.toString());
                List<StoryModel> str = new ArrayList<StoryModel>();
                Gson gson = new Gson();
                // Loop through the JSONArray and create a StoryModel object for each item
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Get the JSONObject at the current index
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Create a new StoryModel object from the JSONObject
                    StoryModel story = gson.fromJson(jsonObject.toString(), StoryModel.class);
                    // Add the StoryModel object to the ArrayList

                    str.add(story);
                }
                return str;
            }
        });

        return myList;
    }
    public static boolean addStory(StoryModel story){
        return false;
    }
    public static boolean login(String email, String password){
        return false;
    }
    public static boolean register(User user){
        return false;
    }
    public static StoryModel getStory(String id){
        return null;
    }
    public static boolean updateStory(StoryModel story){
        return false;
    }
    public static boolean removeStory(StoryModel story){
        return false;
    }
    public static List<StoryModel> getStoryByGenres(String genre){
        return null;
    }



    private static class User {
        private String username;
        private String password;
        private String email;
    }
}

