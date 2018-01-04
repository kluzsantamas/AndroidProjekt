package com.example.tms.sapihir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailfield;
    private EditText mPasswordfield1;
    private EditText mPasswordfield2;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mdatabase;
    private Button mtomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mtomain=(Button)findViewById(R.id.tomain);
        mNameField=(EditText)findViewById(R.id.teljes_nev);
        mEmailfield=(EditText)findViewById(R.id.email);
        mPasswordfield1=(EditText)findViewById(R.id.password1);
        mPasswordfield2=(EditText)findViewById(R.id.password2);
        mRegisterBtn=(Button)findViewById(R.id.Register_button);
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth=FirebaseAuth.getInstance();

        mProgress=new ProgressDialog(this);

        mtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }

    private void startRegister() {
         final String name=mNameField.getText().toString().trim();
        String email=mEmailfield.getText().toString().trim();
        String passwd1=mPasswordfield1.getText().toString().trim();
        String passwd2=mPasswordfield2.getText().toString().trim();
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(passwd1)&&passwd1.equals(passwd2))
        { mProgress.setMessage("Signing Up ...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email,passwd1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {mProgress.setMessage("Signing Up  ...\n User created!...");
                        mProgress.show();
                        String user_id=mAuth.getCurrentUser().getUid();
                        DatabaseReference curent_user_db= mdatabase.child("user_id");
                        curent_user_db.child("name").setValue(name);
                        curent_user_db.child("image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent =new Intent(RegisterActivity.this,MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

                }
            });
        }
        else
        {
            if(mNameField.getText().length()<3)
            {
                mNameField.setError("A cim nem lehet rovidebb mint 3 karakter!");
            }
            if(mEmailfield.getText().length()<5)
            {
                mEmailfield.setError("Helytelen E-mail cim!");
            }
            if(!passwd1.equals(passwd2))
            {
                mPasswordfield2.setError("A 2 kod nem egyezik meg!");
            }
            if(passwd1.length()<6)
            {
                mPasswordfield2.setError("A kod minimum 6 karakter!");
            }

        }

    }
}
