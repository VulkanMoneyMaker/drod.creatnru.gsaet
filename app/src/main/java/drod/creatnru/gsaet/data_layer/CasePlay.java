package drod.creatnru.gsaet.data_layer;


import drod.creatnru.gsaet.data_layer.usecase.UserPlayCase;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface CasePlay {

    @GET("test9")
    Call<UserPlayCase> getPlayItems();

    @POST("test9")
    Call<UserPlayCase> createPlayItems();

    @PUT("test9")
    Call<UserPlayCase> updatePlayItems();

}
