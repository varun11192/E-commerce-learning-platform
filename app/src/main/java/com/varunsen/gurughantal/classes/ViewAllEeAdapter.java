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
import com.varunsen.gurughantal.activities.ViewAllActivity;

import org.w3c.dom.Text;

import java.util.List;

public class ViewAllEeAdapter extends RecyclerView.Adapter<ViewAllEeAdapter.ViewHolder> {

    private List<ViewAllEeModel> viewAllList;

    public ViewAllEeAdapter(List<ViewAllEeModel> viewAllList){this.viewAllList = viewAllList;}

    @NonNull
    @Override
    public ViewAllEeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllEeAdapter.ViewHolder holder, int position) {

        holder.setData(viewAllList.get(position).getProductId() ,viewAllList.get(position).getImageResource(), viewAllList.get(position).getLecNo(), viewAllList.get(position).getWatch(), viewAllList.get(position).getPrice(), viewAllList.get(position).getOldPrice());

    }

    @Override
    public int getItemCount() {
        return viewAllList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView lecNoTv, watchTv, priceTv, oldPriceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.lectureImage);
            lecNoTv = itemView.findViewById(R.id.lecNumber);
            watchTv = itemView.findViewById(R.id.watchNow);
            priceTv = itemView.findViewById(R.id.price);
            oldPriceTv = itemView.findViewById(R.id.prodOldPrice);

        }

        public void setData(String productId ,String imageResource, String lecNumber, String watchNow, String priceReal, String oldPriceNakli) {

            lecNoTv.setText(lecNumber);
            watchTv.setText(watchNow);
            priceTv.setText("Rs. " + priceReal + "/-");
            oldPriceTv.setText("Rs. " + oldPriceNakli + "/-");
            Glide.with(itemView.getContext()).load(imageResource).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("product_id", productId);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }
}
