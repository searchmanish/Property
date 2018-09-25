package com.m.property.addProperty;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String BASE_URL = "http://beliefitsolution.com/property/";

    //this is our multipart request
    //we have two parameters on is name and other one is description
  /*  @Multipart
    @POST("Api.php?apicall=upload")
    Call<ImageResponse> uploadImage
    (@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
                                 @Part("desc") RequestBody desc);

*/
    @Multipart
    @POST("upload3.php")
    Call<ImageResponse> uploadImage
    (@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
     @Part("name") RequestBody desc,
     @Part("property_id") RequestBody prop_id);

}
