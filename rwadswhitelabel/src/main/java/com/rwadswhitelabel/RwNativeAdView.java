package com.rwadswhitelabel;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.nativead.NativeAdView;


public class RwNativeAdView {

    public static void initNativeAds(Context context,NativeAdView adView,String id){
        AppConfiguration appConfiguration = AppConfiguration.getInstance(context);
        if (!appConfiguration.getAdsPosition().containsKey(id) || appConfiguration.getAdsMediationListings().get(appConfiguration.getAdsPosition().get(id)).getSwitchBetNativeToBanner() != 1) {
            return;
        }


        AdManagerAdView adManager = new AdManagerAdView(context);

        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();

        int height = adView.getHeight();
        if((height /context.getResources().getDisplayMetrics().density ) > 250){
            adManager.setAdSize(AdSize.MEDIUM_RECTANGLE);
        }else if((height /context.getResources().getDisplayMetrics().density ) > 100){
            adManager.setAdSize(AdSize.LARGE_BANNER);
        }else {
            adManager.setAdSize(AdSize.BANNER);
        }
        View view = (View) adView.getParent();
        view.setVisibility(View.GONE);
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).removeAllViews();
            ((LinearLayout) view).addView(adManager);
        }
        if (view instanceof RelativeLayout) {
            ((RelativeLayout) view).removeAllViews();
            ((RelativeLayout) view).addView(adManager);
        }
        if (view instanceof ConstraintLayout) {
            ((ConstraintLayout) view).removeAllViews();
            ((ConstraintLayout) view).addView(adManager);
        }
        if(view instanceof CardView){
            ((CardView) view).removeAllViews();
            ((CardView) view).addView(adManager);
        }

        adManager.setAdUnitId(id);
        adManager.loadAd(request);
    }

}
