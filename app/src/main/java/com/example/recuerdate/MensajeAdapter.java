package com.example.recuerdate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {
    private List<Mensaje> mensajes;

    public MensajeAdapter(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    @Override
    public MensajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_item, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajeViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.remitenteTextView.setText(mensaje.getNomCognoms());
        holder.contenidoTextView.setText(mensaje.getMessage());
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class MensajeViewHolder extends RecyclerView.ViewHolder {
        public TextView remitenteTextView;
        public TextView contenidoTextView;

        public MensajeViewHolder(View itemView) {
            super(itemView);
            remitenteTextView = itemView.findViewById(R.id.remitente);
            contenidoTextView = itemView.findViewById(R.id.contenido);
        }
    }
}
