package com.rwadswhitelabel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rwadswhitelabel.models.ConfigResponses;
import com.rwadswhitelabel.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RwAdsIntialize {

    private static void getAppConfig(Context context,String rwAppId){
        Call<ConfigResponses> call = RetrofitClient.INSTANCE.getApiInterface().getAppConfig();
        call.enqueue(new Callback<ConfigResponses>() {
            @Override
            public void onResponse(Call<ConfigResponses> call, Response<ConfigResponses> response) {
                if(response.isSuccessful() && response.body()!=null && response.body().getStatus()){
                    AppConfiguration.getInstance(context).setAdsMediationListings(response.body().getData());
                    saveStringShared(context,"config",new Gson().toJson(response.body()));
                }else {
                    ConfigResponses configResponses = new Gson().fromJson(getStringShared(context,"config"),ConfigResponses.class);
                    AppConfiguration.getInstance(context).setAdsMediationListings(configResponses.getData());
                }
            }
            @Override
            public void onFailure(Call<ConfigResponses> call, Throwable t) {
                ConfigResponses configResponses = new Gson().fromJson(getStringShared(context,"config"),ConfigResponses.class);
                AppConfiguration.getInstance(context).setAdsMediationListings(configResponses.getData());
                t.printStackTrace();
            }
        });
    }

    public static void init(@NotNull Activity mainActivity, @NotNull String rwAppId) {
        getAppConfig(mainActivity,rwAppId);
    }

    private static void saveStringShared(Context context, String key, String
            value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStringShared(Context context, String key) {
        SharedPreferences shared = context.getSharedPreferences("", Context.MODE_PRIVATE);
        return shared.getString(key, "");
    }

}
