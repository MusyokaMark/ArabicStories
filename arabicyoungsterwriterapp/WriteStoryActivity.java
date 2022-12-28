package com.example.arabicyoungsterwriterapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class WriteStoryActivity extends AppCompatActivity {

    public static final String TEMPLATE = "template";
    private static final int IMAGE_SIZE = 800;
    private static final int REQ_CODE_PICK_SOUNDFILE = 100;
    private static final String TAG = "WriteStoryActivity";
    StoryModel model;
    int positon;
    ImageView coverImg;
    File file;
    Uri imageURI;
    EditText etParagraph;
    EditText etTitle;
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    int CAMERA = 0;
    int GALLERY = 1;
    boolean isUploaded = false;
    Uri imgUri;
    RelativeLayout rlTop;
    int mDefaultColor = 212121;
    String bgColor = "";
    CardView cardView;
    Button btnSelectSound;
    Uri audioFileUri;
    ImageView ivAnimation;
    Button submitButt;
    Button backButt;
    private String currentPhotoPath;
    private int uploadedTemp = 0;
    private boolean doOnce = true;
    private String tvColor = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);
        getSupportActionBar().hide();

        this.coverImg = findViewById(R.id.ivCover);
        cardView = findViewById(R.id.card_view);
        btnSelectSound = findViewById(R.id.tvAudioFileName);
        ivAnimation = findViewById(R.id.ivAnimation);
        rlTop = findViewById(R.id.rlTop);

        etTitle = findViewById(R.id.etTitle);
        etTitle.setTypeface(Configs.osRegular);

        etParagraph = findViewById(R.id.etParagraph);
        etParagraph.setTypeface(Configs.osRegular);

        backButt = findViewById(R.id.wsBackButt);
        submitButt = findViewById(R.id.btnSubmit);

        ivAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSelectSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), REQ_CODE_PICK_SOUNDFILE);
            }
        });

        findViewById(R.id.ivTvColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ColorPickerDialog
                        .Builder(WriteStoryActivity.this)
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(mDefaultColor)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection
                                tvColor = colorHex;
                                etTitle.setTextColor(Color.parseColor(colorHex));
                                etParagraph.setTextColor(Color.parseColor(colorHex));
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.ivBgColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ColorPickerDialog
                        .Builder(WriteStoryActivity.this)
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(mDefaultColor)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection
                                bgColor = colorHex;
                                cardView.setBackgroundColor(Color.parseColor(colorHex));
                            }
                        })
                        .show();
            }
        });
        this.etParagraph.addTextChangedListener(new TextWatcher() { // from class: com.domain.stories.WriteStoryActivity.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.coverImg.setOnClickListener(new View.OnClickListener() { // from class: com.domain.stories.WriteStoryActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                openGallery();
            }
        });

        submitButt.setTypeface(Configs.osBold);
        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(WriteStoryActivity.this);
                submitStory();
            }
        });

        backButt.setOnClickListener(new View.OnClickListener() { // from class: com.domain.stories.WriteStoryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WriteStoryActivity.this.finish();
            }
        });

        GetAlreadyUploaded();
    }


    public void submitStory() {
        if (WriteStoryActivity.this.etTitle.getText().toString().matches("") || WriteStoryActivity.this.etParagraph.getText().toString().matches("") || WriteStoryActivity.this.coverImg.getDrawable() == null) {
            Configs.simpleAlert("Make sure you've typed a Title, a first paragraph and uploaded a cover image!", WriteStoryActivity.this);
            return;
        }
        uploadOnFirebase();
    }

    private void GetAlreadyUploaded() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(TEMPLATE);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                uploadedTemp = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "uploaded temp: " + uploadedTemp);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


    }

    private void uploadOnFirebase() {

        StoryModel model = new StoryModel();
        model.setTitle(etTitle.getText().toString());
        model.setPara(etParagraph.getText().toString());
        model.setImage(imgUri.toString());
        model.setAudio(audioFileUri.toString());

        try {
            System.out.println(model.getMap());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();


//        client.post("",model, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(TEMPLATE);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                uploadedTemp = (int) dataSnapshot.getChildrenCount();
//
//
//                if (doOnce) {
//                    doOnce = false;
//                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//
//                    mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("title").setValue(etTitle.getText().toString());
//                    mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("para").setValue(etParagraph.getText().toString());
//                    uploadImage();
////                    Uri uriOfAudio = uploadAudio();
////                    Uri uriOfAnim = uploadAnimation();
//                    if (imgUri != null) {
//                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("image").setValue(imgUri);
//                    } else {
//                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("image").setValue("null");
//
//                    }
////
////
////                    if (uriOfAudio != null) {
////
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("audio").setValue(uriOfAudio);
////                    } else {
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("audio").setValue("null");
////
////                    }
////                    if (!bgColor.equals("")) {
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("bgColor").setValue(bgColor);
////
////                    } else {
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("bgColor").setValue("FFFFFF");
////
////                    }
////                    if (!tvColor.equals("")) {
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("tvColor").setValue(tvColor);
////
////                    } else {
////                        mDatabase.child(TEMPLATE).child(String.valueOf(uploadedTemp + 1)).child("tvColor").setValue("FFFFFF");
////
////                    }
//
//                    isUploaded = true;
//                    Toast.makeText(WriteStoryActivity.this, "Upload Your Story Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(WriteStoryActivity.this, MainActivity.class));
//                    finish();
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//            }
//        });
    }

    private Uri uploadAnimation() {
        return null;
    }

//    private Uri uploadAudio() {
//        InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        storageRef.child("audio/"+)
//        uploadTask = mountainsRef.putStream(stream);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
//        try {
//
//
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReference();    //change the url according to your firebase app
//
//            StorageReference riversRef = storageRef.child("audio/" + audioFileUri.getLastPathSegment());
//            UploadTask uploadTask = riversRef.putFile(audioFileUri);
//
//// Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    audioFileUri = taskSnapshot.getUploadSessionUri();
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return audioFileUri;
//
//    }

    public void uploadImage() {
        // Get the data from an ImageView as bytes
        coverImg.setDrawingCacheEnabled(true);
        coverImg.buildDrawingCache();
        Bitmap bitmap = coverImg.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //creating reference to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();    //change the url according to your firebase app

        long l = System.currentTimeMillis();


        StorageReference childRef = storageRef.child("Images").child("image" +l+".jpg");

        UploadTask uploadTask = childRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                return;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                childRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgUri =  uri;
                                Toast.makeText(WriteStoryActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }

        });


    }


    public void openGallery() {
        ImagePicker.with(this)
                .crop()                //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_PICK_SOUNDFILE) {
            if ((data != null) && (data.getData() != null)) {

                audioFileUri = data.getData();
                btnSelectSound.setText(getString(R.string.sucesselect));
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data != null) {
                coverImg.setImageURI(data.getData());
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


}