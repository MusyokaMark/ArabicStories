package com.example.arabicyoungsterwriterapp.myStory;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arabicyoungsterwriterapp.R;

import java.util.ArrayList;

public class MyStoryActivity extends AppCompatActivity {

    ArrayList<Model> modelArrayList;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    Cursor cursor;
    private MyStoryDBManager myStoryDBManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_story);
        getSupportActionBar().setTitle("My Story");

        recyclerView = findViewById(R.id.rv);
        modelArrayList = new ArrayList<>();
        myStoryDBManager = new MyStoryDBManager(this);
        myStoryDBManager.open();


        cursor = myStoryDBManager.fetch();

        modelArrayList = new ArrayList<>();

        //create method
        modelArrayList = displayData();
        myAdapter = new MyAdapter(modelArrayList, MyStoryActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyStoryActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);

    }

    private ArrayList<Model> displayData() {
        ArrayList<Model> modelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                modelArrayList.add(new Model(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }


    public class Model {
        private int id;
        private String title;
        private String paragraph;
        private String image_url;
        private String tv_color;
        private String tv_back;

        public Model(int id, String title, String paragraph, String image_url, String tv_color, String tv_back) {
            this.id = id;
            this.title = title;
            this.paragraph = paragraph;
            this.image_url = image_url;
            this.tv_color = tv_color;
            this.tv_back = tv_back;
        }



        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getParagraph() {
            return paragraph;
        }

        public void setParagraph(String paragraph) {
            this.paragraph = paragraph;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getTv_color() {
            return tv_color;
        }

        public void setTv_color(String tv_color) {
            this.tv_color = tv_color;
        }

        public String getTv_back() {
            return tv_back;
        }

        public void setTv_back(String tv_back) {
            this.tv_back = tv_back;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHoder> {
        private final ArrayList<Model> modelArrayList;
        private final Context context;

        //constructor

        public MyAdapter(ArrayList<Model> modelArrayList, Context context) {
            this.modelArrayList = modelArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mystory_row, parent, false);
            return new ViewHoder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHoder holder, int position) {
            Model model = modelArrayList.get(position);
            holder.tvTitle.setText(model.getTitle());
            holder.tvParagraph.setText(model.getParagraph());
            Glide.with(context).load(model.getImage_url()).into(holder.ivBack);
            holder.ivDeleteStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myStoryDBManager.delete(modelArrayList.get(position).getId());
                    Toast.makeText(context, "Story Deleted", Toast.LENGTH_SHORT).show();
                    myAdapter.notifyDataSetChanged();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent modify_intent = new Intent(getApplicationContext(), MyStoryViewActivity.class);
                    modify_intent.putExtra("title", modelArrayList.get(position).title);
                    modify_intent.putExtra("paragraph", modelArrayList.get(position).getParagraph());
                    modify_intent.putExtra("image_url", modelArrayList.get(position).getImage_url());
                    modify_intent.putExtra("tv_color", modelArrayList.get(position).getTv_color());
                    modify_intent.putExtra("tv_back", modelArrayList.get(position).getTv_back());

                    startActivity(modify_intent);
                }
            });

            //icon background random color
//            Random random=new Random();
//            int color= Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(256));
//            holder.ivBack.setBackgroundColor(color);
        }

        @Override
        public int getItemCount() {
            return modelArrayList.size();
        }

        public class ViewHoder extends RecyclerView.ViewHolder {
            private final TextView tvTitle;
            private final TextView tvParagraph;
            private final ImageView ivBack;
            private final ImageView ivDeleteStory;

            public ViewHoder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvParagraph = itemView.findViewById(R.id.tvParagraph);
                ivBack = itemView.findViewById(R.id.ivBack);
                ivDeleteStory = itemView.findViewById(R.id.ivDeleteStory);
            }
        }
    }


}