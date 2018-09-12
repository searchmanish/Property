package com.m.property.webServices;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.m.property.BuildConfig;
import com.m.property.beanResponse.ForgotPassword;
import com.m.property.beanResponse.NewPassword;
import com.m.property.beanResponse.NewUserRegistration;
import com.m.property.beanResponse.Registration;
import com.m.property.beanResponse.userSignin;
import com.m.property.utility.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceWrapper {

    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Call<NewUserRegistration> newUserRegistrationCall(String fullname, String email, String phone, String username, String password) {
        return mServiceInterface.NewUserRegistrationCall(convertPlainString(fullname), convertPlainString(email), convertPlainString(phone), convertPlainString(username),
                convertPlainString(password));
    }


    //  user signin
    public Call<userSignin> UserSigninCall(String phone, String password) {
        return mServiceInterface.UserSigninCall(convertPlainString(phone), convertPlainString(password));

    }

    //Social signin

    public Call<Registration> socialRegister(String fullname, String email, String uid, String type) {
        return mServiceInterface.register(convertPlainString(fullname),
                convertPlainString(email), convertPlainString(uid), convertPlainString(type));
    }

    //  forgot password
    public Call<ForgotPassword> UserForgotPassword(String phone) {
        return mServiceInterface.UserForgotPassword(convertPlainString(phone));
    }

    //  user new password
    public Call<NewPassword> UserNewPassword(String userid, String password) {
        return mServiceInterface.UserNewPassword(convertPlainString(userid), convertPlainString(password));
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }
}


