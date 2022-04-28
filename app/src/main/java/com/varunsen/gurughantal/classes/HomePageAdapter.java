package com.varunsen.gurughantal.classes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.ProductDetailsActivity;
import com.varunsen.gurughantal.activities.ViewAllActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.varunsen.gurughantal.activities.ViewAllActivity.viewAllActivityList;
import static com.varunsen.gurughantal.classes.HomePageModel.BANNER;
import static com.varunsen.gurughantal.classes.HomePageModel.GRID;
import static com.varunsen.gurughantal.classes.HomePageModel.HORIZONTAL;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> list;

    private FirebaseFirestore firestore;

    public HomePageAdapter(List<HomePageModel> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getType()){

            case BANNER:

                return 0;

            case HORIZONTAL:

                return 1;

            case GRID:

                return 2;

            default:

                return -1;

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){

            case BANNER:

                View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_slider_root_layout, parent, false);

                return new PosterSliderView(view0);

            case HORIZONTAL:

                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_slider_layout, parent, false);

                return new HorizontalProductView(view1);

            case GRID:

                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);

                return new GridProductView(view2);

            default:

                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (list.get(position).getType()){

            case BANNER:

                ((PosterSliderView)holder).setPosters(list.get(position).getPostersList());

                break;

            case HORIZONTAL:
                ((HorizontalProductView)holder).setHorizontalData(list.get(position).getLayoutTitle(), list.get(position).getProductList() );

                break;

            case GRID:
                ((GridProductView)holder).setGridData(list.get(position).getBgColor(), list.get(position).getLayoutTitle(), list.get(position).getProductList());

                break;

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HorizontalProductView extends RecyclerView.ViewHolder{

        private TextView layoutTitleTv;
        private RecyclerView recyclerView;
        private Button viewAllBtn;
        private List<HorizontalItemModel> horizontalList;
        private List<ViewAllEeModel> viewAllList;

        public HorizontalProductView(@NonNull View itemView) {
            super(itemView);

            layoutTitleTv = itemView.findViewById(R.id.layoutTitle);
            recyclerView = itemView.findViewById(R.id.horizontalScrollRecyclerView);
            viewAllBtn = itemView.findViewById(R.id.viewAllBtn);
            firestore = FirebaseFirestore.getInstance();

        }

        public void setHorizontalData(String title, List<String> productList){

            layoutTitleTv.setText(title);
            horizontalList = new ArrayList<>();
            viewAllList = new ArrayList<>();


            LinearLayoutManager horizontalRvManager = new LinearLayoutManager(itemView.getContext());
            horizontalRvManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(horizontalRvManager);

            // Fetching product details

            if(!productList.get(0).equals("")) {


                for(int x = 0; x < productList.size(); x++){

                int a = x;


                    firestore.collection("Products").document(productList.get(x)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot shot = task.getResult();

                            if (task.isSuccessful()) {

                                String productTitle = (String) shot.get("product_title");
                                String productSubtitle = (String) shot.get("product_subtitle");
                                String productPrice = (String) shot.get("product_price");
                                String productNormalPrice = (String) shot.get("product_normal_price");

                                List<String> productImagesList = (List<String>) shot.get("product_images");
                                String productImgUrl = (String) productImagesList.get(0);

                                horizontalList.add(new HorizontalItemModel(productList.get(a) ,productImgUrl, productTitle, productSubtitle, productPrice));
                                viewAllList.add(new ViewAllEeModel(productList.get(a) , productImgUrl, productTitle, productSubtitle, productPrice, productNormalPrice));

                                if (a == productList.size() - 1) {

                                    HorizontalProductAdapter horiAdapter = new HorizontalProductAdapter(horizontalList);
                                    recyclerView.setAdapter(horiAdapter);
                                    horiAdapter.notifyDataSetChanged();

                                }

                            } else {
                                Toast.makeText(itemView.getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            } else {

                horizontalList.add(new HorizontalItemModel("","", "", "", ""));
                horizontalList.add(new HorizontalItemModel("","", "", "", ""));
                horizontalList.add(new HorizontalItemModel("","", "", "", ""));
                horizontalList.add(new HorizontalItemModel("","", "", "", ""));
                horizontalList.add(new HorizontalItemModel("","", "", "", ""));

                HorizontalProductAdapter adapter = new HorizontalProductAdapter(horizontalList);
                recyclerView.setAdapter(adapter);
                 adapter.notifyDataSetChanged();

            }

            // End

            viewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewAllActivityList = viewAllList;
                    Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_title", title);
                    itemView.getContext().startActivity(viewAllIntent);

                }
            });

        }

    }

    public class GridProductView extends RecyclerView.ViewHolder{

        private ConstraintLayout rootLayout;
        private GridLayout gridLayout;
        private TextView layoutTitle;
        private Button viewAllGridBtn;
        private List<ViewAllEeModel> viewAllList;
        private List<HorizontalItemModel> gridList;

        public GridProductView(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView.findViewById(R.id.gridParentLayout);
            gridLayout = itemView.findViewById(R.id.gridProdLayout);
            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            viewAllGridBtn = itemView.findViewById(R.id.viewAllBtn);
            firestore = FirebaseFirestore.getInstance();

        }

        private void setGridData(String backgroundColor, String title, List<String> productList){

            rootLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(backgroundColor)));
            layoutTitle.setText(title);

            viewAllList = new ArrayList<>();
            gridList = new ArrayList<>();

            if(!productList.get(0).equals("")){

                for(int x = 0; x<productList.size(); x++){

                    int a = x;

                    firestore.collection("Products").document(productList.get(x)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot shot = task.getResult();

                            if(task.isSuccessful()){

                                String productTitle = (String) shot.get("product_title");
                                String productSubtitle = (String) shot.get("product_subtitle");
                                String productPrice = (String) shot.get("product_price");
                                String productNormalPrice = (String) shot.get("product_normal_price");

                                List<String> productImagesList = (List<String>) shot.get("product_images");
                                String productImgUrl = (String) productImagesList.get(0);

                                gridList.add(new HorizontalItemModel(productList.get(a), productImgUrl, productTitle, productSubtitle, productPrice));
                                viewAllList.add(new ViewAllEeModel(productList.get(a), productImgUrl, productTitle, productSubtitle, productPrice, productNormalPrice));

                                if(a == productList.size() - 1){

                                    for (int x = 0; x < 4; x++){

                                        TextView prodTitle = gridLayout.getChildAt(x).findViewById(R.id.titleTv);
                                        TextView prodSubtitle = gridLayout.getChildAt(x).findViewById(R.id.subtitleTv);
                                        TextView prodPrice = gridLayout.getChildAt(x).findViewById(R.id.priceTV);
                                        ImageView prodImage = gridLayout.getChildAt(x).findViewById(R.id.productImageView);

                                        prodTitle.setText(gridList.get(x).getTitle());
                                        prodSubtitle.setText(gridList.get(x).getSubtitle());
                                        prodPrice.setText("Rs. " + gridList.get(x).getPrice() + "/-");

                                        Glide.with(itemView.getContext()).load(gridList.get(x).getImageResource()).into(prodImage);

                                        gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //todo: to pass identification data through intent.
                                                Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                                                intent.putExtra("product_id", productList.get(a));
                                                itemView.getContext().startActivity(intent);

                                            }
                                        });


                                    }

                                }

                            } else {
                                Toast.makeText(itemView.getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            } else {

                gridList.add(new HorizontalItemModel("","", "", "", ""));
                gridList.add(new HorizontalItemModel("","", "", "", ""));
                gridList.add(new HorizontalItemModel("","", "", "", ""));
                gridList.add(new HorizontalItemModel("","", "", "", ""));

                for (int x = 0; x < 4; x++){

                    TextView prodTitle = gridLayout.getChildAt(x).findViewById(R.id.titleTv);
                    TextView prodSubtitle = gridLayout.getChildAt(x).findViewById(R.id.subtitleTv);
                    TextView prodPrice = gridLayout.getChildAt(x).findViewById(R.id.priceTV);

                    prodTitle.setText(gridList.get(x).getTitle());
                    prodSubtitle.setText(gridList.get(x).getSubtitle());
                    prodPrice.setText(gridList.get(x).getPrice());

                }

            }




            viewAllGridBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewAllActivityList = viewAllList;
                    Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_title", title);
                    itemView.getContext().startActivity(viewAllIntent);

                }
            });


        }

    }

    public class PosterSliderView extends RecyclerView.ViewHolder{

        private ViewPager posterViewPager;
        private List<String> arrangedList;
        private int currentPage;
        private Timer timer;
        final static int DELAY_TIME = 3000, PERIOD_TIME = 3000;

        public PosterSliderView(@NonNull View itemView) {
            super(itemView);

            posterViewPager = itemView.findViewById(R.id.poster_slider_view_pager);

        }

        private void setPosters(List<String> imageUrls){

            if(timer != null){
                timer.cancel();
            }

            currentPage = 2;
            arrangedList = new ArrayList<>();

            for( int x = 0; x < imageUrls.size(); x++){
                arrangedList.add(imageUrls.get(x));
            }
            arrangedList.add(0, imageUrls.get(imageUrls.size() - 1));
            arrangedList.add(1, imageUrls.get(imageUrls.size() - 2));

            arrangedList.add(imageUrls.get(0));
            arrangedList.add(imageUrls.get(1));

            PosterAdapter adapter = new PosterAdapter(arrangedList);
            posterViewPager.setAdapter(adapter);

            posterViewPager.setPageMargin(20);

            posterViewPager.setCurrentItem(currentPage);

            startSliding(arrangedList);

            ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == ViewPager.SCROLL_STATE_IDLE){
                        createInfiniteLoop(arrangedList);
                    }

                }
            };

            posterViewPager.addOnPageChangeListener(listener);

            posterViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    createInfiniteLoop(arrangedList);
                    stopSliding();

                    if(event.getAction() == MotionEvent.ACTION_UP){
                        startSliding(arrangedList);
                    }

                    return false;
                }
            });

        }

        private void createInfiniteLoop(List<String> list){

            if (currentPage == list.size() - 2){
                currentPage = 2;
                posterViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1){
                currentPage = list.size() - 3;
                posterViewPager.setCurrentItem(currentPage, false);
            }

        }

        private void startSliding(List<String> list){
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if (currentPage >= list.size()){
                        currentPage = 1;
                    }
                    posterViewPager.setCurrentItem(currentPage++, true);

                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, DELAY_TIME, PERIOD_TIME);

        }

        private void stopSliding(){
            timer.cancel();
        }

    }

}
