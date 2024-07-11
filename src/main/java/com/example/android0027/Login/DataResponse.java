package com.example.android0027.Login;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class DataResponse {
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userPassword")
    private String userPassword;
    @SerializedName("userAdr")
    private String userAdr;
    @SerializedName("userCno")
    private String userCno;
    @SerializedName("userNm")
    private String userNm;
    @SerializedName("userBirthDt")
    private String userBirthDt;

    public String getUserAdr(){return userAdr;}
    public String getUserCno(){return userCno;}
    public String getUserNm(){return userNm;}
    public String getUserBirthDt(){return userBirthDt;}

    public String getUserEmail(){return userEmail;}
    public String getUserPassword(){return userPassword;}



}

//    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//    Call<List<CctvResponse>> call = apiService.getCctvList();
//        call.enqueue(new Callback<CctvResponse>() {
//@Override
//public void onResponse
//        (Call<CctvResponse> call, Response<CctvResponse> response) {
//        if (response.isSuccessful()) {
//        List<CctvResponse> cctvResponse = response.body();
//
//        String content = "";
//        for (CctvInfo cctv : cctvList) {
//        content += "관리기관명: " + cctv.getManagementAgency() + "\n";
//        content += "소재지지번주소: " + cctv.getAddress() + "\n";
//        content += "위도: " + cctv.getLatitude() + "\n";
//        content += "경도: " + cctv.getLongitude() + "\n\n";
//        }
//        Log.i("확인용", content);
//        textViewResult.setText("1 / " + cctvList.size());
//        } else {
//        textViewResult.setText("Error: " + response.code());
//        }
//        }
//
//@Override
//public void onFailure(Call<CctvResponse> call, Throwable t) {
//        textViewResult.setText("Error: " + t.getMessage());
//        }
//        });