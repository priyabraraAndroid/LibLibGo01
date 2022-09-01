package com.lib.liblibgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentModel {
    @SerializedName("apartment_id")
    @Expose
    private String apartmentId;
    @SerializedName("apartment_name")
    @Expose
    private String apartmentName;
    public final static Parcelable.Creator<ApartmentModel> CREATOR = new Parcelable.Creator<ApartmentModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public com.lib.liblibgo.model.ApartmentModel createFromParcel(Parcel in) {
            return new com.lib.liblibgo.model.ApartmentModel(in);
        }

        public com.lib.liblibgo.model.ApartmentModel[] newArray(int size) {
            return (new com.lib.liblibgo.model.ApartmentModel[size]);
        }

    }
            ;

    public ApartmentModel(Parcel in) {
        this.apartmentId = ((String) in.readValue((String.class.getClassLoader())));
        this.apartmentName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ApartmentModel() {

    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public static Parcelable.Creator<ApartmentModel> getCREATOR() {
        return CREATOR;
    }
}
