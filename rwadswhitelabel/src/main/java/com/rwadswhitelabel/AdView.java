package com.rwadswhitelabel;

import static com.rwadswhitelabel.RwAdsIntialize.AdRequestOriginal;
import static com.rwadswhitelabel.RwAdsIntialize.AdRequestReadWhere;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.BaseAdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.common.internal.Preconditions;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AdView extends BaseAdView {

    private ArrayList<String> adUnitListing = new ArrayList<>();
    private View view;
    private Context context;
    private boolean bulkLoad = false;
    private HashMap<String,AdView> loadedAds = new HashMap<>();
    int loadedCount =0;
    private boolean isLoaded = false;
    private ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture future;
    private int refreshInterval = 0;

    public AdView(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        Preconditions.checkNotNull(context, "Context cannot be null");
    }


    protected AdView(@NonNull Context context, int adViewType) {
        super(context, adViewType);
    }

    protected AdView(@NonNull Context context, @NonNull AttributeSet attrs, int adViewType) {
        super(context, attrs, adViewType);
    }

    protected AdView(@NonNull Context context, @NonNull AttributeSet attrs, boolean allowMultipleAdSizes) {
        super(context, attrs, allowMultipleAdSizes);
    }

    protected AdView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle, int adViewType) {
        super(context, attrs, defStyle, adViewType);
    }

    protected AdView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle, int adViewType, boolean allowMultipleAdSizes) {
        super(context, attrs, defStyle, adViewType, allowMultipleAdSizes);
    }

    public void loadAds(AdRequest adRequest){
        super.loadAd(adRequest);
    }

    public void loadAd(AdRequest adRequest){
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
            loadedCount = 0;
            isLoaded = false;
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
    public void loadAd(AdManagerAdRequest adManagerAdRequest){
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
    public void updateAdsWithRwFlow(@Nullable View linearLayout) {
        this.view = linearLayout;
        String adUnitId = super.getAdUnitId();
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            adUnitListing = new ArrayList<>();
            adUnitListing.addAll(appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits());
            bulkLoad = appConfiguration.getRequestBatch();
        }

    }
    private void loadAdInBulk(String adUnitId, int totalCount){
        AdSize adSize = super.getAdSize();
        AdView adView = new AdView(context);
        AdRequest request = new AdRequest.Builder().build();
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
                Log.d("TAG", "loadAdInBulk: "+adUnitId);
                loadedCount++;
                loadedAds.put(adUnitId,adView);
                if(loadedCount == totalCount){
                    showAdFromBulkList();
                }
            }
        });
    }


    private AdView checkOverWriteAdUnitId(){
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

    protected void showAdFromBulkList(){
        if (isLoaded || loadedAds.size() == 0) {
            return;
        }
        AdView adView = checkOverWriteAdUnitId();

//        for(int count =0;count<adUnitListing.size();count++){
//            if(loadedAds.containsKey(adUnitListing.get(count))){
//                 adView = loadedAds.get(adUnitListing.get(count));
//                 isLoaded =true;
//                break;
//            }
//        }
        if (adView != null) {
            isLoaded = true;
            showAdUpdates(adView);
        }
    }

    private void showAdUpdates(AdView adView){
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).removeAllViews();
            ((LinearLayout) view).setVisibility(VISIBLE);
            ((LinearLayout) view).addView(adView);
        }
        if (view instanceof RelativeLayout) {
            ((RelativeLayout) view).removeAllViews();
            ((RelativeLayout) view).setVisibility(VISIBLE);
            ((RelativeLayout) view).addView(adView);
        }
        if (view instanceof ConstraintLayout) {
            ((ConstraintLayout) view).removeAllViews();
            ((ConstraintLayout) view).setVisibility(VISIBLE);
            ((ConstraintLayout) view).addView(adView);
        }
        if (view instanceof CardView) {
            ((CardView) view).removeAllViews();
            ((CardView) view).setVisibility(VISIBLE);
            ((CardView) view).addView(adView);
        }
    }

    private void loadMediationAd(String originalAdUnit,String adUnitId, Integer rwRequest, Integer originalRequest){
        AdSize adSize = super.getAdSize();
        AdView adView = new AdView(context);

        AdRequest request = new AdRequest.Builder().build();
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
}
