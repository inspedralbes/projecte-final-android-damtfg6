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
import com.google.gson.Gson;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.IOException;
import java.util.ArrayList;
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
    int acertadesRnd1;
    int falladesRnd1;

    int acertadesRnd2;
    int falladesRnd2;

    int acertades;
    int fallades;

    private okhttp3.OkHttpClient client;
    private Gson gson;


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

                                nomRonda = "Nivell 1";
                                acertadesRnd1 = acertades;
                                falladesRnd1 = fallades;

                                System.out.println("Fallades: "+ falladesRnd1);
                                System.out.println("Acertades: " +acertadesRnd1);


                                // Obtener el DNI del usuario
                                SessionManagment sessionManagment = new SessionManagment(context);
                                String dniUsuario;
                                if (sessionManagment.getRol().equals("tutor")) {
                                    dniUsuario = sessionManagment.getUsuariTutoritzatData().getDni();
                                } else {
                                    dniUsuario = sessionManagment.getUserData().getDni();
                                }

                                // Crear un mapa para almacenar los datos
                                Map<String, Object> dataMap = new LinkedHashMap<>();
                                dataMap.put("nomRonda", nomRonda);
                                dataMap.put("acertadesRnd1", acertadesRnd1);
                                dataMap.put("falladesRnd1", falladesRnd1);
                                dataMap.put("dni", dniUsuario);

                                String json = gson.toJson(dataMap);

                                // Crear el cuerpo de la petición
                                RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
                                Request request = new Request.Builder()
                                        .url(Settings.SERVER+ ":" + Settings.PORT + "/stats")
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
                                            System.out.println("Coroné, en el aquaPark");
                                        }
                                    }
                                });

                        } else if(fragment_round_num.equals("Round 2")){
                                //InfoBox infoBox = new InfoBox();
                                //infoBox.addNameScore(context, String.valueOf(gameModel.getScore()));
                                fragment.beginTransaction().replace(R.id.fragment_container, new CongratsScreen(gameModel, "Round 2")).commit();

                                // ----------------------------- Guardar stats de la partida -------------------------------------

                                nomRonda = "Nivell 2";
                                acertadesRnd2 = acertades;
                                falladesRnd2 = fallades;
                                int totalScore = (gameModel.getScore());
                                System.out.println("ScoreTotal: " +totalScore);

                                System.out.println("Fallades: "+ falladesRnd2);
                                System.out.println("Acertades: " +acertadesRnd2);

                                // Obtener el DNI del usuario
                                SessionManagment sessionManagment = new SessionManagment(context);
                                String dniUsuario;
                                if (sessionManagment.getRol().equals("tutor")) {
                                    dniUsuario = sessionManagment.getUsuariTutoritzatData().getDni();
                                } else {
                                    dniUsuario = sessionManagment.getUserData().getDni();
                                }

                                // Crear un mapa para almacenar los datos
                                Map<String, Object> dataMap = new LinkedHashMap<>();
                                dataMap.put("nomRonda", nomRonda);
                                dataMap.put("acertadesRnd1", acertadesRnd2);
                                dataMap.put("falladesRnd1", falladesRnd2);
                                dataMap.put("totalScore", totalScore);
                                dataMap.put("dni", dniUsuario);

                                String json = gson.toJson(dataMap);

                                // Crear el cuerpo de la petición
                                RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
                                Request request = new Request.Builder()
                                        .url(Settings.SERVER+ ":" + Settings.PORT + "/stats")
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
                                            System.out.println("Coroné, en el aquaPark");
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
