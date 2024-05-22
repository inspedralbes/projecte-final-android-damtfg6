package com.example.recuerdate.jocMemoria.adapter.scoreadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
    private ArrayList<HashMap<String, Object>> scoreList;

    public ScoreAdapter(ArrayList<HashMap<String, Object>> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_card, parent, false);
        return new ScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        HashMap<String, Object> scoreData = scoreList.get(position);
        holder.getName().setText(String.valueOf(scoreData.get("dni")));
        holder.getScore().setText(String.valueOf(scoreData.get("totalScore")));
        holder.getRank().setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    // Método para actualizar los datos del adaptador
    public void updateScores(ArrayList<HashMap<String, Object>> newScores) {
        scoreList.clear();
        scoreList.addAll(newScores);
        notifyDataSetChanged();
    }
}
