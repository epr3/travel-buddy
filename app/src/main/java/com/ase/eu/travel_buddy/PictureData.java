package com.ase.eu.travel_buddy;

import android.net.Uri;

public class PictureData {

    private static PictureData instance = null;
    private Uri downloadUri;
    private String locationName;

    public static PictureData getInstance() {
        if(instance == null) {
            instance = new PictureData();
        }
        return instance;
    }

    public Uri getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(Uri downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
