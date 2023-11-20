package com.rwadswhitelabel;


import static com.rwadswhitelabel.RwAdsIntialize.AdRequestOriginal;
import static com.rwadswhitelabel.RwAdsIntialize.AdRequestReadWhere;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public abstract class InterstitialAd extends com.google.android.gms.ads.interstitial.InterstitialAd {
    private static com.google.android.gms.ads.interstitial.InterstitialAd interstitial;
    private static ArrayList<String> idsListing = new ArrayList();
    private static boolean bulkLoad = false;
    private static int loadedCount =0;
    private static HashMap<String, com.google.android.gms.ads.interstitial.InterstitialAd> loadedAds = new HashMap<String, com.google.android.gms.ads.interstitial.InterstitialAd>();
    private static boolean isLoaded = false;
    private static Context lastContext;
    private static String lastAdUnitId;
    private static AdRequest lastAdRequest;
    private static InterstitialAdLoadCallback lastAloadCallback;

    @Nullable
    public abstract FullScreenContentCallback getFullScreenContentCallback();

    @Nullable
    public abstract OnPaidEventListener getOnPaidEventListener();

    @NonNull
    public abstract ResponseInfo getResponseInfo();

    @NonNull
    public abstract String getAdUnitId();

    public InterstitialAd() {
    }

    public static void load(@NonNull Context context, @NonNull String adUnitId, @NonNull AdRequest adRequest, @NonNull InterstitialAdLoadCallback loadCallback) {
        lastContext = context;
        lastAdUnitId = adUnitId;
        lastAdRequest = adRequest;
        lastAloadCallback = loadCallback;
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            idsListing = new ArrayList<>();
            idsListing.addAll(appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits());
            bulkLoad = appConfiguration.getRequestBatch();
        }
        if(idsListing!=null && idsListing.size() > 0){
            if(bulkLoad){
                loadedCount = 0;
                loadedAds = new HashMap<>();
                isLoaded = false;
                for(int count =0;count<idsListing.size();count++){
                    bulkAdLoadMediation(context,idsListing.get(count),adRequest,loadCallback,idsListing.size());
                }
            }else {
                String originalId = idsListing.get(0);
                int percent = AppConfiguration.getInstance(context).getRw_overwrite();

                int rwRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestReadWhere);
                int originalRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestOriginal);
                if(percent == 0){
                    rwRequest = 0;
                    originalRequest = 0;
                }else {
                    if((rwRequest+originalRequest) != 0 &&rwRequest * 100/(rwRequest+originalRequest) < percent){
                        Collections.rotate(idsListing,-1);
                    }
                }
                setMediationFlow(context, idsListing.get(0), adRequest, loadCallback,rwRequest,originalRequest,originalId);
            }
        }else {
            setMediationFlow(context,adUnitId,adRequest,loadCallback,0,0,adUnitId);
        }
    }

    private static void bulkAdLoadMediation(Context context, String adUnitId, AdRequest adRequest, InterstitialAdLoadCallback loadCallback, int totalCount){
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, adUnitId, adRequest, new com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
                loadedCount++;
                if(loadedCount == totalCount) {
                    loadedCallback(loadCallback);
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.d("TAG", "onAdFailedToLoad:false "+ adUnitId);
                loadedCount++;
                interstitial = interstitialAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        interstitial = null;
                        load(context,adUnitId,adRequest,loadCallback);
                    }
                });
                loadedAds.put(adUnitId,interstitial);
                if(loadedCount == totalCount) {
                    loadedCallback(loadCallback);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadedCallback(loadCallback);
                    }
                },2500);
            }
        });
    }

    public static void loadedCallback(InterstitialAdLoadCallback loadCallback) {
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        loadCallback.onAdLoaded(new InterstitialAd() {
            @Nullable
            @Override
            public FullScreenContentCallback getFullScreenContentCallback() {
                return null;
            }

            @Nullable
            @Override
            public OnPaidEventListener getOnPaidEventListener() {
                return null;
            }

            @NonNull
            @Override
            public ResponseInfo getResponseInfo() {
                return null;
            }

            @NonNull
            @Override
            public String getAdUnitId() {
                return null;
            }

            @Override
            public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

            }

            @Override
            public void setImmersiveMode(boolean b) {

            }

            @Override
            public void setOnPaidEventListener(@Nullable OnPaidEventListener onPaidEventListener) {

            }

        });
    }
    private static void setMediationFlow(Context context, String adUnitId, AdRequest adRequest, InterstitialAdLoadCallback loadCallback, Integer rwRequest, Integer originalRequest, String originalAdUnit){
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, adUnitId, adRequest, new com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
//                loadCallback.onAdFailedToLoad(loadAdError);
                for(int count =0;count<idsListing.size();count++){
                    if(idsListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < idsListing.size()){
                        setMediationFlow(context,idsListing.get(count+1),adRequest,loadCallback,rwRequest,originalRequest,originalAdUnit);
                    }
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                if(idsListing!=null && idsListing.size() > 0) {
                    if (originalAdUnit.equalsIgnoreCase(adUnitId)) {
                        Integer originalVal = originalRequest;
                        RwAdsIntialize.saveIntegerShared(originalAdUnit + "_" + AdRequestOriginal, ++originalVal);
                    } else {
                        Integer rwVal = rwRequest;
                        RwAdsIntialize.saveIntegerShared(originalAdUnit + "_" + AdRequestReadWhere, ++rwVal);
                    }
                }
                interstitial = interstitialAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                            interstitial = null;
                            load(context,adUnitId,adRequest,loadCallback);
                    }
                });
                loadCallback.onAdLoaded(new InterstitialAd() {
                    @Nullable
                    @Override
                    public FullScreenContentCallback getFullScreenContentCallback() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OnPaidEventListener getOnPaidEventListener() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public ResponseInfo getResponseInfo() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public String getAdUnitId() {
                        return null;
                    }

                    @Override
                    public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

                    }

                    @Override
                    public void setImmersiveMode(boolean b) {

                    }

                    @Override
                    public void setOnPaidEventListener(@Nullable OnPaidEventListener onPaidEventListener) {

                    }

                });
            }
        });
    }

    private com.google.android.gms.ads.interstitial.InterstitialAd checkOverWriteAdUnitId(Activity context){
        String originalId = idsListing.get(0);
        int percent = AppConfiguration.getInstance(context).getRw_overwrite();
        int rwRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestReadWhere);
        int originalRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestOriginal);

        if(percent != 0 && (rwRequest+originalRequest) != 0 &&rwRequest * 100/(rwRequest+originalRequest) < percent){
            Collections.rotate(idsListing,-1);
        }
        for(int count =0;count<idsListing.size();count++) {
            if (loadedAds.containsKey(idsListing.get(count))) {
                if(idsListing.get(count).equalsIgnoreCase(originalId)){
                    RwAdsIntialize.saveIntegerShared(originalId+"_"+AdRequestOriginal,++originalRequest);
                }else {
                    RwAdsIntialize.saveIntegerShared(originalId+"_"+AdRequestReadWhere,++rwRequest);
                }
                return loadedAds.get(idsListing.get(count));
            }
        }
        return  null;
    }

    public void show(Activity activity){
        if(idsListing!=null && idsListing.size() > 0 && bulkLoad ){
            if ( loadedAds.size() == 0) {
                load(lastContext,lastAdUnitId,lastAdRequest,lastAloadCallback);
                return;
            }
            com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd = checkOverWriteAdUnitId(activity);
            if(interstitialAd!=null){
                interstitialAd.show(activity);
                loadedAds.clear();
            }else {
                load(lastContext,lastAdUnitId,lastAdRequest,lastAloadCallback);
            }

        }
//        if(bulkLoad ){
//            for(int count =0;count<idsListing.size();count++){
//                if(loadedAds.containsKey(idsListing.get(count))){
//                    loadedAds.get(idsListing.get(count)).show(activity);
//                    break;
//                }
//            }
//        }
        else {
            if(interstitial!=null){
                interstitial.show(activity);
            }else {
                load(lastContext,lastAdUnitId,lastAdRequest,lastAloadCallback);
            }
        }

    }

    public abstract void setFullScreenContentCallback(@Nullable FullScreenContentCallback var1);

    public abstract void setImmersiveMode(boolean var1);

    public abstract void setOnPaidEventListener(@Nullable OnPaidEventListener var1);


}
