package com.varunsen.gurughantal.classes;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.CourseSubjectsActivity;
import com.varunsen.gurughantal.activities.NotesDisplayActivity;
import com.varunsen.gurughantal.activities.TestSetsActivity;

import java.util.List;

public class CommonPurchasedAdapter extends RecyclerView.Adapter<CommonPurchasedAdapter.ViewHolder>  {

    List<CommonPurchasedModel> list;

    public CommonPurchasedAdapter(List<CommonPurchasedModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommonPurchasedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_purchased_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonPurchasedAdapter.ViewHolder holder, int position) {
        Log.d("abbb", "" + list.get(position).getProductType());
        holder.setData(list.get(position).getProductTitle(), list.get(position).getProductSubtitle(), list.get(position).getProductImage(), list
        .get(position).getProductId(), list.get(position).getProductType());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv, subtitleTv;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleTv);
            subtitleTv = itemView.findViewById(R.id.subtitleTv);
            imageView = itemView.findViewById(R.id.productImageView);

        }

        private void setData(String title, String subtitle, String imageResource, String productId, int productType){

            Log.d("adaaaa", "title:  " + title);

            titleTv.setText(title);
            subtitleTv.setText(subtitle);
            Glide.with(itemView.getContext()).load(imageResource).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (productType == Variables.TYPE_COURSE){

                        Intent courseIntent = new Intent(itemView.getContext(), CourseSubjectsActivity.class);
                        courseIntent.putExtra("product_id", productId);
                        courseIntent.putExtra("product_type", productType);
                        courseIntent.putExtra("product_title", title);
                        itemView.getContext().startActivity(courseIntent);

                    } else if (productType == Variables.TYPE_NOTES){

                        Intent courseIntent = new Intent(itemView.getContext(), NotesDisplayActivity.class);
                        courseIntent.putExtra("product_id", productId);
                        courseIntent.putExtra("product_type", productType);
                        courseIntent.putExtra("product_title", title);
                        itemView.getContext().startActivity(courseIntent);

                    } else if (productType == Variables.TYPE_TESTS){

                        Intent courseIntent = new Intent(itemView.getContext(), TestSetsActivity.class);
                        courseIntent.putExtra("product_id", productId);
                        courseIntent.putExtra("product_type", productType);
                        courseIntent.putExtra("product_title", title);
                        itemView.getContext().startActivity(courseIntent);

                    } else {
                        Toast.makeText(itemView.getContext(), "Invalid Product ID, Please Contact Support.", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }
}
