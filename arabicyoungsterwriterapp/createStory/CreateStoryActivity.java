package com.example.arabicyoungsterwriterapp.createStory;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.arabicyoungsterwriterapp.Configs;
import com.example.arabicyoungsterwriterapp.HomeActivity;
import com.example.arabicyoungsterwriterapp.MainActivity;
import com.example.arabicyoungsterwriterapp.R;
import com.example.arabicyoungsterwriterapp.StoryModel;
import com.example.arabicyoungsterwriterapp.myStory.MyStoryDBManager;
import com.example.arabicyoungsterwriterapp.template.TemplateActivity;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateStoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STORIES = "stories";
    private static final int REQUEST_CODE_AUDIO = 100;
    private static final int REQUEST_CODE_IMAGE = 101;
    private static final int PERMISSION_CODE_READ = 102;
    EditText etTitle, etParagraph;
    ImageView ivAudio, ivText, ivPicture, ivTemplate, ivCancel, ivDelete, ivSave, ivCopy, ivAddPage, ivBg;
    Uri audioUri;
    Uri picUri;
    MyStoryDBManager myStoryDBManager;
    AlertDialog alertDialog;
    boolean isUploaded = false;
    String fileName;
    String path;
    int mDefaultColor = 212121;
    String tvColor = "#FF000000";
    String bgColor = "#FFFFFFFF";
    RelativeLayout llMain;
    private int uploadedTemp = 0;
    private boolean doOnce = true;
    private String imageUrl;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);


        getSupportActionBar().setTitle("Create Story");
        initViews();
        myStoryDBManager = new MyStoryDBManager(this);
        myStoryDBManager.open();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etParagraph = findViewById(R.id.etParagraph);
        ivAudio = findViewById(R.id.ivAudio);
        ivText = findViewById(R.id.ivText);
        ivPicture = findViewById(R.id.ivPicture);
        ivTemplate = findViewById(R.id.ivTemplate);
        ivCancel = findViewById(R.id.ivCancel);
        ivDelete = findViewById(R.id.ivDelete);
        ivSave = findViewById(R.id.ivSave);
        ivCopy = findViewById(R.id.ivCopy);
        ivAddPage = findViewById(R.id.ivAddPage);
        ivBg = findViewById(R.id.ivBg);
        llMain = findViewById(R.id.llMain);


        ivAudio.setOnClickListener(this);
        ivText.setOnClickListener(this);
        ivPicture.setOnClickListener(this);
        ivTemplate.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        ivCopy.setOnClickListener(this);
        ivAddPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivAudio:

                addAudio();

                break;
            case R.id.ivText:

                addText();

                break;
            case R.id.ivPicture:
                addPicture();

                break;
            case R.id.ivTemplate:

                template();

                break;
            case R.id.ivCancel:
                cancelStory();

                break;
            case R.id.ivDelete:

                deleteTitleAndParagraph();

                break;
            case R.id.ivSave:

                saveStory();
                break;
            case R.id.ivCopy:

                copyParagraph();

                break;
            case R.id.ivAddPage:

                addPage();

                break;

        }
    }

    private void addPage() {

    }

    private void copyParagraph() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", etParagraph.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Data Copied", Toast.LENGTH_SHORT).show();
    }

    private void saveStory() {

        final String title = etTitle.getText().toString();
        final String para = etParagraph.getText().toString();
        if (!title.equals("") && !para.equals("")) {

            StoryModel model = new StoryModel();
            model.setTitle(etTitle.getText().toString());
            model.setPara(etParagraph.getText().toString());
            model.setImage(imageUrl);
            model.setAudio(audioUrl);

//            try {
//                System.out.println(model.getMap());
//            } catch (IllegalAccessException e) {
//                System.out.println(e.getMessage());
//            }

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();

//            saveInDataBase(title, para, path);
//            saveDataOnFirebase();
        } else {
            Toast.makeText(this, "Title Paragraph and image is required", Toast.LENGTH_SHORT).show();
        }


    }

    private void saveDataOnFirebase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(STORIES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                uploadedTemp = (int) dataSnapshot.getChildrenCount();


                if (doOnce) {
                    doOnce = false;
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("title").setValue(etTitle.getText().toString());
                    mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("para").setValue(etParagraph.getText().toString());

                    if (imageUrl != null) {
                        mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("image").setValue(imageUrl);
                    } else {
                        mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("image").setValue("null");

                    }
                    if (audioUrl != null) {
                        mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("audio").setValue(audioUrl);
                    } else {
                        mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("audio").setValue("null");

                    }
                    mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("bgColor").setValue(bgColor);
                    mDatabase.child(STORIES).child(String.valueOf(uploadedTemp + 1)).child("tvColor").setValue(tvColor);


                    isUploaded = true;
                    Toast.makeText(CreateStoryActivity.this, "Upload Your Story Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateStoryActivity.this, MainActivity.class));
                    finish();


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(CreateStoryActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveInDataBase(String title, String paragraph, String imgUrl) {

        myStoryDBManager.insert(title, paragraph, imgUrl, tvColor, bgColor);
        Toast.makeText(this, "Successfully in Database", Toast.LENGTH_SHORT).show();

    }

    private void deleteTitleAndParagraph() {

        startActivity(new Intent(CreateStoryActivity.this, CreateStoryActivity.class));
        finish();

    }

    private void cancelStory() {

        startActivity(new Intent(CreateStoryActivity.this, HomeActivity.class));
        finish();
    }

    private void template() {

        startActivity(new Intent(CreateStoryActivity.this, TemplateActivity.class));
    }

    private void addPicture() {


        ImagePicker.with(this)
                .crop()                //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .saveDir(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))
                .start();
    }

    private void addText() {

        String textToPaste = null;

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();

            // if you need text data only, use:
            if (clip.getDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))
                // WARNING: The item could cantain URI that points to the text data.
                // In this case the getText() returns null and this code fails!
                textToPaste = clip.getItemAt(0).getText().toString();

            // or you may coerce the data to the text representation:
            textToPaste = clip.getItemAt(0).coerceToText(this).toString();
        }

        if (!TextUtils.isEmpty(textToPaste))
            etParagraph.append(textToPaste);
    }

    private void addAudio() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/mpeg");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload, REQUEST_CODE_AUDIO);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_READ);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_READ:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent_upload = new Intent();
                    intent_upload.setType("audio/mpeg");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload, REQUEST_CODE_AUDIO);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateStoryActivity.this);
                    builder.setTitle("Permission");
                    builder.setMessage("This Permission for Your Audio file Otherwise you can not use this functionality");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addAudio();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_AUDIO) {


                //the selected audio.
                if (data != null) {

                    audioUri = data.getData();
                    ivAudio.setColorFilter(R.color.green);
                    uploadAudioToFirebase(audioUri);


                }

            } else {
                //Image Uri will not be null for RESULT_OK
                if (data != null) {

                    picUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                        path = saveToInternalStorage(bitmap);
                        path = path + "/" + fileName;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUrl = picUri.toString();
                    ivBg.setImageURI(data.getData());
                    ivPicture.setColorFilter(R.color.green);


                    uploadImageToFirebase(picUri);

                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_color, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.actionTvColor:

                new ColorPickerDialog
                        .Builder(CreateStoryActivity.this)
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(mDefaultColor)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection

                                tvColor = colorHex;
                                etTitle.setTextColor(Color.parseColor(tvColor));
                                etParagraph.setTextColor(Color.parseColor(tvColor));
                            }
                        })
                        .show();
                break;
            case R.id.actionBgColor:
                new ColorPickerDialog
                        .Builder(CreateStoryActivity.this)
                        .setTitle("Pick Theme")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(mDefaultColor)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection

                                bgColor = colorHex;
                                llMain.setBackgroundColor(Color.parseColor(bgColor));
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        fileName = "tempFile" + System.currentTimeMillis() + ".jpg";
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getFilesDir();
        File file;

        fileName = "tempFile" + System.currentTimeMillis();

        file = File.createTempFile(fileName, ".jpg", storageDir);

        return file;
    }

    private void uploadImageToFirebase(Uri uri) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait...");
        builder.setMessage("Your Image is Uploading...");
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        if (CreateStoryActivity.this.etTitle.getText().toString().matches("") || CreateStoryActivity.this.etParagraph.getText().toString().matches("") && picUri == null) {
            Configs.simpleAlert("Make sure you've typed a Title, a first paragraph and uploaded a cover image!", CreateStoryActivity.this);
            alertDialog.dismiss();
            return;
        }

        ivBg.setDrawingCacheEnabled(true);
        ivBg.buildDrawingCache();
        Bitmap bitmap = ivBg.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //creating reference to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();    //change the url according to your firebase app

        long l = System.currentTimeMillis();


        StorageReference childRef = storageRef.child("Images").child("image" + l + ".jpg");

        UploadTask uploadTask = childRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                return;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        alertDialog.dismiss();

                        Toast.makeText(CreateStoryActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();
                    }
                });


            }

        });

    }

    private void uploadAudioToFirebase(Uri uri) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait...");
        builder.setMessage("Your Audio is Uploading...");
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        //creating reference to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();    //change the url according to your firebase app

        long l = System.currentTimeMillis();


        StorageReference childRef = storageRef.child("audio").child("audio" + l + ".mp3");

        UploadTask uploadTask = childRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                return;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        alertDialog.dismiss();
                        Toast.makeText(CreateStoryActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();

                        audioUrl = uri.toString();
                    }
                });


            }

        });

    }


}