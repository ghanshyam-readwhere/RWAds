package com.rwadswhitelabel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rwadswhitelabel.models.ConfigResponses;
import com.rwadswhitelabel.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RwAdsIntialize {

    public static  Context appContext;
    public static  String AdRequestOriginal = "ad_request_original";
    public static  String AdRequestReadWhere = "ad_request_rw";

    private static void getAppConfig(Context context,String rwAppId){
        Call<ConfigResponses> call = RetrofitClient.INSTANCE.getApiInterface().getAppConfig(context.getPackageName());
        call.enqueue(new Callback<ConfigResponses>() {
            @Override
            public void onResponse(Call<ConfigResponses> call, Response<ConfigResponses> response) {
                if(response.isSuccessful() && response.body()!=null && response.body().getStatus()){
                    AppConfiguration.getInstance(context).setAdsMediationListings(response.body().getData());
                    AppConfiguration.getInstance(context).setRwOverWriteValue(response.body().getRw_overwrite());
                    AppConfiguration.getInstance(context).setRequestBatch(response.body().getRequestBatch()== 1);
                    AppConfiguration.getInstance(context).setAutoRefresh(response.body().getAutoRefresh());
                    saveStringShared("config",new Gson().toJson(response.body()));
                    if(getIntegerShared("rw_overwrite_val") !=response.body().getRw_overwrite() ){
                        saveIntegerShared("rw_overwrite_val",response.body().getRw_overwrite());
                        clearSavedShared();
                    }

                }else {
                    try {
                        ConfigResponses configResponses = new Gson().fromJson(getStringShared("config"),ConfigResponses.class);
                        if(configResponses!= null){
                            AppConfiguration.getInstance(context).setAdsMediationListings(configResponses.getData());
                            AppConfiguration.getInstance(context).setRwOverWriteValue(response.body().getRw_overwrite());
                            AppConfiguration.getInstance(context).setRequestBatch(response.body().getRequestBatch()== 1);
                            AppConfiguration.getInstance(context).setAutoRefresh(response.body().getAutoRefresh());
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ConfigResponses> call, Throwable t) {
                ConfigResponses configResponses = new Gson().fromJson(getStringShared("config"),ConfigResponses.class);
                AppConfiguration.getInstance(context).setAdsMediationListings(configResponses.getData());
                AppConfiguration.getInstance(context).setRwOverWriteValue(configResponses.getRw_overwrite());
                AppConfiguration.getInstance(context).setRequestBatch(configResponses.getRequestBatch()== 1);
                AppConfiguration.getInstance(context).setAutoRefresh(configResponses.getAutoRefresh());
                t.printStackTrace();
            }
        });
    }

    public static void init(@NotNull Activity mainActivity, @NotNull String rwAppId) {
        appContext = mainActivity;
//        if(true){
//            loadJSONFromAsset(mainActivity);
//        }else {
            getAppConfig(mainActivity,rwAppId);
//        }

    }

    public static void saveStringShared( String key, String
            value) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void saveIntegerShared( String key, Integer
            value) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Integer getIntegerShared( String key) {
        SharedPreferences shared = appContext.getSharedPreferences("", Context.MODE_PRIVATE);
        return shared.getInt(key, 0);
    }
    public static void clearSavedShared (){
        Object[] savedData = getAllIntegerShared().keySet().toArray();
        for(int count =0;count<savedData.length;count++){
            if(savedData[count].toString().contains(AdRequestReadWhere) || savedData[count].toString().contains(AdRequestOriginal)){
                saveIntegerShared(savedData[count].toString(),0);
            }
        }

    }

    public static Map<String, ?> getAllIntegerShared() {
        SharedPreferences shared = appContext.getSharedPreferences("", Context.MODE_PRIVATE);
        return shared.getAll();
    }

    public static String getStringShared( String key) {
        SharedPreferences shared = appContext.getSharedPreferences("", Context.MODE_PRIVATE);
        return shared.getString(key, "");
    }
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("feed.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            ConfigResponses configResponses = new Gson().fromJson(json,ConfigResponses.class);
            AppConfiguration.getInstance(context).setAdsMediationListings(configResponses.getData());
            AppConfiguration.getInstance(context).setRwOverWriteValue(configResponses.getRw_overwrite());
            AppConfiguration.getInstance(context).setRequestBatch(configResponses.getRequestBatch() == 1);
            AppConfiguration.getInstance(context).setAutoRefresh(configResponses.getAutoRefresh());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
