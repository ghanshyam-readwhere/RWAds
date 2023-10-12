package com.rwadswhitelabel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresPermission;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.BaseAdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AppEventListener;
import com.google.android.gms.ads.internal.client.zzbs;
import com.google.android.gms.common.internal.Preconditions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdManagerAdView extends BaseAdView {


    private ArrayList<String> adUnitListing = new ArrayList<>();
    private View view;
    private Context context;

    @NonNull
    public VideoController getVideoController() {
        return this.zza.zzf();
    }

    @Nullable
    public VideoOptions getVideoOptions() {
        return this.zza.zzg();
    }

    @Nullable
    public AppEventListener getAppEventListener() {
        return this.zza.zzh();
    }

    public AdManagerAdView(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public AdManagerAdView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs, true);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public AdManagerAdView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, 0, true);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(@NonNull AdManagerAdRequest adManagerAdRequest) {

        if(view != null && adUnitListing.size() != 0) {
            loadMediationAd(adUnitListing.get(0));
        }else {
            super.loadAd(adManagerAdRequest);
        }
    }

    public void recordManualImpression() {
        this.zza.zzo();
    }

    public void setAdSizes(@NonNull AdSize... adSizes) {
        if (adSizes != null && adSizes.length > 0) {
            this.zza.zzt(adSizes);
        } else {
            IllegalArgumentException var2 = new IllegalArgumentException("The supported ad sizes must contain at least one valid ad size.");
            throw var2;
        }
    }

    public void setAppEventListener(@Nullable AppEventListener appEventListener) {
        this.zza.zzv(appEventListener);
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
        this.zza.zzw(manualImpressionsEnabled);
    }

    public void setVideoOptions(@NonNull VideoOptions videoOptions) {
        this.zza.zzy(videoOptions);
    }

    @Nullable
    public AdSize[] getAdSizes() {
        return this.zza.zzB();
    }

    public final boolean zzb(zzbs var1) {
        return this.zza.zzz(var1);
    }
    public void updateAdsWithRwFlow(View linearLayout) {
        this.view = linearLayout;
        String adUnitId = super.getAdUnitId();
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            adUnitListing = appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits();
        }
    }

    private void loadMediationAd(String adUnitId){

        AdSize adSize = super.getAdSize();
        AdManagerAdView adView = new AdManagerAdView(context);
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).removeAllViews();
            ((LinearLayout) view).addView(adView);
        }
        if (view instanceof RelativeLayout) {
            ((RelativeLayout) view).removeAllViews();
            ((RelativeLayout) view).addView(adView);
        }
        if (view instanceof ConstraintLayout) {
            ((ConstraintLayout) view).removeAllViews();
            ((ConstraintLayout) view).addView(adView);
        }
        if(view instanceof CardView){
            ((CardView) view).removeAllViews();
            ((CardView) view).addView(adView);
        }
        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        adView.setAdSize(adSize);
        adView.setAdUnitId(adUnitId);
        adView.loadAd(request);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adView.setAdListener(null);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
                for(int count =0;count<adUnitListing.size();count++){
                    if(adUnitListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < adUnitListing.size()){
                        loadMediationAd(adUnitListing.get(count+1));
                    }
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }
}
