package com.example.arabicyoungsterwriterapp;

import static com.example.arabicyoungsterwriterapp.createStory.CreateStoryActivity.STORIES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arabicyoungsterwriterapp.utills.StoryRequests;
import com.example.arabicyoungsterwriterapp.utills.Urls;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;


import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    List<StoryModel> list = new ArrayList<>();
    RecyclerView stories_rv;
    DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private StoriesAdapter adapter;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        list = StoryRequests.myList;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(toolbar);

        stories_rv = findViewById(R.id.stories_rv);
        if (stories_rv != null) {
            stories_rv.setLayoutManager(new LinearLayoutManager(this));
            this.stories_rv.setItemAnimator(new DefaultItemAnimator());
        }


        FirebaseApp.initializeApp(this);

        databaseReference = database.getReference(STORIES);

        loadStory();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionRefresh){
            loadStory();

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadStory() {
        DiaWait.alerDialogeWaiting(this, "Loading Your Stories");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Urls.STORY, new BaseJsonHttpResponseHandler<List<StoryModel>>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, List<StoryModel> response) {
                System.out.println("Successfully request made!");
                System.out.println(response.size());
                list.addAll(response);
                StoryRequests.GETSTORIES = false;
                adapter = new StoriesAdapter(MainActivity.this, list);
                stories_rv.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, List<StoryModel> errorResponse) {
                System.out.println("Something Happened!");
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DiaWait.closeDia();
            }
        },1000);

    }

    private void writeToJsonFile(List<StoryModel> list) {
        // Create a Gson instance
        Gson gson = new Gson();

        try {
            File dir = getExternalFilesDir(null);

// Create a file in the directory
            File file = new File(dir, "my_file.json");
            System.out.println(file.getAbsolutePath());
            // Create a FileWriter instance
            FileWriter writer = new FileWriter(file);

            // Convert the list of objects to a JSON array
            String json = gson.toJson(list);

            // Write the JSON array to the file
            writer.write(json);

            // Close the FileWriter instance
            writer.close();
        } catch (IOException e) {
            // Handle errors
            System.out.println(e);
        }
    }


    public static class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesVH> {
        private final Context context;
        private final List<StoryModel> list;

        public StoriesAdapter(Context context, List<StoryModel> list) {
            this.list = list;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public StoriesVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.story_row, parent, false);
            return new StoriesVH(view);
        }

        public void onBindViewHolder(final StoriesVH holder, @SuppressLint("RecyclerView") int position) {
            final StoryModel sObj = list.get(position);
            holder.titleTxt.setText(sObj.getTitle());
            holder.paragraphsTxt.setText(sObj.getPara());
            Glide.with(context)
                    .load(sObj.getImage())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivBack);

            holder.ivDeleteStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete

                    Toast.makeText(context, "Admin can delete", Toast.LENGTH_SHORT).show();

                }
            });
            holder.ivEditStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to edit activity
                    Intent intent = new Intent(context, EditStoryActivity.class);
                    intent.putExtra("key", list.get(position));
                    intent.putExtra("pos", position);
                    context.startActivity(intent);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewStoryActivity.class);
                    intent.putExtra("key", list.get(position));
                    context.startActivity(intent);

                }
            });

        }

        public int getItemCount() {
            return list.size();
        }

        public class StoriesVH extends RecyclerView.ViewHolder {
            private final ImageView ivBack, ivEditStory, ivDeleteStory;
            private final TextView paragraphsTxt;
            private final TextView titleTxt;

            public StoriesVH(View finalCell) {
                super(finalCell);
                this.titleTxt = finalCell.findViewById(R.id.tvTitles);
                this.ivBack = finalCell.findViewById(R.id.ivBack);
                this.paragraphsTxt = finalCell.findViewById(R.id.tvParagraph);
                this.ivEditStory = finalCell.findViewById(R.id.ivEditStory);
                this.ivDeleteStory = finalCell.findViewById(R.id.ivDeleteStory);
            }
        }
    }
}