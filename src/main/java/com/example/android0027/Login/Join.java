package com.example.android0027.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android0027.R;

public class Join extends AppCompatActivity {
    private String TAG = Join.class.getSimpleName();

    private WebView webView = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        webView = (WebView) findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());

        webView.setDownloadListener(new DownloadListener(){
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }});  // 파일 다운로드 설정


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 특정 페이지의 URL을 체크합니다.
                if (url.contains("http://192.168.50.159:8081/WelcomeGymFit/login")) {
                    // 특정 페이지로 이동할 때 특정 인텐트를 실행합니다.
                    Intent intent = new Intent(Join.this, LoginActive.class);
                    startActivity(intent);
                    // 페이지 이동을 WebView가 처리하도록 false를 반환합니다.
                    return false;
                } else {
                    // 특정 페이지가 아닌 경우에는 WebView가 처리하도록 true를 반환합니다.
                    view.loadUrl(url);
                    return true;
                }
            }
        });

        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부

        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
//        webview.addJavascriptInterface(new AndroidBridge(), "android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부
//
        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출 // https://eunoia3jy.tistory.com/16(세부설정)
//        webView.loadUrl("http://www.naver.com");
        webView.loadUrl( "http://192.168.50.159:8081/WelcomeGymFit/new-user");

    }
}
