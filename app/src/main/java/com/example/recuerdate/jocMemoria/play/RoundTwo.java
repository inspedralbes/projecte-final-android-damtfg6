package com.example.recuerdate.jocMemoria.play;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recuerdate.R;
import com.example.recuerdate.jocMemoria.adapter.gameadapter.CardAdapter;
import com.example.recuerdate.jocMemoria.game.InfoBox;
import com.example.recuerdate.jocMemoria.game.PopulateCard;
import com.example.recuerdate.jocMemoria.model.GameModel;


public class RoundTwo extends Fragment {

    RecyclerView recyclerView;
    GameModel gameModel;
    TextView gameScore, animScore;
    ImageView backBtn, infoBtn;
    InfoBox infoBox;

    public RoundTwo(GameModel gameModel){
        this.gameModel = gameModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_round_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.animals_card);
        gameScore = view.findViewById(R.id.game_score);
        backBtn = view.findViewById(R.id.back_btn);
        animScore = view.findViewById(R.id.anim_score);
        infoBox = new InfoBox();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        PopulateCard populateCard = new PopulateCard(6);
        CardAdapter cardAdapter = new CardAdapter(populateCard.populateCard(), getContext(), gameModel, gameScore, animScore, populateCard.getTotalAnimals(), getFragmentManager(), "Round 2");
        recyclerView.setAdapter(cardAdapter);
        gameScore.setText(String.valueOf(gameModel.getScore()));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}