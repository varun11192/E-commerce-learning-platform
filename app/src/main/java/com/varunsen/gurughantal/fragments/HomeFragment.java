package com.varunsen.gurughantal.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.CategoriesAdapter;
import com.varunsen.gurughantal.classes.CategoriesModel;
import com.varunsen.gurughantal.classes.HomePageAdapter;
import com.varunsen.gurughantal.classes.HomePageModel;
import com.varunsen.gurughantal.classes.HorizontalItemModel;
import com.varunsen.gurughantal.classes.ViewAllEeModel;

import java.util.ArrayList;
import java.util.List;

import static com.varunsen.gurughantal.classes.HomePageModel.BANNER;
import static com.varunsen.gurughantal.classes.HomePageModel.GRID;
import static com.varunsen.gurughantal.classes.HomePageModel.HORIZONTAL;


public class HomeFragment extends Fragment {

    private RecyclerView categoriesRV, homeMainRv;
    private TextView scrollableTv;
    private List<CategoriesModel> categoryList;
    private List<HorizontalItemModel> horizontalList;
    private List<HomePageModel> homeList;
    private List<ViewAllEeModel> viewAllList;
    private List<String> posterUrlList;
    private List<String> productFakeList;

    //fake lists

    private List<CategoriesModel> categoryFakeList;
    private List<String> bannerFakeList;
    private List<HorizontalItemModel> mixedFakeList;
    private List<HomePageModel> homeFakeList;

    //end fake lists

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private FirebaseFirestore firestore;
    private SwipeRefreshLayout refreshLayout;

    private ImageView noInternetImage;
    private Button retryBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        categoriesRV = view.findViewById(R.id.categoriesRV);
        scrollableTv = view.findViewById(R.id.scrollableTv);
        homeMainRv = view.findViewById(R.id.homeRecyclerView);
        refreshLayout = view.findViewById(R.id.homeSwipeLayout);
        noInternetImage = view.findViewById(R.id.no_internet_image);
        retryBtn = view.findViewById(R.id.retryBtn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //scrolling textView

        scrollableTv.setSelected(true);
        scrollableTv.setText("                                             This application is under development. Various versions of it were distributed to Beta testers by the developer. The version you are currently using is also a test application and may contain various bugs. Kindly explore the application, use it to the fullest and report any problems to the developer. Have a good day ahead :) ");

        //scrolling textView end.

        categoryList = new ArrayList<>();
        horizontalList = new ArrayList<>();
        homeList = new ArrayList<>();
        viewAllList = new ArrayList<>();
        posterUrlList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        bannerFakeList = new ArrayList<>();
        categoryFakeList = new ArrayList<>();
        mixedFakeList = new ArrayList<>();
        homeFakeList = new ArrayList<>();
        productFakeList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        categoriesRV.setLayoutManager(manager);

        LinearLayoutManager homeLm = new LinearLayoutManager(getContext());
        homeLm.setOrientation(RecyclerView.VERTICAL);
        homeMainRv.setLayoutManager(homeLm);

        //Filling Fake

        productFakeList.add("");
        productFakeList.add("");
        productFakeList.add("");
        productFakeList.add("");

        bannerFakeList.add("");
        bannerFakeList.add("");
        bannerFakeList.add("");
        bannerFakeList.add("");

        categoryFakeList.add(new CategoriesModel("", ""));
        categoryFakeList.add(new CategoriesModel("", ""));
        categoryFakeList.add(new CategoriesModel("", ""));
        categoryFakeList.add(new CategoriesModel("", ""));
        categoryFakeList.add(new CategoriesModel("", ""));

        mixedFakeList.add(new HorizontalItemModel("", "", "", "", ""));
        mixedFakeList.add(new HorizontalItemModel("", "", "", "", ""));
        mixedFakeList.add(new HorizontalItemModel("", "", "", "" ,""));
        mixedFakeList.add(new HorizontalItemModel("", "", "", "", ""));

        homeFakeList.add(new HomePageModel(BANNER, bannerFakeList));
        homeFakeList.add(new HomePageModel(GRID, "", "#ffffff",productFakeList));
        homeFakeList.add(new HomePageModel(HORIZONTAL, "", productFakeList));
        homeFakeList.add(new HomePageModel(HORIZONTAL, "", productFakeList));

        //End filling fake lists

        CategoriesAdapter adapter = new CategoriesAdapter(categoryFakeList);
        categoriesRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        HomePageAdapter hAdapter = new HomePageAdapter(homeFakeList);
        homeMainRv.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){

            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            homeMainRv.setVisibility(View.VISIBLE);
            categoriesRV.setVisibility(View.VISIBLE);
            scrollableTv.setVisibility(View.VISIBLE);

            noInternetImage.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);

            loadCategories();

            loadHomeFragment();


        } else {

            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            homeMainRv.setVisibility(View.INVISIBLE);
            categoriesRV.setVisibility(View.INVISIBLE);
            scrollableTv.setVisibility(View.INVISIBLE);

            noInternetImage.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

        }

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!reloadPage()){
                    Toast.makeText(getContext(), "Sorry, No Internet Connection Available.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshLayout.setRefreshing(true);

                homeList.clear();
                categoryList.clear();

                CategoriesAdapter adapter = new CategoriesAdapter(categoryFakeList);
                categoriesRV.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                HomePageAdapter hAdapter = new HomePageAdapter(homeFakeList);
                homeMainRv.setAdapter(hAdapter);
                hAdapter.notifyDataSetChanged();

                loadCategories();

                loadHomeFragment();

            }
        });

    }

    private Boolean reloadPage(){

        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){

            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            homeList.clear();
            categoryList.clear();

            homeMainRv.setVisibility(View.VISIBLE);
            categoriesRV.setVisibility(View.VISIBLE);
            scrollableTv.setVisibility(View.VISIBLE);
            noInternetImage.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);

            CategoriesAdapter adapter = new CategoriesAdapter(categoryFakeList);
            categoriesRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            HomePageAdapter hAdapter = new HomePageAdapter(homeFakeList);
            homeMainRv.setAdapter(hAdapter);
            hAdapter.notifyDataSetChanged();

            loadCategories();

            loadHomeFragment();

            return true;

        } else{

            MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            homeMainRv.setVisibility(View.INVISIBLE);
            categoriesRV.setVisibility(View.INVISIBLE);
            scrollableTv.setVisibility(View.INVISIBLE);

            noInternetImage.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

            if(refreshLayout.isRefreshing()){

                refreshLayout.setRefreshing(false);

            }

            return false;

        }

    }

    private void loadCategories(){


        firestore.collection("Categories")
                .orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for(DocumentSnapshot shot : task.getResult()){

                        String name = (String) shot.get("category_name");
                        String icon = (String) shot.get("category_icon");

                        categoryList.add(new CategoriesModel(name, icon));

                    }

                    CategoriesAdapter adapter = new CategoriesAdapter(categoryList);

                    categoriesRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadHomeFragment(){


        firestore.collection("Categories")
                .document("Home").collection("ToDisplay")
                .orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(DocumentSnapshot shot : task.getResult()){

                        long layoutType = (long) shot.get("layout_type");

                        if(layoutType == BANNER){

                            List<String> bannerList = (List<String>) shot.get("url_list");

                            homeList.add(new HomePageModel(BANNER, bannerList));

                        } else if (layoutType == HORIZONTAL){

                            String layoutTitle = (String) shot.get("layout_title");
                            List<String> productList = (List<String>) shot.get("product_list");

                            homeList.add(new HomePageModel(HORIZONTAL, layoutTitle, productList));

                        } else if (layoutType == GRID){

                            String bgColor = (String) shot.get("bg_color");
                            String layoutTitle = (String) shot.get("layout_title");
                            List<String> productList = (List<String>) shot.get("product_list");

                            homeList.add(new HomePageModel(GRID, layoutTitle, bgColor ,productList));


                        } else {
                            // Don't do anything.
                        }

                    }

                    HomePageAdapter homePageAdapter = new HomePageAdapter(homeList);
                    homeMainRv.setAdapter(homePageAdapter);
                    homePageAdapter.notifyDataSetChanged();

                    if(refreshLayout.isRefreshing()){

                        refreshLayout.setRefreshing(false);

                    }

                } else {
                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}