package com.m.property.beanResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropertyDetailsOwner {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private List<Information> information = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Information> getInformation() {
        return information;
    }

    public void setInformation(List<Information> information) {
        this.information = information;
    }

    public class Information {

        @SerializedName("property_id")
        @Expose
        private Integer propertyId;
        @SerializedName("property_details")
        @Expose
        private String propertyDetails;
        @SerializedName("property_mrp")
        @Expose
        private Double propertyMrp;
        @SerializedName("Property_address")
        @Expose
        private String propertyAddress;
        @SerializedName("property_phone")
        @Expose
        private String propertyPhone;
        @SerializedName("property_image")
        @Expose
        private String propertyImage;
        @SerializedName("property_listing_date")
        @Expose
        private String propertyListingDate;
        @SerializedName("property_rating")
        @Expose
        private Integer propertyRating;
        @SerializedName("Property_type")
        @Expose
        private String propertyType;
        @SerializedName("property_category")
        @Expose
        private Integer propertyCategory;

        public Integer getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Integer propertyId) {
            this.propertyId = propertyId;
        }

        public String getPropertyDetails() {
            return propertyDetails;
        }

        public void setPropertyDetails(String propertyDetails) {
            this.propertyDetails = propertyDetails;
        }

        public Double getPropertyMrp() {
            return propertyMrp;
        }

        public void setPropertyMrp(Double propertyMrp) {
            this.propertyMrp = propertyMrp;
        }

        public String getPropertyAddress() {
            return propertyAddress;
        }

        public void setPropertyAddress(String propertyAddress) {
            this.propertyAddress = propertyAddress;
        }

        public String getPropertyPhone() {
            return propertyPhone;
        }

        public void setPropertyPhone(String propertyPhone) {
            this.propertyPhone = propertyPhone;
        }

        public String getPropertyImage() {
            return propertyImage;
        }

        public void setPropertyImage(String propertyImage) {
            this.propertyImage = propertyImage;
        }

        public String getPropertyListingDate() {
            return propertyListingDate;
        }

        public void setPropertyListingDate(String propertyListingDate) {
            this.propertyListingDate = propertyListingDate;
        }

        public Integer getPropertyRating() {
            return propertyRating;
        }

        public void setPropertyRating(Integer propertyRating) {
            this.propertyRating = propertyRating;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        public Integer getPropertyCategory() {
            return propertyCategory;
        }

        public void setPropertyCategory(Integer propertyCategory) {
            this.propertyCategory = propertyCategory;
        }

    }

}
