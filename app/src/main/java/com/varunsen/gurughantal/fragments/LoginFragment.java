package com.varunsen.gurughantal.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;

import static com.varunsen.gurughantal.activities.AuthenticationActivity.isLogInFragment;


public class LoginFragment extends Fragment {

    private TextView forgotPassTv, signUpTv, tryAppTv;
    private FrameLayout parentFrameLayout;
    private ImageView closeBtn;
    private Button loginBtn;
    private EditText emailEt, passEt;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        isLogInFragment = true;
        forgotPassTv = view.findViewById(R.id.forgotPassTv);
        signUpTv = view.findViewById(R.id.signUpTv);
        tryAppTv = view.findViewById(R.id.tryAppTv);
        closeBtn = view.findViewById(R.id.closeBtn);
        emailEt = view.findViewById(R.id.emailEt);
        passEt = view.findViewById(R.id.passwordEt);
        loginBtn = view.findViewById(R.id.loginBtn);
        progressBar = view.findViewById(R.id.progBar);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!emailEt.getText().toString().equals("")){

                    if(!passEt.getText().toString().equals("")){

                        // ready to authenticate.

                        loginBtn.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        loginBtn.setText("");


                            String email = emailEt.getText().toString().trim();
                            String password = passEt.getText().toString().trim();

                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();

                                        Toast.makeText(getContext(), "Login Success!", Toast.LENGTH_SHORT).show();

                                    } else{

                                        Toast.makeText(getContext(), "Some error occurred. " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                        loginBtn.setEnabled(true);

                                    }

                                    progressBar.setVisibility(View.INVISIBLE);
                                    loginBtn.setText("LOG IN");

                                }
                            });

                    } else{
                        passEt.setError("Please enter the Password.");
                    }

                } else{

                    emailEt.setError("Please enter the Email.");

                }

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

        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isLogInFragment = false;
                changeFragment(new ForgotPasswordFragment());

            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isLogInFragment = false;
                changeFragment(new SignUpFragment());

            }
        });

        tryAppTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

    }

    private void changeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}





