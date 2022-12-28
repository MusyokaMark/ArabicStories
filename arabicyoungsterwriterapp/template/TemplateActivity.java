package com.example.arabicyoungsterwriterapp.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arabicyoungsterwriterapp.EditStoryActivity;
import com.example.arabicyoungsterwriterapp.R;
import com.example.arabicyoungsterwriterapp.StoryModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TemplateActivity extends AppCompatActivity {

    List<StoryModel> list = new ArrayList<>();
    RecyclerView stories_rv;
    DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private TemplateActivity.StoriesAdapter adapter;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template);
        getSupportActionBar().setTitle("Default Template");

        stories_rv = findViewById(R.id.stories_rv);
        if (stories_rv != null) {
            stories_rv.setLayoutManager(new LinearLayoutManager(this));
            this.stories_rv.setItemAnimator(new DefaultItemAnimator());
        }



        FirebaseApp.initializeApp(this);

        databaseReference = database.getReference("defaultTemp");

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
        showAlertDialoge();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot.toString());
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StoryModel value = postSnapshot.getValue(StoryModel.class);
                    list.add(value);
                }

                adapter = new StoriesAdapter(TemplateActivity.this, list);
                stories_rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dismissDialoge();
    }

    public void showAlertDialoge(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Load Data...");
        builder.setMessage("Please Wait while fetching your data");

        alertDialog = builder.create();
        alertDialog.show();
    }
    public void dismissDialoge(){
        alertDialog.dismiss();
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
            return new StoriesAdapter.StoriesVH(view);
        }

        public void onBindViewHolder(final StoriesVH holder, @SuppressLint("RecyclerView") int position) {
            final StoryModel sObj = list.get(position);
            holder.titleTxt.setText(sObj.getTitle());
            holder.titleTxt.setTextColor(Color.parseColor(sObj.getTvColor()));
            holder.paragraphsTxt.setText(sObj.getPara());
            holder.paragraphsTxt.setTextColor(Color.parseColor(sObj.getTvColor()));
            holder.llMain.setBackgroundColor(Color.parseColor(sObj.getBgColor()));



            Glide.with(context)
                    .load(sObj.getImage())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivBack);

            holder.ivDeleteStory.setVisibility(View.INVISIBLE);
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
                    Intent intent = new Intent(context, ViewTemplateActivity.class);
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
            private final RelativeLayout llMain;

            public StoriesVH(View finalCell) {
                super(finalCell);
                this.titleTxt = finalCell.findViewById(R.id.tvTitles);
                this.ivBack = finalCell.findViewById(R.id.ivBack);
                this.paragraphsTxt = finalCell.findViewById(R.id.tvParagraph);
                this.ivEditStory = finalCell.findViewById(R.id.ivEditStory);
                this.ivDeleteStory = finalCell.findViewById(R.id.ivDeleteStory);
                this.llMain = finalCell.findViewById(R.id.llMain);
            }
        }
    }
}