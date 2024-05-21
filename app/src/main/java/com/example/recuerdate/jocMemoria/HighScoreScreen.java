package com.example.recuerdate.jocMemoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recuerdate.R;
import com.example.recuerdate.Settings;
import com.example.recuerdate.jocMemoria.adapter.scoreadapter.ScoreAdapter;
import com.example.recuerdate.jocMemoria.game.InfoBox;
import com.example.recuerdate.jocMemoria.model.ScoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class HighScoreScreen extends AppCompatActivity {
    ImageView backBtn, infoBtn;
    TextView highest_score_txt, highest_score_name_txt;
    RecyclerView score_view;
    InfoBox infoBox;
    ScoreAdapter scoreAdapter;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        init();
        clickBackInfo();
        try {
            connectToSocketServer();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(){
        backBtn = findViewById(R.id.back_btn);
        infoBox = new InfoBox();
        highest_score_txt = findViewById(R.id.highest_score_txt);
        highest_score_name_txt = findViewById(R.id.highest_score_name_txt);
        score_view = findViewById(R.id.score_view);
        scoreAdapter = new ScoreAdapter(new ArrayList<>());
        score_view.setLayoutManager(new GridLayoutManager(this, 1));
        score_view.setAdapter(scoreAdapter);
    }

    public void clickBackInfo(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void connectToSocketServer() throws URISyntaxException {
        socket = IO.socket(Settings.SERVER + ":" + Settings.PORT);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Conectado al servidor de Socket.IO");
            }
        });

        // Conectar al servidor
        socket.connect();
    }

    private ArrayList<ScoreModel> parseRanking(Object data) {
        ArrayList<ScoreModel> ranking = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) data;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                int score = jsonObject.getInt("score");
                ranking.add(new ScoreModel(score, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    private void updateRanking(ArrayList<ScoreModel> ranking) {
        if (!ranking.isEmpty()) {
            ScoreModel highestScore = ranking.get(0);
            highest_score_txt.setText(String.valueOf(highestScore.getScore()));
            highest_score_name_txt.setText(highestScore.getName());
            scoreAdapter.updateScores(ranking);
        }
    }
}
