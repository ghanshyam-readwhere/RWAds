package com.rwadswhitelabel;

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

        View view = (View) adView.getParent();
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).removeAllViews();
        }
        if (view instanceof RelativeLayout) {
            ((RelativeLayout) view).removeAllViews();
        }
        if (view instanceof ConstraintLayout) {
            ((ConstraintLayout) view).removeAllViews();
        }
        if(view instanceof CardView){
            ((CardView) view).removeAllViews();
        }
        AdManagerAdView adManager = new AdManagerAdView(context);

        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();

        int height = adView.getHeight();
        if((height /context.getResources().getDisplayMetrics().density ) > 250){
            adManager.setAdSizes(AdSize.MEDIUM_RECTANGLE);
        }else if((height /context.getResources().getDisplayMetrics().density ) > 100){
            adManager.setAdSizes(AdSize.LARGE_BANNER);
        }else {
            adManager.setAdSizes(AdSize.BANNER);
        }
        adManager.setAdUnitId(id);
        adManager.updateAdsWithRwFlow(view);

        adManager.loadAd(request);
    }

}
