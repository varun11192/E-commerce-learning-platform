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
import com.varunsen.gurughantal.activities.CategoriesActivity;

import org.w3c.dom.Text;

import java.util.List;

public class CategoriesAdapter  extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<CategoriesModel> list;

    public CategoriesAdapter(List<CategoriesModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_layout, parent, false);

        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {

        holder.setData(list.get(position).getCategoryTitle(), list.get(position).getImageAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.categoriesImageIcon);
            title = itemView.findViewById(R.id.categoryTitleTv);

        }

        public void setData(String name, String imageResource){

        title.setText(name);

            if(!name.equals("")){

                Glide.with(itemView.getContext()).load(imageResource).into(image);

                if(!(getAdapterPosition() == 0)){
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(itemView.getContext(), CategoriesActivity.class);
                            intent.putExtra("category_name", name);
                            itemView.getContext().startActivity(intent);
                        }
                    });
                }

            }




        }

    }
}
