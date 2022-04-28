package com.varunsen.gurughantal.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.Variables;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private EditText nameEt, phoneEt, oldPassEt, newPassEt, cnfPassEt;
    private Button updateBtn;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    public static Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEt = findViewById(R.id.full_name_et);
        phoneEt = findViewById(R.id.mobile_et);
        oldPassEt = findViewById(R.id.old_pass_et);
        newPassEt = findViewById(R.id.new_pass_et);
        cnfPassEt = findViewById(R.id.cnf_new_pass_et);
        updateBtn = findViewById(R.id.updateBtn);

        nameEt.setText(Variables.userName);
        phoneEt.setText(Variables.userMobile);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loading dialog
                loadingDialog = new Dialog(UpdateUserInfoActivity.this);
                loadingDialog.setContentView(R.layout.loading_dialog);
                loadingDialog.setCancelable(false);
                loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
                loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingDialog.show();
                //end loading dialog

                if (newPassEt.getText().toString().equals("")){
                    //user is not changing password, only updating name and mobile
                    Map<String, Object> updateUserData = new HashMap<>();
                    updateUserData.put("full_name", nameEt.getText().toString());
                    updateUserData.put("mobile_number", phoneEt.getText().toString());

                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                            .update(updateUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(UpdateUserInfoActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateUserInfoActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();

                        }
                    });
                } else {
                    //user is changing the password
                    if (!oldPassEt.getText().toString().equals("")){
                        if (newPassEt.getText().toString().equals(cnfPassEt.getText().toString()) && newPassEt.getText().toString().length() >= 6){
                            //update in authentication
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            final String email = user.getEmail();

                            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassEt.getText().toString());

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        user.updatePassword(newPassEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> updateUserPass = new HashMap<>();
                                                    updateUserPass.put("full_name", nameEt.getText().toString());
                                                    updateUserPass.put("mobile_number", phoneEt.getText().toString());

                                                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                                                            .update(updateUserPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(UpdateUserInfoActivity.this, "Profile and Password updated successfully!", Toast.LENGTH_SHORT).show();
                                                                loadingDialog.dismiss();
                                                                finish();
                                                            } else {
                                                                Toast.makeText(UpdateUserInfoActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }   else {
                                                    Toast.makeText(UpdateUserInfoActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(UpdateUserInfoActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        } else{
                            Toast.makeText(UpdateUserInfoActivity.this, "Please enter same password in Confirm Password or Password should be at least 6 characters long.", Toast.LENGTH_LONG).show();
                            loadingDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(UpdateUserInfoActivity.this, "Please enter your old password.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else{
            return false;
        }

    }

}