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
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AppEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class AdManagerInterstitialAd extends com.google.android.gms.ads.admanager.AdManagerInterstitialAd {

    private static com.google.android.gms.ads.admanager.AdManagerInterstitialAd adManagerInterstitial;
    private static ArrayList<String> idsListing = new ArrayList();
    private static boolean bulkLoad = false;
    private static int loadedCount =0;
    private static HashMap<String, com.google.android.gms.ads.admanager.AdManagerInterstitialAd> loadedAds = new HashMap<String, com.google.android.gms.ads.admanager.AdManagerInterstitialAd>();
    private static boolean isLoaded = false;

    @Nullable
    public abstract AppEventListener getAppEventListener();

    public AdManagerInterstitialAd() {

    }

    public static void load(@NonNull Context context, @NonNull String adUnitId, @NonNull AdManagerAdRequest adManagerAdRequest, @NonNull AdManagerInterstitialAdLoadCallback loadCallback) {
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
                    bulkAdLoadMediation(context,idsListing.get(count),adManagerAdRequest,loadCallback,idsListing.size());
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
                    if ((rwRequest + originalRequest) != 0 && rwRequest * 100 / (rwRequest + originalRequest) < percent) {
                        Collections.rotate(idsListing, -1);
                    }
                }

                setMediationFlow(context,idsListing.get(0),adManagerAdRequest,loadCallback,rwRequest,originalRequest,originalId);
            }
        }else {
            setMediationFlow(context,adUnitId,adManagerAdRequest,loadCallback,0,0,adUnitId);
        }
    }

    private static void bulkAdLoadMediation(Context context, String adUnitId, AdManagerAdRequest adManagerAdRequest, AdManagerInterstitialAdLoadCallback loadCallback,int totalCount){
        com.google.android.gms.ads.admanager.AdManagerInterstitialAd.load(context, adUnitId, adManagerAdRequest, new com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                loadedCount++;
                if(loadedCount == totalCount) {
                    loadedCallback(loadCallback);
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.admanager.AdManagerInterstitialAd adManagerInterstitialAd) {
                super.onAdLoaded(adManagerInterstitialAd);
                loadedCount++;
                adManagerInterstitial = adManagerInterstitialAd;
                loadedAds.put(adUnitId,adManagerInterstitialAd);
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

    public static void loadedCallback(AdManagerInterstitialAdLoadCallback loadCallback){
        if(isLoaded){
            return;
        }
        isLoaded = true;
        loadCallback.onAdLoaded(new AdManagerInterstitialAd() {
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

            @Nullable
            @Override
            public AppEventListener getAppEventListener() {
                return null;
            }

            @Override
            public void setAppEventListener(@Nullable AppEventListener var1) {

            }

        });
    }
    private static void setMediationFlow(Context context, String adUnitId, AdManagerAdRequest adManagerAdRequest, AdManagerInterstitialAdLoadCallback loadCallback, Integer rwRequest, Integer originalRequest, String originalAdUnit){
        com.google.android.gms.ads.admanager.AdManagerInterstitialAd.load(context, adUnitId, adManagerAdRequest, new com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
//                loadCallback.onAdFailedToLoad(loadAdError);
                for(int count =0;count<idsListing.size();count++){
                    if(idsListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < idsListing.size()){
                        setMediationFlow(context,idsListing.get(count+1),adManagerAdRequest,loadCallback,rwRequest,originalRequest,originalAdUnit);
                    }
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.admanager.AdManagerInterstitialAd adManagerInterstitialAd) {
                super.onAdLoaded(adManagerInterstitialAd);
                if(idsListing!=null && idsListing.size() > 0) {
                    if (originalAdUnit.equalsIgnoreCase(adUnitId)) {
                        Integer originalVal = originalRequest;
                        RwAdsIntialize.saveIntegerShared(originalAdUnit + "_" + AdRequestOriginal, ++originalVal);
                    } else {
                        Integer rwVal = rwRequest;
                        RwAdsIntialize.saveIntegerShared(originalAdUnit + "_" + AdRequestReadWhere, ++rwVal);
                    }
                }
                adManagerInterstitial = adManagerInterstitialAd;
                loadCallback.onAdLoaded(new AdManagerInterstitialAd() {
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

                    @Nullable
                    @Override
                    public AppEventListener getAppEventListener() {
                        return null;
                    }

                    @Override
                    public void setAppEventListener(@Nullable AppEventListener var1) {

                    }
                });
            }
        });
    }
    private com.google.android.gms.ads.admanager.AdManagerInterstitialAd checkOverWriteAdUnitId(Activity context){
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
                return;
            }
            checkOverWriteAdUnitId(activity).show(activity);

        }else {
            adManagerInterstitial.show(activity);
        }

    }

    public abstract void setAppEventListener(@Nullable AppEventListener var1);

}
