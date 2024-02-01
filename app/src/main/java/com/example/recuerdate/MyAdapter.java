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
        public Button button1, button2;

        public MyViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.user_image);
            text = v.findViewById(R.id.user_name);
            button1 = v.findViewById(R.id.button);
            button2 = v.findViewById(R.id.button6);
        }
    }

    public MyAdapter(List<Familiar> items) {
        this.items = items;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("MyAdapter", "onCreateViewHolder llamado");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.persona_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("MyAdapter", "onBindViewHolder llamado para la posición " + position);
        Familiar item = items.get(position);
        holder.image.setImageResource(item.getImagen());
        holder.text.setText(item.getNombre());
        // Aquí puedes configurar los listeners para tus botones
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
