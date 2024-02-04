package com.example.recuerdate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface apiService {

    @POST("registrarUsuari")
    Call<Resposta> EnviarUsuario(@Body UsuariLocalitzat usuariTrobat);

    @POST("registrarTutor")
    Call<Resposta> EnviarTutor(@Body TutorTrobat tutorTrobat);

    @POST("usuarisLogin")
    Call<Resposta> EnviarUsuari(@Body UsuariLocalitzat usuariTrobat);
}
