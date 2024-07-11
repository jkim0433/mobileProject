package com.example.android0027.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android0027.Main.MainActivity;
import com.example.android0027.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActive extends AppCompatActivity {


    EditText loginEmail, loginPW;
    Button login;
    TextView join;

    List<DataResponse> dataList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPW = findViewById(R.id.loginPW);
        login = findViewById(R.id.login);
        join = findViewById(R.id.join);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RetrofitService retrofitService = RetrofitClient.getRetrofitInstance().create(RetrofitService.class);
                Call<List<DataResponse>> call = retrofitService.getUserData();
                call.enqueue(new Callback<List<DataResponse>>() {
                    @Override
                    public void onResponse(Call<List<DataResponse>> call, Response<List<DataResponse>> response) {
                        if (response.isSuccessful()) {
                            List<DataResponse> dataResponse = response.body();
                            String userEmailInput = loginEmail.getText().toString().trim();
                            String userPWInput = loginPW.getText().toString().trim();


                            for (DataResponse profile : dataResponse) {
                                String userEmailResponse = profile.getUserEmail();
                                String userPWResponse = profile.getUserCno();
                                //비밀번호 를 사용할수 있게 됬음 좋겠다.

                                if (userEmailInput.equals(userEmailResponse) && userPWInput.equals(userPWResponse)) {
                                    // ID와 비밀번호가 일치하는 경우 MainActivity로 이동
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("ID", userEmailResponse);
                                    startActivity(intent);
                                    return;
                                }
                            }
                            // ID 또는 비밀번호가 일치하지 않는 경우
                            Toast.makeText(getApplicationContext(), "너 누구지? 내 동료가 아니군!", Toast.LENGTH_SHORT).show();
                            //토스트 메세지 커스텀하기
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DataResponse>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActive.this, Join.class);
                startActivity(intent);
            }
        });
    }













































}


