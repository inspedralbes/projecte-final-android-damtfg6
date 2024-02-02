package com.example.recuerdate;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Familiar> items;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;
        public Button button1, button2, editButton;

        public MyViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.user_image);
            text = v.findViewById(R.id.user_name);
            button1 = v.findViewById(R.id.button);
            button2 = v.findViewById(R.id.button6);
            editButton = v.findViewById(R.id.edit_button);
        }

        public void bind(Familiar item) {
            image.setImageResource(item.getImagen());
            text.setText("");
        }
    }

    public MyAdapter(List<Familiar> items) {
        this.items = items;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.persona_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Familiar item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}