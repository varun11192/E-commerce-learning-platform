package com.varunsen.gurughantal.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.ProductDetailsActivity;

import java.util.List;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder> {

    private List<HorizontalItemModel> list;

    public HorizontalProductAdapter(List<HorizontalItemModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HorizontalProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_product_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductAdapter.ViewHolder holder, int position) {

        holder.setData(list.get(position).getProductId() ,list.get(position).getImageResource(), list.get(position).getTitle(), list.get(position).getSubtitle(), list.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleTv, subtitleTv, priceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productImageView);
            titleTv = itemView.findViewById(R.id.titleTv);
            subtitleTv = itemView.findViewById(R.id.subtitleTv);
            priceTv = itemView.findViewById(R.id.priceTV);

        }

        public void setData(String productId ,String imageResource, String title, String subtitle, String price){

        titleTv.setText(title);
        subtitleTv.setText(subtitle);

            if(!title.equals("")) {

                priceTv.setText("Rs." + price + "/-");
                Glide.with(itemView.getContext()).load(imageResource).into(imageView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //todo: to pass identification data through intent.
                        Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        intent.putExtra("product_id" ,productId);
                        itemView.getContext().startActivity(intent);

                    }
                });

            } else {

                priceTv.setText("price");


            }

        }

    }
}
