package com.example.recuerdate.jocMemoria.adapter.scoreadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;
import com.example.recuerdate.jocMemoria.model.ScoreModel;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
    private ArrayList<ScoreModel> scoreModels;

    public ScoreAdapter(ArrayList<ScoreModel> scoreModels){
        this.scoreModels = scoreModels;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_card, parent, false);
        return new ScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        ScoreModel scoreModel = scoreModels.get(position);
        holder.getName().setText(scoreModel.getName());
        holder.getScore().setText(String.valueOf(scoreModel.getScore()));
        holder.getRank().setText(String.valueOf(position + 1)); // Cambiado de +2 a +1
    }

    @Override
    public int getItemCount() {
        return scoreModels.size();
    }

    // Método para actualizar los datos del adaptador
    public void updateScores(ArrayList<ScoreModel> newScores) {
        scoreModels.clear();
        scoreModels.addAll(newScores);
        notifyDataSetChanged();
    }
}
