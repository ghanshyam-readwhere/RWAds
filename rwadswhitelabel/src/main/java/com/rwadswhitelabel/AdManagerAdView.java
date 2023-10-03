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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.BaseAdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AppEventListener;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.ads.zzbfn;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdManagerAdView extends BaseAdView {


    private ArrayList<String> adUnitListing = new ArrayList<>();
    private View view;
    private Context context;


    public AdManagerAdView( Context context) {
        super(context, 0);
        this.context = context;
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public AdManagerAdView( Context context,  AttributeSet attrs) {
        super(context, attrs, true);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public AdManagerAdView( Context context,  AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, 0, true);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    
    public VideoController getVideoController() {
        return this.zza.zzw();
    }

    public void setVideoOptions( VideoOptions videoOptions) {
        this.zza.zzy(videoOptions);
    }

    public VideoOptions getVideoOptions() {
        return this.zza.zzz();
    }

    public AdSize[] getAdSizes() {
        return this.zza.zze();
    }

    public AppEventListener getAppEventListener() {
        return this.zza.zzg();
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd( AdManagerAdRequest adManagerAdRequest) {
        if(view != null && adUnitListing.size() != 0) {
            loadMediationAd(adUnitListing.get(0));
        }else {
            this.zza.zzh(adManagerAdRequest.zza());
        }

    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
        this.zza.zzr(manualImpressionsEnabled);
    }

    public void recordManualImpression() {
        this.zza.zzj();
    }

    public void setAdSizes( AdSize... adSizes) {
        if (adSizes != null && adSizes.length > 0) {
            this.zza.zzo(adSizes);
        } else {
            IllegalArgumentException var2 = new IllegalArgumentException("The supported ad sizes must contain at least one valid ad size.");
            throw var2;
        }
    }

    public void setAppEventListener(@Nullable AppEventListener appEventListener) {
        this.zza.zzq(appEventListener);
    }

    public final boolean zza(zzbfn var1) {
        return this.zza.zzA(var1);
    }


    public void updateAdsWithRwFlow(View linearLayout, @NotNull ArrayList<String> list) {
        this.view = linearLayout;
        adUnitListing = list;
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
        AdRequest request = new AdRequest.Builder().build();
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
