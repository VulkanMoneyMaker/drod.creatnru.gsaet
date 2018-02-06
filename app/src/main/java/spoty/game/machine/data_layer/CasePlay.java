package spoty.game.machine.data_layer;


import spoty.game.machine.data_layer.usecase.UserPlayCase;
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
