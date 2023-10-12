package com.rwadswhitelabel;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import java.util.ArrayList;
import java.util.Arrays;


public abstract class InterstitialAd extends com.google.android.gms.ads.interstitial.InterstitialAd {
    private static com.google.android.gms.ads.interstitial.InterstitialAd interstitial;
    private static ArrayList<String> idsListing = new ArrayList();


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
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            idsListing = appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits();
        }
        if(idsListing!=null && idsListing.size() > 0){
            setMediationFlow(context,idsListing.get(0),adRequest,loadCallback);
        }else {
            setMediationFlow(context,adUnitId,adRequest,loadCallback);
        }
    }

    private static void setMediationFlow(Context context, String adUnitId, AdRequest adRequest, InterstitialAdLoadCallback loadCallback){
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, adUnitId, adRequest, new com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
//                loadCallback.onAdFailedToLoad(loadAdError);
                for(int count =0;count<idsListing.size();count++){
                    if(idsListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < idsListing.size()){
                        setMediationFlow(context,idsListing.get(count+1),adRequest,loadCallback);
                    }
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                interstitial = interstitialAd;
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

    public void show(Activity activity){
        interstitial.show(activity);
    }

    public abstract void setFullScreenContentCallback(@Nullable FullScreenContentCallback var1);

    public abstract void setImmersiveMode(boolean var1);

    public abstract void setOnPaidEventListener(@Nullable OnPaidEventListener var1);


}
