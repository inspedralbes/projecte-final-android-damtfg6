package com.example.recuerdate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface apiService {

    @POST("registrarUsuari")
    Call<Resposta> EnviarUsuario(@Body UsuariLocalitzat usuariTrobat);

    @POST("registrarTutor")
    Call<Resposta> EnviarTutor(@Body TutorTrobat tutorTrobat);

    @POST("usuarisLogin")
    Call<RespostaLogin> EnviarUsuari(@Body UsuariLocalitzat usuariTrobat);

    @POST("registrarTutoritzacio")
    Call<RespostaTutoritzacio> EnviarTutoritzacio(@Query("familiarID") int familiarID, @Query("identificador") int identificador);

    @POST("enviarMensaje")
    Call<Resposta> EnviarMensaje(@Body Mensaje mensaje);
}
