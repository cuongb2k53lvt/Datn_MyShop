package com.example.modelfashion.Interface;

import com.example.modelfashion.Model.MHistory.BillModel;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.Model.response.my_product.CartProduct;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.Model.response.my_product.Sizes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiRetrofit {
    Gson gson = new GsonBuilder().setLenient().create();
    ApiRetrofit apiRetrofit = new Retrofit.Builder().baseUrl("https://cuongb2k53lvt.000webhostapp.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiRetrofit.class);
    @Multipart
    @POST("FashionShop-phpServer/upload_avatar.php")
    Call<String> uploadAvatar(@Part MultipartBody.Part avatar);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/insert_user.php")
    Call<String> InsertUser (@Field("taikhoan") String taikhoan, @Field("matkhau") String matkhau);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/get_products_size.php")
    Call<ArrayList<Sizes>> GetProductsSize(@Field("product_name") String product_name);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/check_quantity_left.php")
    Call<String> CheckSizeLeft(@Field("size_id") String size_id, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/insert_bill_buy_now.php")
    Call<String> InsertBillBuyNow(@Field("user_id") String user_id, @Field("date") String date,
                                  @Field("price") String price, @Field("size_id") String size_id);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/insert_cart.php")
    Call<String> InsertCart(@Field("user_id") String user_id, @Field("size_id") String size_id,
                                @Field("product_name") String product_name, @Field("quantity") String quantity);

    @GET("FashionShop-phpServer/get_user_cart.php")
    Call<ArrayList<CartProduct>> GetCartProduct(@Query("user_id") String user_id);

    @GET("FashionShop-phpServer/get_product_by_name.php")
    Call<ArrayList<MyProduct>> GetProductByName(@Query("product_name") JSONArray product_name, @Query("user_id") String user_id);

    @GET("FashionShop-phpServer/get_sizes_by_id.php")
    Call<ArrayList<Sizes>> GetSizeById(@Query("sizes_id") JSONArray sizes_id, @Query("user_id") String user_id);

    @GET("FashionShop-phpServer/get_amount_cart.php")
    Call<String> GetAmountCart(@Query("product_name") JSONArray product_name, @Query("user_id") String user_id);

    @POST("insert_user.php")
    Call<User> editUser(@Body User user);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/insert_payment.php")
    Call<String> InsertPayment(@Field("user_id") String user_id, @Field("amount") String amount,
                               @Field("date_created") String date_created, @Field("arr_sizes") String arr_sizes);


    @GET("FashionShop-phpServer/get_bill_by_user_id.php")
    Call<ArrayList<Bill>> GetBillByUserId(@Query("user_id") String user_id, @Query("status") String status);

    @GET("FashionShop-phpServer/get_all_product_by_detail_id.php")
    Call<ArrayList<MyProduct>> GetProductByDetailId(@Query("detail_id") JSONArray detail_id);

    @GET("FashionShop-phpServer/get_user_by_id.php")
    Call<User> GetUserById(@Query("user_id") String user_id);

    @GET("FashionShop-phpServer/get_bill_detail_in_bill.php")
    Call<ArrayList<BillDetail>> GetBillDetailInBill(@Query("bill_id") String bill_id);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/delete_product_from_cart_by_size_id.php")
    Call<String> DeleteProductFromCart(@Field("user_id") String user_id, @Field("size_id") String size_id);


    @FormUrlEncoded
    @POST("FashionShop-phpServer/update_avatar_user.php")
    Call<String> UpdateAvatar(@Field("user_id") String user_id, @Field("new_avatar") String new_avatar, @Field("old_avatar") String old_avatar);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/change_cart_quantity.php")
    Call<String> ChangeCartQuantity(@Field("user_id") String user_id, @Field("quantity") String quantity, @Field("size_id") String size_id);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/insert_fcm_token.php")
    Call<String> InsertFcmToken(@Field("user_id") String user_id, @Field("token") String token);

    @FormUrlEncoded
    @POST("FashionShop-phpServer/delete_fcm_token.php")
    Call<String> DeleteFcmToken(@Field("user_id") String user_id, @Field("token") String token);

    @FormUrlEncoded
    @POST("https://payment.momo.vn/application/json")
    Call<String> getMomo(@Field("user_id") String user_id, @Field("token") String token);

}
