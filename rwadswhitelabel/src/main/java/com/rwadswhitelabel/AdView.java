package com.rwadswhitelabel;

import android.content.Context;
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
import com.google.android.gms.common.internal.Preconditions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdView extends BaseAdView {

    private ArrayList<String> adUnitListing = new ArrayList<>();
    private View view;
    private Context context;

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
    public void loadAd(AdRequest adRequest){

        if(view != null && adUnitListing.size() != 0) {
            loadMediationAd(adUnitListing.get(0));
        }else {
            super.loadAd(adRequest);
        }

    }
    public void updateAdsWithRwFlow(@Nullable View linearLayout) {
        this.view = linearLayout;
        String adUnitId = super.getAdUnitId();
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (appConfiguration.getAdsPosition().containsKey(adUnitId)) {
            adUnitListing = appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(adUnitId)).getMediationsAdunits();
        }

    }

    private void loadMediationAd(String adUnitId){
        AdSize adSize = super.getAdSize();
        AdView adView = new AdView(context);
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
