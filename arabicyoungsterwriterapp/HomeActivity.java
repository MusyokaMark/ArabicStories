package com.example.arabicyoungsterwriterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.arabicyoungsterwriterapp.createStory.CreateStoryActivity;
import com.example.arabicyoungsterwriterapp.dictionary.ViewWordMeaningActivity;
import com.example.arabicyoungsterwriterapp.myStory.MyStoryActivity;
import com.example.arabicyoungsterwriterapp.utills.StoryRequests;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnReadStory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
        findViewById(R.id.btnCreateStory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CreateStoryActivity.class));
            }
        });
        findViewById(R.id.btnDictionary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ViewWordMeaningActivity.class));
            }
        });
        findViewById(R.id.btnMyStory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyStoryActivity.class));
            }
        });
    }
}