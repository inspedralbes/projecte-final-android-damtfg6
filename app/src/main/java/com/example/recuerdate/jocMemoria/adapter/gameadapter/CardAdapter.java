package com.example.recuerdate.jocMemoria.adapter.gameadapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;
import com.example.recuerdate.SessionManagment;
import com.example.recuerdate.Settings;
import com.example.recuerdate.jocMemoria.game.InfoBox;
import com.example.recuerdate.jocMemoria.game.ScoreAnimation;
import com.example.recuerdate.jocMemoria.model.CardModel;
import com.example.recuerdate.jocMemoria.model.GameModel;
import com.example.recuerdate.jocMemoria.play.CongratsScreen;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.gson.Gson;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CardAdapter extends RecyclerView.Adapter<CardHolder> {
    private ArrayList<CardModel> mData;
    private ArrayList<EasyFlipView> flipCards;
    private ArrayList<String> names;
    GameModel gameModel;
    Context context;
    TextView gameScore, animScore;
    int totalCard;
    String fragment_round_num;
    FragmentManager fragment;
    ScoreAnimation scoreAnimation;
    String nomRonda;
    int acertadesRnd1; // MODIFICADO
    int falladesRnd1; // MODIFICADO
    int acertadesRnd2;
    int falladesRnd2;
    int acertades;
    int fallades;
    private okhttp3.OkHttpClient client;
    private Gson gson;

    private PreferenceManager preferenceManager;
    public CardAdapter(ArrayList<CardModel> mData, Context context, GameModel gameModel, TextView gameScore, TextView animScore, int totalCard, FragmentManager fragment, String fragment_round_num){
        this.mData = mData;
        this.context = context;
        this.gameModel = gameModel;
        this.gameScore = gameScore;
        this.animScore = animScore;
        this.totalCard = totalCard;
        this.fragment_round_num = fragment_round_num;
        this.fragment = fragment;
        flipCards = new ArrayList<>();
        names = new ArrayList<>();
        scoreAnimation = new ScoreAnimation();
        this.gson = new Gson();
        this.client = new OkHttpClient();
        this.preferenceManager = new PreferenceManager(context);
    }
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new CardHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        CardModel model = mData.get(position);
        holder.getBackImage().setImageResource(model.getBack_img());
        holder.getFrontImage().setImageResource(model.getFront_img());
        Handler handler = new Handler();
        gameLogic(holder.getEasyFlipView(), handler, model, scoreAnimation);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    private void enviarDatos(String endpoint, Map<String, Object> dataMap) {
        String json = gson.toJson(dataMap);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Settings.SERVER + ":" + Settings.PORT + endpoint)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // Aquí puedes manejar la respuesta del servidor
                    System.out.println("Respuesta recibida del servidor para el endpoint " + endpoint + ": " + response.body().string());
                }
            }
        });
    }

    public void gameLogic(EasyFlipView flipView, Handler handler, CardModel model, ScoreAnimation scoreAnimation){
        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                if(easyFlipView.isBackSide()){
                    flipCards.add(easyFlipView);
                    names.add(model.getName());
                    easyFlipView.setFlipOnTouch(false);
                } else {
                    easyFlipView.setFlipOnTouch(true);
                }

                if (flipCards.size() == 2) {
                    if(names.get(0).equals(names.get(1))){
                        totalCard--;
                        scoreAnimation.animationScore(animScore, "+10");
                        acertades++;
                        gameModel.setScore(+10);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (EasyFlipView view : flipCards) {
                                    view.setFlipEnabled(false);
                                }
                                flipCards.clear();
                                names.clear();
                            }
                        }, 200);
                    } else {
                        scoreAnimation.animationScore(animScore, "-5");
                        gameModel.setScore(-5);
                        fallades++;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (EasyFlipView view : flipCards) {
                                    view.flipTheView();
                                }
                                flipCards.clear();
                                names.clear();
                            }
                        }, 200);
                    }
                }
                scoreAnimation.delaySetText(gameScore, String.valueOf(gameModel.getScore()));
                //gameScore.setText(String.valueOf(gameModel.getScore()));

                if(totalCard == 0){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(fragment_round_num.equals("Round 1")){
                                fragment.beginTransaction().replace(R.id.fragment_container, new CongratsScreen(gameModel, "Round 1")).commit();

                                // Almacena los datos de la primera ronda en GameModel
                                gameModel.setNomRonda1("Nivell 1");
                                gameModel.setAcertadesRnd1(acertades);
                                gameModel.setFalladesRnd1(fallades);

                                // No se envían los datos aquí, se almacenan para enviarlos después de la segunda ronda
                            } else if(fragment_round_num.equals("Round 2")){
                                fragment.beginTransaction().replace(R.id.fragment_container, new CongratsScreen(gameModel, "Round 2")).commit();

                                // Obtener el DNI del usuario
                                String dniUsuario = preferenceManager.getString(Constants.KEY_EMAIL);

                                // Datos de la segunda ronda
                                gameModel.setNomRonda2("Nivell 2");
                                gameModel.setAcertadesRnd2(acertades);
                                gameModel.setFalladesRnd2(fallades);
                                gameModel.setTotalScore(gameModel.getScore());

                                // Crear un mapa para almacenar los datos de ambas rondas
                                Map<String, Object> dataMap = new LinkedHashMap<>();
                                dataMap.put("dni", dniUsuario);

                                // Agregar la marca de tiempo
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                dataMap.put("fecha", dateFormat.format(date));

                                // Datos de la ronda 1
                                Map<String, Object> ronda1 = new LinkedHashMap<>();
                                ronda1.put("nomRonda", gameModel.getNomRonda1());
                                ronda1.put("acertades", gameModel.getAcertadesRnd1());
                                ronda1.put("fallades", gameModel.getFalladesRnd1());
                                dataMap.put("ronda1", ronda1);

                                // Datos de la ronda 2
                                Map<String, Object> ronda2 = new LinkedHashMap<>();
                                ronda2.put("nomRonda", gameModel.getNomRonda2());
                                ronda2.put("acertades", gameModel.getAcertadesRnd2());
                                ronda2.put("fallades", gameModel.getFalladesRnd2());
                                dataMap.put("ronda2", ronda2);

                                dataMap.put("totalScore", gameModel.getTotalScore());
                                // Enviar datos a /stats
                                enviarDatos("/stats", dataMap);

                                // Preparar datos para enviar a /ranking
                                Map<String, Object> rankingDataMap = new LinkedHashMap<>();
                                rankingDataMap.put("dni", dniUsuario);
                                rankingDataMap.put("totalScore", gameModel.getTotalScore());
                                String rankingJson = gson.toJson(rankingDataMap);

                                // Crear el cuerpo de la petición para /ranking
                                RequestBody rankingBody = RequestBody.create(rankingJson, MediaType.parse("application/json; charset=utf-8"));

                                // Construir la petición POST para /ranking
                                Request rankingRequest = new Request.Builder()
                                        .url(Settings.SERVER + ":" + Settings.PORT + "/ranking")
                                        .post(rankingBody)
                                        .build();

                                // Enviar la petición a /ranking
                                client.newCall(rankingRequest).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (!response.isSuccessful()) {
                                            throw new IOException("Unexpected code " + response);
                                        } else {
                                            // Aquí puedes manejar la respuesta del servidor
                                            System.out.println("Datos enviados a /ranking: " + response.body().string());
                                        }
                                    }
                                });
                            }
                        }
                    }, 800);
                }
            }
        });
    }
}
