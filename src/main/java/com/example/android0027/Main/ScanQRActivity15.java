package com.example.android0027.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android0027.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScanQRActivity15 extends AppCompatActivity {
    private TextView txtResult;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        txtResult = findViewById(R.id.txtResult);

        new IntentIntegrator(this).initiateScan();
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
                String currentTime = getTime();


            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo


                //                //barcode value ==> index(0) , (1) , (2)
//
//                // Str, Int, Long,
//                String fd =  result.getContents();
//                //db isert
//                sqlDB = customSQLHelper.getWritableDatabase();
//                sqlDB.execSQL("INSERT INTO groupDD (gName, gNumber, clickTime) VALUES ( '" + fd.getText().toString() + "', '" + edtNumber.getText().toString() + "','');");
//                sqlDB.close();


                if (result.getContents().equals("Array")) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    //QR스캔 성공시 할 것들

                    //출석 처리

                    long currentTimeMillis = System.currentTimeMillis();
                    txtResult.setText((int) currentTimeMillis);
                } else {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    txtResult.setText(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void sendAttendanceToServer() {
//        // Retrofit을 사용하여 출석 여부를 서버로 전송하는 API 호출
//        RetrofitService retrofitService = RetrofitClient.getRetrofitInstance().create(RetrofitService.class);
//        Call<Void> call = retrofitService.sendAttendance();
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    // 성공적으로 출석 정보가 서버로 전송됨
//                    Toast.makeText(ScanQRActivity.this, "Attendance sent successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    // 출석 정보 전송에 실패함
//                    Toast.makeText(ScanQRActivity.this, "Failed to send attendance", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // 통신 실패
//                Toast.makeText(ScanQRActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}