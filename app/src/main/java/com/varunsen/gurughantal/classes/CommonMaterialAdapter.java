package com.varunsen.gurughantal.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.activities.LectureDisplayActivity;

import java.util.List;

public class CommonMaterialAdapter extends RecyclerView.Adapter<CommonMaterialAdapter.ViewHolder> {

    private List<String> titleList, idUrlList;
    private int materialType;

    public CommonMaterialAdapter(List<String> titleList, List<String> idUrlList, int materialType) {
        this.titleList = titleList;
        this.idUrlList = idUrlList;
        this.materialType = materialType;
    }

    @NonNull
    @Override
    public CommonMaterialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_material_course_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonMaterialAdapter.ViewHolder holder, int position) {

        holder.setData(titleList.get(position), materialType, idUrlList.get(position));

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconIv;
        private TextView titleTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconIv = itemView.findViewById(R.id.icon);
            titleTv = itemView.findViewById(R.id.lessonTitle);

        }

        private void setData(String title, int type, String idUrl){

            titleTv.setText(title);

            if (type == Variables.LECTURES){
                iconIv.setImageDrawable(itemView.getResources().getDrawable(R.drawable.courses_icon_blue));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), LectureDisplayActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("url", idUrl);
                        itemView.getContext().startActivity(intent);
                    }
                });

            } else if (type == Variables.NOTES){
                iconIv.setImageDrawable(itemView.getResources().getDrawable(R.drawable.notes_icon_blue));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(itemView.getContext(), PDFDisplayActivity.class);
//                        intent.putExtra("title", title);
//                        intent.putExtra("url", idUrl);
//                        itemView.getContext().startActivity(intent);

                    }
                });

            } else if (type == Variables.TESTS){
                iconIv.setImageDrawable(itemView.getResources().getDrawable(R.drawable.tests_icon_blue));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(itemView.getContext(), TestInstructionsActivity.class);
//                        intent.putExtra("title", title);
//                        intent.putExtra("id", idUrl);
//                        itemView.getContext().startActivity(intent);

                    }
                });

            }

        }

    }
}
