package com.varunsen.gurughantal.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.Variables;

import org.json.JSONObject;

import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity implements PaymentResultListener {


    private Dialog dialog;
    private ImageView productImageView;
    private TextView productTitleTv, normalPriceTv, sellPriceTv, itemPriceTv, totalAmountSmallTv, totalAmountLargeTv,
            savedAmount;
    private Button continueBtn;

    private String normalPrice, sellPrice, productTitle, productId, savedPrice, imgUrl;

    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private int productType;

    //order placed layout

    private ConstraintLayout orderSuccessRootLayout;
    private Button goToMainActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase");

        dialog = new Dialog(DeliveryActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_bacgkround_white));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();


        productImageView = findViewById(R.id.product_image);
        productTitleTv = findViewById(R.id.product_title);
        normalPriceTv = findViewById(R.id.cutted_price);
        sellPriceTv = findViewById(R.id.product_price);
        itemPriceTv = findViewById(R.id.total_items_price);
        totalAmountSmallTv = findViewById(R.id.total_price);
        totalAmountLargeTv = findViewById(R.id.delivery_total_amount);
        savedAmount = findViewById(R.id.saved_amount);
        continueBtn = findViewById(R.id.cart_continue_button);

        orderSuccessRootLayout = findViewById(R.id.order_confirmation_layout);
        goToMainActivityBtn = findViewById(R.id.goToMainActBtn);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent() != null) {

            productId = getIntent().getStringExtra("product_id_intent");
            sellPrice = getIntent().getStringExtra("product_sell_price_intent");
            normalPrice = getIntent().getStringExtra("product_normal_price_intent");
            productTitle = getIntent().getStringExtra("product_title_intent");
            imgUrl = getIntent().getStringExtra("product_image_intent");

            productTitleTv.setText(productTitle);
            totalAmountLargeTv.setText("Rs. " + sellPrice + "/-");
            totalAmountSmallTv.setText("Rs. " + sellPrice + "/-");
            itemPriceTv.setText("Rs. " + sellPrice + "/-");
            sellPriceTv.setText("Rs. " + sellPrice + "/-");
            normalPriceTv.setText("Rs. " + normalPrice + "/-");
            Glide.with(DeliveryActivity.this).load(imgUrl).into(productImageView);

            int a = Integer.parseInt(normalPrice);
            int b = Integer.parseInt(sellPrice);
            int c = a-b;
            savedPrice = String.valueOf(c);

            savedAmount.setText("You saved Rs. " + savedPrice + "/-");

        } else{

            finish();
            Toast.makeText(this, "Something went wrong! Please contact support.", Toast.LENGTH_SHORT).show();

        }

        dialog.dismiss();



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user != null){
                    //startPayment(sellPrice);
                    testMethods();
                } else {
                    Dialog dialog = new Dialog(DeliveryActivity.this);
                    dialog.setContentView(R.layout.login_dialog);
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_bacgkround_white));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);

                    Button logIn = dialog.findViewById(R.id.log_in_dialog_btn);
                    logIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DeliveryActivity.this, AuthenticationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    dialog.show();
                }

            }
        });

        goToMainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveredMaterialIntent = new Intent(DeliveryActivity.this, MainActivity.class);
                MainActivity.currentFragment = productType;
                startActivity(deliveredMaterialIntent);
                finish();
            }
        });

    }

    private void startPayment(String amount){

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Guru Ghantal");
            options.put("description", "Product Name");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://www.linkpicture.com/q/PicsArt_02-11-11.37.38.jpg");
            options.put("currency", "INR");
            //If you have to transact RS 100 then you have to put amount 100 in string not in integer.
            options.put("amount", amount + "00");

            JSONObject preFill = new JSONObject();
            preFill.put("email", Variables.userEmail);
            preFill.put("contact", Variables.userMobile);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();

        firestore.collection("Products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot shot = task.getResult();

                    Long number = (long) shot.get("product_type");
                    productType = number.intValue();

                    if(productType == Variables.TYPE_COURSE) {

                        FirebaseMessaging.getInstance().subscribeToTopic("courses");

                        firestore.collection("Users").document(user.getUid())
                                .collection("User Data").document("My_Courses").update("products", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(DeliveryActivity.this, "Success!", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(DeliveryActivity.this, "Failed! 1" + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }

                } else if (productType == Variables.TYPE_NOTES){

                    firestore.collection("Users").document(user.getUid())
                            .collection("User Data").document("My_Notes").update("products", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(DeliveryActivity.this, "Success!", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(DeliveryActivity.this, "Failed! 2" + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else if (productType == Variables.TYPE_TESTS){

                    firestore.collection("Users").document(user.getUid())
                            .collection("User Data").document("My_Tests").update("products", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(DeliveryActivity.this, "Success!", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(DeliveryActivity.this, "Failed! 3" + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                }

                else{

                    String errorNumber = UUID.randomUUID().toString().substring(0,5);

                    Toast.makeText(DeliveryActivity.this, "4" + task.getException().getMessage() + "Please Contact Customer Support. Error Code:" + errorNumber, Toast.LENGTH_LONG).show();

                    //Post this error to the server
                    //notify customer support team
                    //notify billing team

                }

            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();

    }

    private void testMethods(){

        orderSuccessRootLayout.setVisibility(View.VISIBLE);

        firestore.collection("Products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot shot = task.getResult();

                    Long number = (long) shot.get("product_type");
                    productType = number.intValue();

                    if(productType == Variables.TYPE_COURSE) {

                        firestore.collection("Users").document(user.getUid())
                                .collection("USER_DATA").document("MY_COURSES").update("products", FieldValue.arrayUnion(productId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(DeliveryActivity.this, "Success!", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(DeliveryActivity.this, "Failed!" + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        );
                    }

                } else{

                    String errorNumber = UUID.randomUUID().toString().substring(0,5);

                    Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage() + "Please Contact Customer Support. Error Code:" + errorNumber, Toast.LENGTH_LONG).show();

                    //Post this error to the server
                    //notify customer support team
                    //notify billing team

                }

            }
        });

    }

}