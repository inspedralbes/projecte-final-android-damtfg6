package com.example.recuerdate.familiars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;

import java.util.ArrayList;

public class ModelRecyclerView extends RecyclerView.Adapter<ModelRecyclerView.ViewHolder> {


    Context context;

    ArrayList<Model> arrayList = new ArrayList<>();

    public ModelRecyclerView(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el modelo en la posición actual
        Model model = arrayList.get(position);

        // Obtener la imagen del modelo
        ImageView imageView = model.getImatge();

        // Establecer la imagen en el ImageView del ViewHolder
        holder.imageView.setImageDrawable(imageView.getDrawable());

        // Establecer el nombre de usuario y el número de teléfono en los TextViews del ViewHolder
        holder.nomUsuari.setText(model.getNomUsuari());
        holder.numeroTlf.setText(model.getNumeroTlf());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nomUsuari, numeroTlf;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageViewFamiliar);
            nomUsuari = itemView.findViewById(R.id.textViewFamiliarNom);
            numeroTlf = itemView.findViewById(R.id.textViewFamiliarTelefon);

            animation(itemView);
        }
    }

    public void animation(View view){
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);
    }
}

