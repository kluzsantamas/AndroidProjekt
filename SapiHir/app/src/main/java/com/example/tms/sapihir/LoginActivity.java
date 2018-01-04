package com.example.tms.sapihir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText mloginemail;
    private  EditText mloginpasswd;
    private Button mloginbtn;
    private TextView msignup;
    private FirebaseAuth mauth;
    private DatabaseReference mdatabase;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mloginpasswd=(EditText)findViewById(R.id.loginpassword);
        mloginemail=(EditText)findViewById(R.id.loginmail);
        mloginbtn=(Button)findViewById(R.id.loginbutton);
        msignup=(Button)findViewById(R.id.signuplink);
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });

         mloginbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 checkLogin();
             }
         });
    }

    private void checkLogin() {

        String email=mloginemail.getText().toString().trim();
        String passwd=mloginpasswd.getText().toString().trim();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(passwd))
        {   mProgress.setMessage("Signing in ...");
            mProgress.show();
            mauth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override

                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        checkUserExists();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Error Login",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void checkUserExists() {
    final String user_id=mauth.getCurrentUser().getUid();
    mdatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(user_id)){
                Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
            else
            {
                Toast.makeText(LoginActivity.this,"You need to setup yout account!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    }
}
