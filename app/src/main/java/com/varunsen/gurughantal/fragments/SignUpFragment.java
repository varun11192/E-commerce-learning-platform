package com.varunsen.gurughantal.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.AuthenticationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {

    private ImageView closeBtn;
    private TextView logInBtn;
    private FrameLayout parentFrameLayout;
    private EditText emailEt, passwordEt, cnfPass, fullNameEt, mobileEt;
    private Button signUpButton;
    private ProgressBar progressBar;


    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        closeBtn = view.findViewById(R.id.closeBtn);
        logInBtn = view.findViewById(R.id.logInTv);
        emailEt = view.findViewById(R.id.emailEt);
        passwordEt = view.findViewById(R.id.passEt);
        signUpButton = view.findViewById(R.id.signupBtn);
        cnfPass = view.findViewById(R.id.cnfPassEt);
        fullNameEt = view.findViewById(R.id.fullNameEt);
        mobileEt = view.findViewById(R.id.phoneEt);
        progressBar = view.findViewById(R.id.progBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        fullNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnfPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailEt.getText().toString().matches(emailPattern)) {
                    if (passwordEt.getText().toString().equals(cnfPass.getText().toString())) {
                        // ready to authenticate!

                        signUpButton.setEnabled(false);

                        progressBar.setVisibility(View.VISIBLE);
                        signUpButton.setText("");

                        String email = emailEt.getText().toString();
                        String password = passwordEt.getText().toString();

                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                           if(task.isSuccessful()){

                                               Map<String, Object> basicDetails = new HashMap<>();
                                               basicDetails.put("full_name", fullNameEt.getText().toString());
                                               basicDetails.put("mobile_number", mobileEt.getText().toString());
                                               basicDetails.put("email", emailEt.getText().toString());

                                               firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                                                       .set(basicDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {

                                                       if(task.isSuccessful()){

                                                           CollectionReference userDataReferences = firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                           List<String> documentsName = new ArrayList<>();

                                                           documentsName.add("MY_RATINGS");
                                                           documentsName.add("MY_COURSES");
                                                           documentsName.add("MY_TESTS");
                                                           documentsName.add("MY_NOTES");

                                                           Map<String, Long> totalMap = new HashMap<>();
                                                           totalMap.put("total", 0L);

                                                           for(int i = 0; i < documentsName.size(); i++){

                                                                userDataReferences.document(documentsName.get(i)).set(totalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if(task.isSuccessful()){

                                                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserBasics", Context.MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                            editor.putString("user_name", fullNameEt.getText().toString());
                                                                            editor.putString("user_email", emailEt.getText().toString());
                                                                            editor.putString("user_phone", mobileEt.getText().toString());
                                                                            editor.commit();

                                                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                                                            startActivity(intent);
                                                                            getActivity().finish();

                                                                        } else {

                                                                            firebaseAuth.signOut();
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            signUpButton.setText("SIGN UP");

                                                                            Toast.makeText(getContext(), "Something Went Wrong! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                                        }

                                                                    }
                                                                });

                                                           }





                                                       } else {

                                                           firebaseAuth.signOut();
                                                           progressBar.setVisibility(View.INVISIBLE);
                                                           signUpButton.setText("SIGN UP");

                                                           Toast.makeText(getContext(), "Error Occurred! : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                                                       }

                                                   }
                                               });



                                           } else{

                                               progressBar.setVisibility(View.INVISIBLE);
                                               signUpButton.setText("SIGN UP");

                                               Toast.makeText(getContext(), "Some error occurred. " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                                           }
                                        }
                                    });

                                    Toast.makeText(getContext(), "Your account has been created!", Toast.LENGTH_SHORT).show();

                                } else{

                                    Toast.makeText(getContext(), "Some error occurred. " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    signUpButton.setEnabled(true);

                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpButton.setText("SIGN UP");
                                }



                            }
                        });

                    } else{

                        cnfPass.setError("Password does not matches!");

                    }
                };

                    }

                });



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthenticationActivity.isLogInFragment = true;
                changeFragment(new LoginFragment());


            }
        });

    }

    private void checkInputs(){

        if(!fullNameEt.getText().toString().equals("")){
               //full name has been filled till now

            if(!emailEt.getText().toString().equals("")){

                if(!mobileEt.getText().toString().equals("") && mobileEt.getText().toString().length() == 10){

                    if((!passwordEt.getText().toString().equals("")) && (passwordEt.getText().toString().length() >= 6)){

                        if(!cnfPass.getText().toString().equals("")){

                                signUpButton.setEnabled(true);

                        } else {
                            signUpButton.setEnabled(false);
                            cnfPass.setError("Please enter Confirm Password Field.");
                        }

                    } else {
                        signUpButton.setEnabled(false);
                        passwordEt.setError("Password must be of at least 6 characters.");
                    }

                } else {
                    signUpButton.setEnabled(false);
                    mobileEt.setError("Mobile number should be of 10 digits.");
                }

            } else {
                signUpButton.setEnabled(false);
                emailEt.setError("Please enter Email field.");
            }

        } else{

            signUpButton.setEnabled(false);
            fullNameEt.setError("Please enter Full Name field!");

        }

    }

    private void changeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}