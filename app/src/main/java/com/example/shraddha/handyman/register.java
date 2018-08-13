package com.example.shraddha.handyman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.util.Date;

public class register extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText nameEdittext, phoneEditText, passwordEditText, emailEditText, addressEditText;
    private RadioGroup occupationRadioGroup;
    private RadioButton radioButton;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String token_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEdittext = (EditText)findViewById(R.id.nameEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        addressEditText = (EditText)findViewById(R.id.addressEditText);
        occupationRadioGroup = (RadioGroup)findViewById(R.id.occupationRadioGroup);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("handymans");
        progressDialog = new ProgressDialog(this);

        findViewById(R.id.loginbutton).setEnabled(false);

        findViewById(R.id.registerButton).setOnClickListener(this);
        findViewById(R.id.loginbutton).setOnClickListener(this);
    }

    private void registerUser(){
        String name, password, email, phone, address, rb;
        int selectedId;

        if(TextUtils.isEmpty(nameEdittext.getText().toString().trim())){
            Toast.makeText(register.this, "Name is required", Toast.LENGTH_SHORT).show();
            nameEdittext.requestFocus();
            return;
        }
        else
            name = nameEdittext.getText().toString().trim();

        if(TextUtils.isEmpty(passwordEditText.getText().toString().trim())){
            Toast.makeText(register.this, "Password is required", Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return;
        }
        else
            password = passwordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(emailEditText.getText().toString().trim())){
            Toast.makeText(register.this, "Email is required", Toast.LENGTH_SHORT).show();
            emailEditText.requestFocus();
            return;
        }
        else
            email = emailEditText.getText().toString().trim();

        if(TextUtils.isEmpty(phoneEditText.getText().toString().trim())){
            Toast.makeText(register.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            phoneEditText.requestFocus();
            return;
        }
        else
            phone = phoneEditText.getText().toString().trim();

        if(TextUtils.isEmpty(addressEditText.getText().toString().trim())){
            Toast.makeText(register.this, "Address is required", Toast.LENGTH_SHORT).show();
            addressEditText.requestFocus();
            return;
        }
        else
            address = addressEditText.getText().toString().trim();

        String dateJoined = DateFormat.getDateTimeInstance().format(new Date());

        if(occupationRadioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(register.this, "Occupation is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            selectedId = occupationRadioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton)findViewById(selectedId);
            rb = radioButton.getText().toString().trim();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.requestFocus();
            emailEditText.setError("Invaild Email");
            return;
        }
        if(password.length()<6){
            passwordEditText.setError("Minimum length of password should be 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if(task.isSuccessful()){
                            Toast.makeText(register.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
                            token_id =  FirebaseInstanceId.getInstance().getToken();
                            String currentUser_Id = mAuth.getCurrentUser().getUid();

                            databaseReference.child(currentUser_Id).child("tokenId").setValue(token_id);
                        }
                        else
                            Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
        findViewById(R.id.loginbutton).setEnabled(true);
    }

    private void loginUser(){
        String name = nameEdittext.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String dateJoined = DateFormat.getDateTimeInstance().format(new Date());
        int selectedId = occupationRadioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton)findViewById(selectedId);
        String rb = radioButton.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    token_id =  FirebaseInstanceId.getInstance().getToken();
                                    String currentUser_Id = mAuth.getCurrentUser().getUid();

                                    databaseReference.child(currentUser_Id).child("tokenId").setValue(token_id);
                                }
                            });
                        }
                        else
                            Toast.makeText(register.this, "SignIn Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        handymanInfo info = new handymanInfo(email, password, name, phone, rb, address, dateJoined,token_id);
        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(info);
        Toast.makeText(this, "Welcome!!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), tracker.class);
        startActivity(i);
        setContentView(R.layout.activity_tracker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                registerUser();
                break;
            case R.id.loginbutton:
                loginUser();
                break;
        }
    }
}
