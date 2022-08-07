package com.example.modelfashion.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Activity.MainActivity;
import com.example.modelfashion.Adapter.cart.CartAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.Product;
import com.example.modelfashion.Model.response.my_product.CartProduct;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.Model.response.my_product.Sizes;
import com.example.modelfashion.R;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class CartFragment extends Fragment {
    private View initView;
    private RecyclerView recyclerView;
    private ArrayList<MyProduct> arrProduct = new ArrayList<>();
    private ArrayList<CartProduct> arrCart = new ArrayList<>();
    private ArrayList<Sizes> arrSize = new ArrayList<>();
    private ArrayList<String> arr_size_id = new ArrayList<>();
    private ArrayList<String> arr_product_name = new ArrayList<>();
    private String user_id, total_money;
    private TextView tvTotal;
    private Button btn_payment, btn_payment_momo;
    private Boolean check_load_successful = false;
    private JSONArray json_product_name, json_size_id;

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "CÔNG TY CỔ PHẦN GIẢI PHÁP CÔNG NGHỆ UNIKOM";
    private String merchantCode = "MOMOJDFR20220731";
    private String merchantNameLabel = "BacNguyen";
    private String description = "Mua Quan Ao";
    private Boolean isMomoPay;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        initView = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = initView.findViewById(R.id.list_product_cart);
        tvTotal = initView.findViewById(R.id.total_money);
        btn_payment = initView.findViewById(R.id.btn_payment);
        btn_payment_momo = initView.findViewById(R.id.btn_payment_momo);
        Bundle info = getArguments();
        user_id = info.getString("user_id");
        getCart();
        btn_payment.setOnClickListener(new View.OnClickListener() {
                                           @RequiresApi(api = Build.VERSION_CODES.O)
                                           @Override
                                           public void onClick(View v) {
                                               isMomoPay = false;
                                               if (check_load_successful == true) {
                                                   Gson gson = new Gson();
                                                   String arr_json = gson.toJson(arrSize);
                                                   String date = LocalDate.now().toString();
                                                   check_load_successful = false;
                                                   insertBill(user_id, total_money, date, arr_json);
                                               }
                                           }
                                       }
        );
        btn_payment_momo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                isMomoPay = true;
                requestPayment(Integer.parseInt(user_id));
            }
        });
        return initView;
    }

    //Get token through MoMo app
    private void requestPayment(int idDonHang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", idDonHang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", idDonHang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", "0"); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack((Activity) getContext(), eventValue);

    }

    //Get token callback from MoMo app an submit to server side
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    Log.e("Thanh Cong", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }
                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.e("Khong Thanh Cong", "khong thanh cong");
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Log.e("Khong Thanh Cong", "khong thanh cong");

                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.e("Khong Thanh Cong", "khong thanh cong");

                } else {
                    //TOKEN FAIL
                    Log.e("Khong Thanh Cong", "khong thanh cong");

                }
            } else {
                Log.e("Khong Thanh Cong", "khong thanh cong");

            }
        } else {
            Log.e("Khong Thanh Cong", "khong thanh cong");

        }
    }

    private void getCart() {
        ApiRetrofit.apiRetrofit.GetCartProduct(user_id).enqueue(new Callback<ArrayList<CartProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<CartProduct>> call, Response<ArrayList<CartProduct>> response) {
                arrCart = response.body();
                if (arrCart != null){
                    for (int i = 0; i < arrCart.size(); i++) {
                        arr_product_name.add(arrCart.get(i).getProductName());
                        arr_size_id.add(arrCart.get(i).getSizeId());
                    }
                }
                json_product_name = new JSONArray(arr_product_name);
                json_size_id = new JSONArray(arr_size_id);
                getProductInfo(json_product_name, json_size_id);
                getAmountCart(json_product_name);
                Log.e("cart", arrCart.size() + "");
            }

            @Override
            public void onFailure(Call<ArrayList<CartProduct>> call, Throwable t) {
                Log.e("loaderr", t.toString());
            }
        });
    }

    private void getProductInfo(JSONArray arr_product_name, JSONArray arr_size_id) {
        ApiRetrofit.apiRetrofit.GetProductByName(arr_product_name, user_id).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arrProduct = response.body();
                getSizeInfo(arr_size_id);
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {
                Log.e("loaderr", t.toString());
            }
        });
    }

    private void getSizeInfo(JSONArray arr_size_id) {
        ApiRetrofit.apiRetrofit.GetSizeById(arr_size_id, user_id).enqueue(new Callback<ArrayList<Sizes>>() {
            @Override
            public void onResponse(Call<ArrayList<Sizes>> call, Response<ArrayList<Sizes>> response) {
                arrSize = response.body();
                setAdapter();
            }

            @Override
            public void onFailure(Call<ArrayList<Sizes>> call, Throwable t) {
                Log.e("loaderr", t.toString());
            }
        });
    }

    private void getAmountCart(JSONArray arr_product_name) {
        ApiRetrofit.apiRetrofit.GetAmountCart(arr_product_name, user_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (arr_product_name.length() > 0) {
                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String money_format = formatter.format(Integer.parseInt(response.body()));
                    tvTotal.setText("Tổng tiền: " + money_format + " VNĐ");
                    total_money = response.body();
                    check_load_successful = response.isSuccessful();
                }
                tvTotal.setText("Tổng tiền: " + response.body() + " VNĐ");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("loaderr", t.toString());
            }
        });
    }

    private void insertBill(String user_id, String amount, String date, String arr_size) {
        ApiRetrofit.apiRetrofit.InsertPayment(user_id, amount, date, arr_size).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("ok")) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), response.body() + " không đủ trong kho!", Toast.LENGTH_SHORT).show();
                    Log.e("size", response.body());
                }
                check_load_successful = true;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("loaderr", t.toString());
            }
        });
    }

    private void setAdapter() {
        CartAdapter adapter = new CartAdapter(arrProduct, arrSize, getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(new CartAdapter.CartOnClick() {
            @Override
            public void OnClick(int position, String size_id) {
                arrProduct.remove(position);
                arrSize.remove(position);
                ApiRetrofit.apiRetrofit.DeleteProductFromCart(user_id, size_id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        btn_payment.setEnabled(false);
                        if (response.body().equalsIgnoreCase("ok")) {
                            getCart();
                        } else {
                            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                        btn_payment.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void ChangeQuantity(String size_id, String quantity, Button btn) {
                ApiRetrofit.apiRetrofit.ChangeCartQuantity(user_id, quantity, size_id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Event khi doi sl thanh cong
                        btn.setEnabled(false);
                        if (response.body().equalsIgnoreCase("ok")) {
                            getAmountCart(json_product_name);
                            JSONArray arr_json_size_id = new JSONArray(arr_size_id);
                            ApiRetrofit.apiRetrofit.GetSizeById(arr_json_size_id, user_id).enqueue(new Callback<ArrayList<Sizes>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Sizes>> call, Response<ArrayList<Sizes>> response) {
                                    btn_payment.setEnabled(false);
                                    arrSize = response.body();
                                    btn_payment.setEnabled(true);
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Sizes>> call, Throwable t) {
                                    Log.e("loaderr", t.toString());
                                }
                            });
                        } else {

                        }
                        btn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
    }

}