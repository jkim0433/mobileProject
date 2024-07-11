package com.example.android0027.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.android0027.ArticleActivity;
import com.example.android0027.Login.LoginActive;
import com.example.android0027.MyPage.MyPage;
import com.example.android0027.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Button gpsRef, btnScan, btnBack, bottomScan, btnGoArticle;

    TextView tvGetTime, tvCheck, textView;

    private LocationManager locationManager;

    private TextView tvLocation;

    private CursorAdapter adapter;
    private CustomSQLHelper customSQLHelper;
    private SQLiteDatabase sqlDB;
    private Cursor cursor;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    private Handler handler;
    private Runnable runnable;

    //-=========================chart==========================//
//    LineChart lineChart;
//    EditText xEditText, yEditText;
//    Button btnShow;
//
//
//    LineDataSet lineDataSet = new LineDataSet(null, null);
//    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//    LineData lineData;


    String ID;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowBottomSheet = findViewById(R.id.btnShowTmp); //FIXME

        gpsRef = (Button) findViewById(R.id.gpsRefresh);
        btnScan = (Button) findViewById(R.id.btnScan);
        ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        tvLocation = findViewById(R.id.tvLocation);
        tvGetTime = (TextView) findViewById(R.id.tvGetTime);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        bottomScan = findViewById(R.id.bottomScan);
        textView = findViewById(R.id.textView);
        btnBack = findViewById(R.id.btnBack);
        tvCheck = findViewById(R.id.tvCheck);
        btnGoArticle = (Button)findViewById(R.id.btnGoArticle);

        //==================SQLITE================================================================================================================//
        customSQLHelper = new CustomSQLHelper(this);
        sqlDB = customSQLHelper.getWritableDatabase();


        //---- 바텀 시트-----//
        View bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        handler = new Handler();
        runnable = () -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnShowBottomSheet.setVisibility(View.VISIBLE);
        };

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        handler.postDelayed(runnable, 5000);

        btnShowBottomSheet.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnShowBottomSheet.setVisibility(View.INVISIBLE);
        });
        handler.postDelayed(runnable, 5000);



        Button btnCloseBottomSheet = findViewById(R.id.btnCloseBottomSheet);
//        btnCloseBottomSheet.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    btnShow.setVisibility(View.VISIBLE);

        btnCloseBottomSheet.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        });


        //---로그인 한 ID 넘기는 송전탑------///
        Intent inIntent = getIntent();
        ID = inIntent.getStringExtra("ID");
        tvGetTime.setText(ID);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);


        btnGoArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });   // 이 버튼 추가해주세요! xml에도 같은 id로 만들어서 넣어주세요!

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(MainActivity.this, LoginActive.class);

                startActivity(intentBack);
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(MainActivity.this, ScanQRActivity.class);
                intentScan.putExtra("ID", ID);
                iv1.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.INVISIBLE);
                tvCheck.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                gpsRef.setVisibility(View.INVISIBLE);

                startActivity(intentScan);
            }
        });

        bottomScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(MainActivity.this, ScanQRActivity.class);
                intentScan.putExtra("ID", ID);

                startActivity(intentScan);
            }
        });


//        Animation moveAni = AnimationUtils.loadAnimation(this,R.anim.rotate);

        gpsRef.setOnClickListener(v -> {
            tvLocation.setText("바뀌는중");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);

        });


        //================go My profile =====//
        Button myPage = (Button) findViewById(R.id.myPage);
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Time;
                Time = (inIntent.getStringExtra("TIME"));


                Intent intentMypage = new Intent(getApplicationContext(), MyPage.class);
                intentMypage.putExtra("ID", ID);

                intentMypage.putExtra("TIME", Time);

                startActivity(intentMypage);

            }
        });
//




        //==============FCM=======================================//
        Intent intent = getIntent();
        if (intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if (notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println(token);
//                        Toast.makeText(MainActivity.this, "Your device registration token is" + token
//                                , Toast.LENGTH_SHORT).show();

                    }
                });


        //========================================chart=========================//

//        btnShow = findViewById(R.id.btnShow);
//
//        exqShowBtn();
//
//        lineDataSet.setLineWidth(4);
//
//        LineDataSet lineDataSet1 = new LineDataSet(data1(), "Data set1");
//        LineDataSet lineDataSet2 = new LineDataSet(data2(), "Data set2");
//        LineDataSet lineDataSet3 = new LineDataSet(data3(), "Data set3");
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(lineDataSet1);
//        dataSets.add(lineDataSet2);
//        dataSets.add(lineDataSet3);
//
//
//        lineChart.setBackgroundColor(Color.WHITE);
//        lineChart.setNoDataText("NO DATA");
//        lineChart.setDrawGridBackground(true);
//
//        lineChart.getXAxis().setValueFormatter(new DateAxisValueFormatter());
//
//        Description description = new Description();
//        description.setPosition(150f, 15f);
//
//        Legend legend = lineChart.getLegend();
//        legend.setEnabled(true);
//        legend.setTextColor(Color.BLACK);
//        legend.setForm(Legend.LegendForm.CIRCLE);
//
//        LineData data = new LineData(dataSets);
//
//        lineChart.setData(data);
//        lineChart.invalidate();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
            }
        }
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double SEOUL_LATITUDE = 37.5665;
    private static final double SEOUL_LONGITUDE = 126.9780;
    private static final double RADIUS_KM = 0.1;


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        tvLocation.setText("위도: " + latitude + ", 경도: " + longitude);

        // 사용자의 현재 위치와 설정된 GPS 범위 내에 있는지 여부를 확인합니다.
        boolean isInRadius = isInRadius(latitude, longitude, SEOUL_LATITUDE, SEOUL_LONGITUDE, RADIUS_KM);
        if (isInRadius) {
            // 설정된 범위 내에 사용자가 있는 경우
            // 여기에 원하는 작업을 수행합니다.
            btnScan.setClickable(true);


        } else {
            // 설정된 범위 밖에 사용자가 있는 경우
            // 여기에 다른 작업을 수행합니다.
            btnScan.setClickable(false);
        }
    }
    // 두 GPS 좌표 간의 거리를 계산하는 메서드입니다.

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    // 사용자의 현재 위치가 설정된 GPS 범위 내에 있는지 여부를 확인하는 메서드입니다.
    private static boolean isInRadius(double userLatitude, double userLongitude, double centerLatitude, double centerLongitude, double radiusKm) {
        double distance = distance(userLatitude, userLongitude, centerLatitude, centerLongitude);
        return distance <= radiusKm;
    }


    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    //==========================sqlite==========================//
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (sqlDB != null) sqlDB.close();
        handler.removeCallbacks(runnable);
    }

    private void refreshData() {
        sqlDB = customSQLHelper.getWritableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL", null);
        adapter.changeCursor(cursor);
        sqlDB.close();
    }

    public static class CustomSQLHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "groupDB";
        private static final int DATABASE_VERSION = 1;

        public CustomSQLHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String createGroupTBL = "CREATE TABLE IF NOT EXISTS groupTBL ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID TEXT, " +
                    "CHECK_IN BOOLEAN, " +
                    "CHECK_IN_TIME LONG, " +
                    "CHECK_OUT BOOLEAN, " +
                    "CHECK_OUT_TIME LONG, " +
                    "CHECK_COUNT INTEGER, " +
                    "TIME INTEGER" +
                    ");";

            String createGroupDD = "CREATE TABLE IF NOT EXISTS groupDD ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "gName TEXT, gNumber INTEGER, clickTime LONG);";


            String createImages = " CREATE TABLE IF NOT EXISTS images ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " image TEXT, ID TEXT);";

            db.execSQL(createGroupTBL);
            db.execSQL(createGroupDD);
            db.execSQL(createImages);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            db.execSQL("DROP TABLE IF EXISTS groupDD");
            db.execSQL("DROP TABLE IF EXISTS images");
            onCreate(db);
        }
    }


    //========================================chart=========================//
//    private void exqShowBtn() {
//        // 30일간의 날짜 생성
//        List<String> dates = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.getDefault());
//        for (int i = 0; i < 30; i++) {
//            dates.add(sdf.format(calendar.getTime()));
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
//
//        btnShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lineDataSet.setValues(getDataValues());
//                dataSets.clear();
//                dataSets.add(lineDataSet);
//                lineData = new LineData(dataSets);
//                lineChart.setVisibility(View.VISIBLE);
//
//// X축 설정
//                XAxis xAxis = lineChart.getXAxis();
//                xAxis.setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value) {
//                        return dates.get((int) value) + "일";
//                    }
//                });
//                xAxis.setLabelCount(8, true);
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                lineChart.clear();
//                lineChart.setData(lineData);
//                lineChart.invalidate();
//            }
//        });
//    }


//    @SuppressLint("Recycle")
//    private ArrayList<Entry> getDataValues() {
//        ArrayList<Entry> dataVals = new ArrayList<>();
//        String[] columns = {"CHECK_OUT_TIME", "TIME"};
//        Cursor cursor = sqlDB.query("groupTBL", columns,
//                "ID=?", new String[]{ID}, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                long xFl = cursor.getLong(0);
//                String x = epochToDate(xFl);
//                String Ym = x.substring(0, 6);
//
//
//                int num2 = Integer.parseInt(Ym) * 100;
//                int num = Integer.parseInt(x) - num2;
//                Entry entry = new Entry(num, cursor.getLong(1));
//
//                dataVals.add(entry);
//            } while (cursor.moveToNext());
//        } else {
//            lineChart.setVisibility(View.INVISIBLE);
//            Toast.makeText(MainActivity.this, "운동을 시작도 안한거야? 시작하면 보여줄께!",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        cursor.close();
//        return dataVals;
//    }


//    @SuppressLint("Recycle")
//    private ArrayList<Entry> getDataValues() {
//        ArrayList<Entry> dataVals = new ArrayList<>();
//        String[] columns = {"CHECK_OUT_TIME", "TIME"};
//        Cursor cursor = sqlDB.query("groupTBL", columns,
//                "ID=?", new String[]{ID}, null, null, null);
//        String x;
//        String Ym;
//        int num2 =0;
//        int num =0;
//
//
//        if(cursor.moveToFirst()){
//
//            int count= cursor.getCount();
//
//            if(count <= 0){
//                    long xFl = cursor.getLong(0);
//                    System.out.println(xFl);
//                     x = epochToDate(xFl);
//                    Ym = x.substring(0,6);
//                     num2 = Integer.parseInt(Ym)*100;
//                     num = Integer.parseInt(x) - num2;
//            }else {
//
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    cursor.moveToNext();
//                    long xFl = cursor.getLong(0);
//                    System.out.println(xFl);
//                    x = epochToDate(xFl);
//
//                    Ym = x.substring(0,6);
//
//                    num2 = Integer.parseInt(Ym)*100;
//
//                    num = Integer.parseInt(x) - num2;
//                }
//
//            }
//            Entry entry = new Entry(num, cursor.getLong(1));
//            dataVals.add(entry);
//        }else {
//            lineChart.setVisibility(View.INVISIBLE);
//            Toast.makeText(MainActivity.this,"운동을 시작도 안한거야? 시작하면 보여줄께!",
//                    Toast.LENGTH_SHORT).show();
//        }
//        cursor.close();
//        return dataVals;
//    }





}