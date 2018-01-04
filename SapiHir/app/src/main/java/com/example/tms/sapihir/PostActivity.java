package com.example.tms.sapihir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST=1;
    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private EditText mPostTelnumber;
    private Button mSubmitButton;
    private Uri mimageUri;
    private ProgressDialog mprogress;

    private StorageReference mstorage;
    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mstorage= FirebaseStorage.getInstance().getReference();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Hir");
        mSelectImage=(ImageButton) findViewById(R.id.ImageSelect);
        mPostTitle=(EditText)findViewById(R.id.HirCim);
        mPostDescription=(EditText)findViewById(R.id.HirLeiras);
        mPostTelnumber=(EditText)findViewById(R.id.HirTelefonszam);
        mSubmitButton=(Button)findViewById(R.id.SubmitBtn);
        mprogress=new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galerryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galerryintent.setType("image/*");
                startActivityForResult(galerryintent,GALLERY_REQUEST);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startPosting();

            }
        });
    }

    private void startPosting() {
        mprogress.setMessage("Hir postolasa...");

    final String title_val=mPostTitle.getText().toString().trim();
    final String desc_val=mPostDescription.getText().toString().trim();
    final String tel_val=mPostTelnumber.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val)&&!TextUtils.isEmpty(desc_val)&&!TextUtils.isEmpty(tel_val)&&mimageUri!=null)
        {   mprogress.show();
            StorageReference filepath=mstorage.child("com.example.tms.sapihir.Blog images").child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

        Uri downloadurl=taskSnapshot.getDownloadUrl();
        DatabaseReference newPost=mdatabase.push();
        newPost.child("cim").setValue(title_val);
        newPost.child("leiras").setValue(desc_val);
        newPost.child("tel").setValue(tel_val);
        newPost.child("img").setValue(downloadurl.toString());
        mprogress.dismiss();
        startActivity(new Intent(PostActivity.this,MainActivity.class));
    }
});
        }
        else
        {
            if(mPostTitle.getText().length()<5)
            {
                mPostTitle.setError("A cim nem lehet rovidebb mint 5 karakter!");
            }
            if(mPostDescription.getText().length()<10)
            {
                mPostDescription.setError("A leiras nem lehet rovidebb mint 10 karakter!");
            }
            if(mPostTelnumber.getText().length()!=10)
            {
                mPostTelnumber.setError("A telefonszam 10 szamjegyu!");
            }

        }
    }

    @Override
     protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            mimageUri=data.getData();
            mSelectImage.setImageURI(mimageUri);
        }
    }
}
