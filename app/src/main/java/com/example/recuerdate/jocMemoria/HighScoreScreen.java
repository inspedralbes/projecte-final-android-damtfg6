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
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        init();
        clickBackInfo();
        connectToSocket();
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

    private void connectToSocket() {
        try {
            mSocket = IO.socket(Settings.SERVER + ":" + Settings.PORT);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Conectado al servidor
                    mSocket.emit("solicitarRanking");
                }
            }).on("actualizarRanking", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<ScoreModel> ranking = parseRanking(args[0]);
                            updateRanking(ranking);
                            System.out.println(ranking);
                        }
                    });
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Desconectado del servidor
                }
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
