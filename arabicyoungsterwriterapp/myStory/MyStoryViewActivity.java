package com.example.arabicyoungsterwriterapp.myStory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arabicyoungsterwriterapp.R;

public class MyStoryViewActivity extends AppCompatActivity {

    TextView tvTitle, tvParagraph;
    ImageView ivMain;
    RelativeLayout llMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_view);


        ivMain = findViewById(R.id.ivMain);
        tvTitle = findViewById(R.id.tvTitle);
        tvParagraph = findViewById(R.id.textGraph);
        llMain = findViewById(R.id.llMain);

        Intent intent = getIntent();
        if (intent != null){
            String title = intent.getStringExtra("title");
            String paragraph = intent.getStringExtra("paragraph");
            String imageUrl = intent.getStringExtra("image_url");
            String tvColor = intent.getStringExtra("tv_color");
            String tvBack = intent.getStringExtra("tv_back");

            tvTitle.setText(title);
            tvParagraph.setText(paragraph);
            tvTitle.setBackgroundColor(Color.parseColor(tvColor));
            tvParagraph.setBackgroundColor(Color.parseColor(tvColor));
            llMain.setBackgroundColor(Color.parseColor(tvBack));
            tvParagraph.setText(paragraph);
            tvParagraph.setText(paragraph);
            if (imageUrl != null){

                Uri uri =  Uri.parse(imageUrl);
                ivMain.setImageURI(uri);
            }


        }
    }
}