package com.varunsen.gurughantal.classes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.CourseMaterialActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<String> list;
    private String productId;

    public SubjectsAdapter(List<String> list, String productId) {
        this.list = list;
        this.productId = productId;
    }

    @NonNull
    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsAdapter.ViewHolder holder, int position) {

        List<String> colorList = new ArrayList<>();
        colorList.add("#3EB489");
        colorList.add("#FF80AB");
        colorList.add("#B2FF59");
        colorList.add("#FFEB3B");
        colorList.add("#7F39FB");
        colorList.add("#03DAC6");
        colorList.add("#00E676");
        colorList.add("#FFBB86");

        Random randomizer = new Random();
        String bgColor = colorList.get(randomizer.nextInt(colorList.size()));

        holder.setData(list.get(position), bgColor);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;
        private ConstraintLayout subjectConstraLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.subject_title_tv);
            subjectConstraLayout = itemView.findViewById(R.id.subjectsContrLayout);

        }

        private void setData(final String title, final String subjectsLAyoutBg){

            titleText.setText(title);
            subjectConstraLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(subjectsLAyoutBg)));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent courseMaterialIntent = new Intent(itemView.getContext(), CourseMaterialActivity.class);
                    courseMaterialIntent.putExtra("subject_name", title);
                    courseMaterialIntent.putExtra("product_id", productId);
                    itemView.getContext().startActivity(courseMaterialIntent);


                }
            });

        }
    }
}
