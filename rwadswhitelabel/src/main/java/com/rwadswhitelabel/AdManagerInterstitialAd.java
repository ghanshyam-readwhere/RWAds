package com.rwadswhitelabel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AppEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AdManagerInterstitialAd extends com.google.android.gms.ads.admanager.AdManagerInterstitialAd {

    private static com.google.android.gms.ads.admanager.AdManagerInterstitialAd adManagerInterstitial;
    private static ArrayList<String> idsListing = new ArrayList();

    @Nullable
    public abstract AppEventListener getAppEventListener();

    public AdManagerInterstitialAd() {

    }

    public static void load(@NonNull Context context, @NonNull String adUnitId, @NonNull AdManagerAdRequest adManagerAdRequest, @NonNull AdManagerInterstitialAdLoadCallback loadCallback) {
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            idsListing = appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits();
        }
        if(idsListing!=null && idsListing.size() > 0){
            setMediationFlow(context,idsListing.get(0),adManagerAdRequest,loadCallback);
        }else {
            setMediationFlow(context,adUnitId,adManagerAdRequest,loadCallback);
        }
    }

    private static void setMediationFlow(Context context, String adUnitId, AdManagerAdRequest adManagerAdRequest, AdManagerInterstitialAdLoadCallback loadCallback){
        com.google.android.gms.ads.admanager.AdManagerInterstitialAd.load(context, adUnitId, adManagerAdRequest, new com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
//                loadCallback.onAdFailedToLoad(loadAdError);
                for(int count =0;count<idsListing.size();count++){
                    if(idsListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < idsListing.size()){
                        setMediationFlow(context,idsListing.get(count+1),adManagerAdRequest,loadCallback);
                    }
                }
            }

            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.admanager.AdManagerInterstitialAd adManagerInterstitialAd) {
                super.onAdLoaded(adManagerInterstitialAd);
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

    public void show(Activity activity){
        adManagerInterstitial.show(activity);
    }

    public abstract void setAppEventListener(@Nullable AppEventListener var1);

}
