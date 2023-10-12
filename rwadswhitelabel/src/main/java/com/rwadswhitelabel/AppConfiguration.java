package com.rwadswhitelabel;

import android.content.Context;

import com.rwadswhitelabel.models.AdsMediationListing;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConfiguration {
    private static AppConfiguration sharedAppConfiguration = null;
    private ArrayList<AdsMediationListing> adsMediationListings = new ArrayList<>();
    private HashMap<String,Integer> adsPosition = new HashMap<>();

    public static AppConfiguration getInstance(Context mContext) {
        if(sharedAppConfiguration== null){
            sharedAppConfiguration = new AppConfiguration();
        }
        return sharedAppConfiguration;
    }


    public ArrayList<AdsMediationListing> getAdsMediationListings() {
        return adsMediationListings;
    }

    public void setAdsMediationListings(ArrayList<AdsMediationListing> adsMediationListings) {
        this.adsMediationListings = adsMediationListings;
        HashMap<String, Integer> hashMap= new HashMap<>();
        for(int count =0;count<adsMediationListings.size();count++){
            hashMap.put(adsMediationListings.get(count).getOriginalId(),count);
        }
        setAdsPosition(hashMap);
    }

    public HashMap<String, Integer> getAdsPosition() {
        return adsPosition;
    }

    public void setAdsPosition(HashMap<String, Integer> adsPosition) {
        this.adsPosition = adsPosition;
    }
}
