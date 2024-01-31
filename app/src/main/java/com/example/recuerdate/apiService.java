package com.example.recuerdate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class apiService {

    @POST("registrarUsuari")
    Call<Resposta> EnviarUsuario(@Body UsuariLocalitzat usuariTrobat);
}
