package com.rwadswhitelabel;

import static com.rwadswhitelabel.RwAdsIntialize.AdRequestOriginal;
import static com.rwadswhitelabel.RwAdsIntialize.AdRequestReadWhere;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.gms.ads.internal.client.zzbs;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AdManagerAdView extends BaseAdView {


    private ArrayList<String> adUnitListing = new ArrayList<>();
    private View view;
    private Context context;

    private boolean bulkLoad = false;
    private HashMap<String,AdManagerAdView> loadedAds = new HashMap<>();
    int loadedCount =0;
    private boolean isLoaded = false;
    private ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture future;
    private int refreshInterval = 0;


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
    public void loadAds(@NonNull AdManagerAdRequest adManagerAdRequest) {
        super.loadAd(adManagerAdRequest);
    }
    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(@NonNull AdManagerAdRequest adManagerAdRequest) {
            this.view = (View) super.getParent();
            String adUnitId = super.getAdUnitId();
            AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
            if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
                adUnitListing = new ArrayList<>();
                adUnitListing.addAll(appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits());
                bulkLoad = appConfiguration.getRequestBatch();
                refreshInterval = appConfiguration.getAutoRefresh();
            }

        if(view != null && adUnitListing.size() != 0) {
            if(refreshInterval!= 0){
                refreshAd();
            }
            renderAds();
        }else {
            super.loadAd(adManagerAdRequest);
        }
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(@NonNull AdRequest adRequest) {
        this.view = (View) super.getParent();
        String adUnitId = super.getAdUnitId();
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            adUnitListing = new ArrayList<>();
            adUnitListing.addAll(appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits());
            bulkLoad = appConfiguration.getRequestBatch();
            refreshInterval = appConfiguration.getAutoRefresh();
        }
        if(view != null && adUnitListing.size() != 0) {
            if(refreshInterval!= 0){
                refreshAd();
            }
            renderAds();
        }else {
            super.loadAd(adRequest);
        }
    }

    private void renderAds(){
        if(bulkLoad){
            isLoaded = false;
            loadedCount = 0;
            for(int count =0;count<adUnitListing.size();count++){
                loadAdInBulk(adUnitListing.get(count),adUnitListing.size());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAdFromBulkList();
                }
            },2500);
        }else {
            String originalId = adUnitListing.get(0);
            int percent = AppConfiguration.getInstance(context).getRw_overwrite();
            int rwRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestReadWhere);
            int originalRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestOriginal);
            if(percent == 0){
                rwRequest = 0;
                originalRequest = 0;
            }else {
                if ((rwRequest + originalRequest) != 0 && rwRequest * 100 / (rwRequest + originalRequest) < percent) {
                    Collections.rotate(adUnitListing, -1);
                }
            }
            loadMediationAd(originalId,adUnitListing.get(0),rwRequest,originalRequest);
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

    private void showAdUpdates(AdManagerAdView adView){
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).removeAllViews();
            ((LinearLayout) view).setVisibility(VISIBLE);
            ((LinearLayout) view).addView(updateAdSlotUI(context,adView));
        }
        if (view instanceof RelativeLayout) {
            ((RelativeLayout) view).removeAllViews();
            ((RelativeLayout) view).setVisibility(VISIBLE);
            ((RelativeLayout) view).addView(updateAdSlotUI(context,adView));
        }
        if (view instanceof ConstraintLayout) {
            ((ConstraintLayout) view).removeAllViews();
            ((ConstraintLayout) view).setVisibility(VISIBLE);
            ((ConstraintLayout) view).addView(updateAdSlotUI(context,adView));
        }
        if(view instanceof CardView){
            ((CardView) view).removeAllViews();
            ((CardView) view).setVisibility(VISIBLE);
            ((CardView) view).addView(updateAdSlotUI(context,adView));
        }
    }
    private void loadMediationAd(String originalAdUnit,String adUnitId, Integer rwRequest, Integer originalRequest){

        AdSize adSize = super.getAdSize();
        AdManagerAdView adView = new AdManagerAdView(context);



        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        adView.setAdSize(adSize);
        adView.setAdUnitId(adUnitId);
        adView.loadAds(request);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adView.setAdListener(null);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
                for(int count =0;count<adUnitListing.size();count++){
                    if(adUnitListing.get(count).equalsIgnoreCase(adUnitId) && (count+1) < adUnitListing.size()){
                        loadMediationAd(originalAdUnit,adUnitListing.get(count+1),rwRequest,originalRequest);
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
                showAdUpdates(adView);
                Log.d("TAG", "onAdFailedToLoad:false "+ adUnitId);
                if(originalAdUnit.equalsIgnoreCase(adUnitId)){
                    Integer originalVal = originalRequest;
                    RwAdsIntialize.saveIntegerShared(originalAdUnit+"_"+AdRequestOriginal, ++originalVal);
                }else {
                    Integer rwVal = rwRequest;
                    RwAdsIntialize.saveIntegerShared(originalAdUnit+"_"+AdRequestReadWhere,++rwVal);
                }
            }
        });
    }

    private void refreshAd(){
        if (future != null)
            future.cancel(false);

        if (worker == null) {
            worker = Executors.newSingleThreadScheduledExecutor();
        }
        future = worker.schedule(new Runnable() {
            @Override
            public void run() {
                if (!((Activity)context).isDestroyed()) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            renderAds();
                        }
                    });
                    refreshAd();
                } else {
                    detachBanner();
                }
            }
        }, refreshInterval, TimeUnit.SECONDS);
    }

    private void loadAdInBulk(String adUnitId, int totalCount){
        AdSize adSize = super.getAdSize();
        AdManagerAdView adView = new AdManagerAdView(context);
        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        adView.setAdSize(adSize);
        adView.setAdUnitId(adUnitId);
        adView.loadAds(request);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("TAG", "onAdFailedToLoad: "+ adUnitId);
                adView.setAdListener(null);
                loadedCount++;
                if(loadedCount == totalCount){
                    showAdFromBulkList();
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("TAG", "onAdFailedToLoad:false "+ adUnitId);
                loadedCount++;
                loadedAds.put(adUnitId,adView);
                if(loadedCount == totalCount){
                    showAdFromBulkList();
                }
            }
        });
    }

    private AdManagerAdView checkOverWriteAdUnitId(){
        String originalId = adUnitListing.get(0);
        int percent = AppConfiguration.getInstance(context).getRw_overwrite();
        int rwRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestReadWhere);
        int originalRequest = RwAdsIntialize.getIntegerShared(originalId+"_"+AdRequestOriginal);

        if(percent != 0 && (rwRequest+originalRequest) != 0 &&rwRequest * 100/(rwRequest+originalRequest) < percent){
            Collections.rotate(adUnitListing,-1);
        }
        for(int count =0;count<adUnitListing.size();count++) {
            if (loadedAds.containsKey(adUnitListing.get(count))) {
                if(adUnitListing.get(count).equalsIgnoreCase(originalId)){
                    RwAdsIntialize.saveIntegerShared(originalId+"_"+AdRequestOriginal,++originalRequest);
                }else {
                    RwAdsIntialize.saveIntegerShared(originalId+"_"+AdRequestReadWhere,++rwRequest);
                }
                return loadedAds.get(adUnitListing.get(count));
            }
        }
        return  null;
    }

    protected void showAdFromBulkList() {
        if (isLoaded || loadedAds.size() == 0) {
            return;
        }
        AdManagerAdView adView = checkOverWriteAdUnitId();
//        for(int count =0;count<adUnitListing.size();count++){
//            if(loadedAds.containsKey(adUnitListing.get(count))){
//                isLoaded = true;
//                adView = loadedAds.get(adUnitListing.get(count));
//                break;
//            }
//        }
        if (adView != null) {
            isLoaded = true;
            showAdUpdates(adView);
        }
    }


    private void detachBanner(){
        try {
            if (future != null) {
                future.cancel(true);
                future = null;
            }
            if (worker != null) {
                worker.shutdown();
                worker = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinearLayout updateAdSlotUI(Context _activity, View adView){
        LinearLayout linearLayout = new LinearLayout(_activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0,10,0,30);
        linearLayout.setBackgroundColor(_activity.getResources().getColor(R.color.adv_bg));
        TextView textView = new TextView(_activity);
        textView.setText("ADVERTISEMENT");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = 10;
        textView.setLayoutParams(lp);
        textView.setTextColor(Color.parseColor("#888888"));
        linearLayout.addView(textView);
        linearLayout.addView(adView);
        return  linearLayout;
    }

}
