package com.example.recuerdate.FamiliarsV2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private List<SubItem> subItemList;
    private Context context;

    SubItemAdapter(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        SubItem subItem = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(subItem.getSubItemTitle());
        subItemViewHolder.tvSubItemDescription.setText(subItem.getSubItemDesc());
        Bitmap bitmap = subItem.getSubItemImage(context);
        if (bitmap != null) {
            subItemViewHolder.ivSubItemImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        TextView tvSubItemDescription;
        ImageView ivSubItemImage; // ImageView field

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            tvSubItemDescription = itemView.findViewById(R.id.tv_sub_item_description);
            ivSubItemImage = itemView.findViewById(R.id.img_sub_item); // ImageView assignment
        }
    }
}
