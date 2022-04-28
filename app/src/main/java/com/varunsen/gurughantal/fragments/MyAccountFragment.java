package com.varunsen.gurughantal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.AuthenticationActivity;
import com.varunsen.gurughantal.activities.UpdateUserInfoActivity;
import com.varunsen.gurughantal.classes.Variables;

public class MyAccountFragment extends Fragment {

    private Button logoutBtn;
    private TextView name, email, phone;
    private ImageButton settingsBtn;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);
        logoutBtn = view.findViewById(R.id.log_out_button);
        settingsBtn = view.findViewById(R.id.settingsBtn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phone.setText(Variables.userMobile);
        name.setText(Variables.userName);
        email.setText(Variables.userEmail);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent userDataUpdateIntent = new Intent(getContext(), UpdateUserInfoActivity.class);
                getActivity().startActivity(userDataUpdateIntent);

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Successfully Logged Out.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }
}