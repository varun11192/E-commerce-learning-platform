package com.varunsen.gurughantal.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Variables {

    public static String userName, userEmail, userMobile;
    public static final int TYPE_NOTES = 2, TYPE_COURSE = 1, TYPE_TESTS = 3;
    public static final int NOTES = 2, LECTURES = 1, TESTS = 3;

    public static String youtube_api_key = "AIzaSyDke6-EdHMGrJYoEzBbTUqAjU1zUbgb6yQ";

    public static void fetchPurchasedData(String documentPath, RecyclerView recyclerView, Context context){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document(documentPath)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<String> productList = new ArrayList<>();

                    if (documentSnapshot.contains("products")) {

                        productList = (List<String>) documentSnapshot.get("products");
                        List<CommonPurchasedModel> list = new ArrayList<>();

                        for (int x = 0; x < productList.size(); x++) {

                            int a = x;

                            List<String> finalProductList = productList;
                            firebaseFirestore.collection("Products")
                                    .document(productList.get(x))
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {



                                        String title = (String) task.getResult().get("product_title");
                                        String subtitle = (String) task.getResult().get("product_subtitle");
                                        Long productType = (Long) task.getResult().get("product_type");
                                        String productId = finalProductList.get(a);
                                        List<String> productImagesList = (List<String>) task.getResult().get("product_images");

                                        String image = productImagesList.get(0);

                                        Log.d("SEARCHH", "onComplete: " + title);

                                        //wait someone has came outside... i will be back in 3-4 mins
                                        //ok
                                        Log.d("abbb", "" + productType.intValue());

                                        list.add(new CommonPurchasedModel(title, subtitle, image,productType.intValue(), productId ));


                                        if (a == finalProductList.size() - 1) {


                                            CommonPurchasedAdapter adapter = new CommonPurchasedAdapter(list);
                                            recyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    } else {

                        Toast.makeText(context, "Galat baat!", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

        });
    }


}
