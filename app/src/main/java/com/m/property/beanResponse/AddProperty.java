package com.m.property.beanResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AddProperty {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private Information information;

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

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public class Information {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("property_id")
        @Expose
        private Object propertyId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Object propertyId) {
            this.propertyId = propertyId;
        }

    }

}
