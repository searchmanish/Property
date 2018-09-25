package com.m.property.webServices;


import com.m.property.beanResponse.AddProperty;
import com.m.property.beanResponse.ForgotPassword;
import com.m.property.beanResponse.NewPassword;
import com.m.property.beanResponse.NewUserRegistration;
import com.m.property.beanResponse.PropertyDetails;
import com.m.property.beanResponse.PropertyDetailsHot;
import com.m.property.beanResponse.PropertyDetailsOwner;
import com.m.property.beanResponse.PropertySell;
import com.m.property.beanResponse.Registration;
import com.m.property.beanResponse.userSignin;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ServiceInterface {

    // method,, return type ,, secondary url
    // ecommerce-android-app-project/new_user_registration.php
    @Multipart
    @POST("property/new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );


    ///  user signin request
    @Multipart
    @POST("property/user_signin.php")
    Call<userSignin> UserSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("property/register.php")
    Call<Registration> register(
           /* @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("email") RequestBody email,
            @Part("username") RequestBody username*/
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("uid") RequestBody uid,
            @Part("type") RequestBody type
           // @Part("timestamp") RequestBody timestamp

    );

    ///  user forgot password request
    @Multipart
    @POST("property/user_forgot_password.php")
    Call<ForgotPassword> UserForgotPassword(
            @Part("phone") RequestBody phone
    );

    ///  create new password request
    @Multipart
    @POST("property/new_password.php")
    Call<NewPassword> UserNewPassword(
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password
    );

    // get new propertySell
    @Multipart
    @POST("property/property_sell.php")
    Call<PropertySell> getNewPorduct(
            @Part("securecode") RequestBody securecode
    );

    // get new propertyDeatailsFresh
    @Multipart
    @POST("property/getproperty_details_freshproperty.php")
    Call<PropertyDetails> getPorductDetailsFresh(
            @Part("securecode") RequestBody securecode
    );

    // get new propertyDeatailsOwner
    @Multipart
    @POST("property/getproperty_details_owner.php")
    Call<PropertyDetailsOwner> getPorductDetailsOwner(
            @Part("securecode") RequestBody securecode
    );

    // get new propertyDeatailsHot
    @Multipart
    @POST("property/getproperty_details_hot.php")
    Call<PropertyDetailsHot> getPorductDetailsHot(
            @Part("securecode") RequestBody securecode
    );

   @Multipart
    @POST("property/addProperty.php")
    Call<AddProperty> addNewProductDetails(
            @Part ("user_id") RequestBody user_id,
            @Part ("mrp") RequestBody mrp,
            @Part ("address") RequestBody address,
            @Part ("phone") RequestBody phone,
            @Part ("property_type") RequestBody propertyType,
            @Part ("details") RequestBody details

   );

}
