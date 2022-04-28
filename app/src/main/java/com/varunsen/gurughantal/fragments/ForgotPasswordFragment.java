package com.varunsen.gurughantal.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.varunsen.gurughantal.R;

import static com.varunsen.gurughantal.activities.AuthenticationActivity.isLogInFragment;

public class ForgotPasswordFragment extends Fragment {

    private TextView goBackBtn;
    private FrameLayout parentFrameLayout;
    private EditText emailEt;
    private Button resetPassBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        goBackBtn = view.findViewById(R.id.btnGoBack);
        emailEt = view.findViewById(R.id.emailEt);
        resetPassBtn = view.findViewById(R.id.resetPassBtn);
        progressBar = view.findViewById(R.id.progBar);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        parentFrameLayout = getActivity().findViewById(R.id.frameLayoutAuth);

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!emailEt.getText().toString().equals("")) {

                    progressBar.setVisibility(View.VISIBLE);
                    resetPassBtn.setEnabled(false);
                    resetPassBtn.setText("");

                    firebaseAuth.sendPasswordResetEmail(emailEt.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(getContext(), "Email sent successfully! Don't forget to check spam folder too. ", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getContext(), "Something went wrong. " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                            }

                            progressBar.setVisibility(View.INVISIBLE);
                            resetPassBtn.setEnabled(true);
                            resetPassBtn.setText("RESET PASSWORD");

                        }
                    });

                } else {

                    emailEt.setError("Please enter your registered Email.");

                }
                
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isLogInFragment = true;
                changeFragment(new LoginFragment());

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