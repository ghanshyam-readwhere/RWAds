package com.rwadswhitelabel;

import android.content.Context;

import com.rwadswhitelabel.models.AdsMediationListing;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConfiguration {
    private static AppConfiguration sharedAppConfiguration = null;
    private ArrayList<AdsMediationListing> adsMediationListings = new ArrayList<>();
    private HashMap<String,Integer> adsPosition = new HashMap<>();
    private Integer rw_overwrite = 0;
    private Integer autoRefresh = 0;
    private Boolean requestBatch = false;

    public static AppConfiguration getInstance(Context mContext) {
        if(sharedAppConfiguration== null){
            sharedAppConfiguration = new AppConfiguration();
        }
        return sharedAppConfiguration;
    }


    public ArrayList<AdsMediationListing> getAdsMediationListings() {
        return adsMediationListings;
    }

    public void setRwOverWriteValue(Integer val){
        this.rw_overwrite = val;
    }
    public Integer getRw_overwrite(){
        return rw_overwrite;
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

    public Boolean getRequestBatch() {
        return requestBatch;
    }

    public void setRequestBatch(Boolean requestBatch) {
        this.requestBatch = requestBatch;
    }

    public Integer getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(Integer autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
}
