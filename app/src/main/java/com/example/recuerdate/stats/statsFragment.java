package com.example.recuerdate.stats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recuerdate.FamiliarsV2.FamiliarsV2;
import com.example.recuerdate.JocsFragment;
import com.example.recuerdate.MainActivity;
import com.example.recuerdate.MainActivityTutor;
import com.example.recuerdate.R;

import com.example.recuerdate.Settings;
import com.example.recuerdate.activities.TokenActivity;
import com.example.recuerdate.dashboard.Dashboard;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class statsFragment extends Fragment {

    private MainActivity mainActivity;
    private MainActivityTutor mainActivityTutor;
    Context context;

    View rootView;

    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String role = preferenceManager.getString(Constants.KEY_ROLE);
        String dni = preferenceManager.getString(Constants.KEY_EMAIL);


        if (role.equals("Tutor")) {
            rootView = inflater.inflate(R.layout.layout_stats_tutor, container, false);
        } else if (role.equals("Usuari")) {
            rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        }


        if (role.equals("Tutor")) {
            dni = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_DNI);
        } else if (role.equals("Usuari")) {
            dni = preferenceManager.getString(Constants.KEY_EMAIL);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Settings.SERVER+ ":" + Settings.PORT + "/getStatsUsuari?dni=" + dni)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Bitmap decodedByte = null, decodedByte2 = null, decodedByte3 = null;
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        JSONObject json;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            String imageBase64 = json.getString("imagen_mejor_resultado");
                            String imageBase642 = json.getString("imagen_falladas_ronda1");
                            String imageBase643 = json.getString("imagen_falladas_ronda2");
                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                            byte[] decodedString2 = Base64.decode(imageBase642, Base64.DEFAULT);
                            byte[] decodedString3 = Base64.decode(imageBase643, Base64.DEFAULT);
                            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                            decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);

                            // Aquí no actualizamos las vistas, solo decodificamos las imágenes
                        }

                        // Actualizamos las vistas fuera del bucle for
                        Bitmap finalDecodedByte = decodedByte;
                        Bitmap finalDecodedByte1 = decodedByte2;
                        Bitmap finalDecodedByte2 = decodedByte3;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imageView = getView().findViewById(R.id.ImageViewProgres);
                                if (finalDecodedByte != null) {
                                    imageView.setImageBitmap(finalDecodedByte);
                                }
                                ImageView imageView2 = getView().findViewById(R.id.ImageViewRonda1);
                                if (finalDecodedByte1 != null) {
                                    imageView2.setImageBitmap(finalDecodedByte1);
                                }
                                ImageView imageView3 = getView().findViewById(R.id.ImageViewRonda2);
                                if (finalDecodedByte2 != null) {
                                    imageView3.setImageBitmap(finalDecodedByte2);
                                }
                                TextView textView = getView().findViewById(R.id.textViewProgres);
                                textView.setVisibility(View.GONE);
                                if (role.equals("Tutor")) {
                                    // Código para el rol de Tutor
                                } else if (role.equals("Usuari")) {
                                    Button button = getView().findViewById(R.id.buttonObrirJoc);
                                    button.setVisibility(View.GONE);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Si no recibes datos, puedes mostrar el texto y el botón aquí
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = getView().findViewById(R.id.textViewProgres);
                            textView.setVisibility(View.VISIBLE);
                            Button button = getView().findViewById(R.id.buttonObrirJoc);
                            button.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

        });


        Button button = (Button) rootView.findViewById(R.id.buttonObrirJoc);


        if (role.equals("Tutor")) {
        } else if (role.equals("Usuari")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.replaceFragment(new JocsFragment());
                }
            });
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            mainActivityTutor = (MainActivityTutor) context;
        }
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
    }
}
