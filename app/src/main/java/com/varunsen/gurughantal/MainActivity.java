package com.varunsen.gurughantal;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.activities.AuthenticationActivity;
import com.varunsen.gurughantal.activities.ProductDetailsActivity;
import com.varunsen.gurughantal.classes.Variables;
import com.varunsen.gurughantal.fragments.HomeFragment;
import com.varunsen.gurughantal.fragments.MyAccountFragment;
import com.varunsen.gurughantal.fragments.MyCoursesFragment;
import com.varunsen.gurughantal.fragments.MyNotesFragment;
import com.varunsen.gurughantal.fragments.MyTestsFragment;
import com.varunsen.gurughantal.fragments.NotificationFragment;

import java.util.Random;

public class    MainActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    public static DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout parentFrameLayout;
    private TextView titleTv;
    private ImageView notificationTop;
    private FirebaseAuth firebaseAuth;
    public Dialog dialog;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;


    public static int currentFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.mainDrawerLayout);
        toolbar = findViewById(R.id.activity_main_toolbar);
        parentFrameLayout = findViewById(R.id.main_activity_frameLayout);
        titleTv = findViewById(R.id.titleTvMain);
        notificationTop = findViewById(R.id.notificationIconTop);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.login_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_bacgkround_white));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        createNotificationChannel();

        Button logIn = dialog.findViewById(R.id.log_in_dialog_btn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        navigationView.setItemIconTintList(null);

        if(currentFragment == -1) {

            setFragment(new HomeFragment());

        } else if (currentFragment == Variables.TYPE_COURSE){

            setFragment(new MyCoursesFragment());

        } else if (currentFragment == Variables.TYPE_NOTES){

            setFragment(new MyNotesFragment());

        } else if (currentFragment == Variables.TYPE_TESTS){

            setFragment(new MyTestsFragment());

        } else {
            setFragment(new HomeFragment());
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

           MenuItem menuItem;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item;

                    drawerLayout.closeDrawer(GravityCompat.START);

                    drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);

                            switch (menuItem.getItemId()) {

                                case R.id.nav_home:

                                    if(user != null) {

                                        goToFragment(new HomeFragment(), "Guru Ghantal");

                                    } else{

                                        dialog.show();
                                    }

                                    break;

                                case R.id.nav_myCourses:

                                    if(user != null) {

                                        goToFragment(new MyCoursesFragment(), "My Courses");
                                    } else{

                    dialog.show();
                                    }

                                    break;

                                case R.id.nav_myNotes:

                                    if(user != null) {

                                        goToFragment(new MyNotesFragment(), "My Notes");
                                    } else{

                    dialog.show();
                                    }

                                    break;

                                case R.id.nav_myTests:

                                    if(user != null) {

                                        goToFragment(new MyTestsFragment(), "My Tests");
                                    } else {

                    dialog.show();
                                    }

                                    break;

                                case R.id.nav_myAccount:

                                    if(user != null) {

                                        goToFragment(new MyAccountFragment(), "My Account");
                                    } else {

                    dialog.show();
                                    }

                                    break;

                                case R.id.nav_notifications:

                                    if(user != null) {

                                        goToFragment(new NotificationFragment(), "Notifications");
                                    } else {

                    dialog.show();
                                    }
                                    break;

                            }

                            drawerLayout.removeDrawerListener(this);

                        }


                    });



                drawerLayout.closeDrawer(GravityCompat.START);

                drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);

                        switch (menuItem.getItemId()) {

                            case R.id.nav_about:

                                Intent aboutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gurughantal.godaddysites.com/"));
                                startActivity(aboutIntent);

                                break;

                            case R.id.nav_shareIcon:

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I want to recommend you this app for all your study needs.\n\n");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://gurughantal.godaddysites.com/");
                                startActivity(shareIntent);

                                break;

                            case R.id.nav_instagram:

                                Uri uri = Uri.parse("https://www.instagram.com/0_guru_ghantal_0/");
                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                likeIng.setPackage("com.instagram.android");

                                try {
                                    startActivity(likeIng);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(MainActivity.this, "Instagram not installed. Opening in Browser.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://www.instagram.com/0_guru_ghantal_0/")));
                                }

                                break;

                            case R.id.nav_youtube:

                                Intent int1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCsJmG3QnL-6wrOrRxEQFNvA"));
                                int1.setPackage("com.google.android.youtube");
                                startActivity(int1);

                                break;

                            case R.id.nav_telegram:

                                try {

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=GuruGhantalOfficial"));
                                    startActivity(intent);

                                } catch(ActivityNotFoundException e){

                                    Toast.makeText(MainActivity.this, "Telegram not installed.", Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case R.id.nav_logOut:

                                FirebaseAuth.getInstance().signOut();

                                //todo: clear all data of this user.

                                Intent signOut = new Intent(MainActivity.this, AuthenticationActivity.class);
                                startActivity(signOut);
                                finish();

                        }

                        drawerLayout.removeDrawerListener(this);

                    }
                });


                return true;
            }
        });



        notificationTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user == null) {

                    dialog.show();
                } else {
                    goToFragment(new NotificationFragment(), "Notification");
                }
            }
        });

        SharedPreferences sh = getSharedPreferences("UserBasics", Context.MODE_PRIVATE);

        if (sh.contains("user_name") && sh.contains("user_email") && sh.contains("user_phone")){

            Variables.userName = sh.getString("user_name", "");
            Variables.userEmail = sh.getString("user_email", "");
            Variables.userMobile = sh.getString("user_phone", "");

        } else {

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            SharedPreferences sharedPreferences = getSharedPreferences("UserBasics", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_name", (String) task.getResult().get("full_name"));
                            editor.putString("user_email", (String) task.getResult().get("email"));
                            editor.putString("user_phone", (String) task.getResult().get("mobile_number"));
                            editor.commit();

                        } else {
                            //do nothing
                        }

                    }
                });
            } else {
                // do nothing
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(currentFragment == -1) {

            setFragment(new HomeFragment());

        } else if (currentFragment == Variables.TYPE_COURSE){

            setFragment(new MyCoursesFragment());

        } else if (currentFragment == Variables.TYPE_NOTES){

            setFragment(new MyNotesFragment());

        } else if (currentFragment == Variables.TYPE_TESTS){

            setFragment(new MyTestsFragment());

        } else {
            setFragment(new HomeFragment());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentFragment == -1) {

            setFragment(new HomeFragment());

        } else if (currentFragment == Variables.TYPE_COURSE){

            setFragment(new MyCoursesFragment());

        } else if (currentFragment == Variables.TYPE_NOTES){

            setFragment(new MyNotesFragment());

        } else if (currentFragment == Variables.TYPE_TESTS){

            setFragment(new MyTestsFragment());

        } else {
            setFragment(new HomeFragment());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(currentFragment == -1) {

            setFragment(new HomeFragment());

        } else if (currentFragment == Variables.TYPE_COURSE){

            setFragment(new MyCoursesFragment());

        } else if (currentFragment == Variables.TYPE_NOTES){

            setFragment(new MyNotesFragment());

        } else if (currentFragment == Variables.TYPE_TESTS){

            setFragment(new MyTestsFragment());

        } else {
            setFragment(new HomeFragment());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void goToFragment(Fragment fragment, String title){

        setFragment(fragment);
        titleTv.setText(title);

    }


    private void triggerNotification() {

        Random random = new Random();
        int id = random.nextInt(9999);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product_id", "grgt001");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL001")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("New Product Recommendation!")
                .setContentText("DSA Lectures Full Series")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Basic Notification";
            String description = "General Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL001", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(parentFrameLayout.getId(),fragment);
        transaction.commit();

    }


}