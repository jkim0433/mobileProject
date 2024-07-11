package com.example.android0027.MyPage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android0027.Login.DataResponse;
import com.example.android0027.Main.MainActivity;
import com.example.android0027.R;
import com.example.android0027.Login.RetrofitClient;
import com.example.android0027.Login.RetrofitService;
import com.example.android0027.Main.ScanQRActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  MyPage extends AppCompatActivity {

    private LinearLayout hiddenLayout;

    Button proBtnQrScanOut, proRestDayBtn, btnShowTmp, bottomScan, bottom_home, btnGoArticle;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ScrollView scrollView;


    private CursorAdapter adapter;
    private CustomSQLHelper customSQLHelper;
    private SQLiteDatabase sqlDB;
    private Cursor cursor;

    // ====profile---------//
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    CircleImageView iv;
    String ID;

    TextView levelTV;

    ProgressBar levelPgBar;

    // 바텀 시트
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Handler handler;
    private Runnable runnable;
//
//    Button btnShowBottomSheet, btnCloseBottomSheet;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        levelPgBar = (ProgressBar) findViewById(R.id.levelPgBar);
        Button btnShowBottomSheet = findViewById(R.id.btnShowTmp);
        btnShowTmp = findViewById(R.id.btnShowTmp);
        bottom_home = findViewById(R.id.btnGoArticle);
        bottomScan = findViewById(R.id.bottomScan);
        btnGoArticle = findViewById(R.id.btnGoArticle);


        // --- 히든 레이아웃 --- //
        hiddenLayout = findViewById(R.id.hiddenLayout);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 스크롤을 내릴 때: 상단 뷰를 위로 숨김
                    slideUp(hiddenLayout);
                }
            }
        });



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
        btnCloseBottomSheet.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        //
        bottomScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(MyPage.this, ScanQRActivity.class);
                intentScan.putExtra("ID", ID);

                startActivity(intentScan);
            }
        });

        btnGoArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(MyPage.this, MainActivity.class);
                intentHome.putExtra("ID", ID);

                startActivity(intentHome);
            }
        });

        // ViewPager 어댑터 설정
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "나의 이용권");
        adapter.addFragment(new Fragment2(), "타이머");
        adapter.addFragment(new Fragment3(), "BMI");
        viewPager.setAdapter(adapter);
//        tabLayout.setTabRippleColor(ColorStateList.valueOf(new ));

        // TabLayout과 ViewPager 연결
        tabLayout.setupWithViewPager(viewPager);

        //Intent 리시버
        Intent inIntent = getIntent();

        ID = (inIntent.getStringExtra("ID"));
        String Time;
        Time = (inIntent.getStringExtra("TIME"));

        //===========================api===========================//

        //로그인을 한 ID= EMAIL을 받는 함수


        TextView proCheckTv = (TextView) findViewById(R.id.proCheckTv);

        TextView proNmTv = (TextView) findViewById(R.id.proNmTv);
        levelTV = (TextView) findViewById(R.id.levelTV);


        RetrofitService retrofitService = RetrofitClient.getRetrofitInstance().create(RetrofitService.class);
        Call<List<DataResponse>> call = retrofitService.getUserData();
        call.enqueue(new Callback<List<DataResponse>>() {
            @Override
            public void onResponse(Call<List<DataResponse>> call, Response<List<DataResponse>> response) {
                if (response.isSuccessful()) {


                    List<DataResponse> dataResponse = response.body();
                    String cotnet = "";

                    for (DataResponse profile : dataResponse) {
                        String userEmailResponse = profile.getUserEmail();

                        assert ID != null;
                        if (ID.equals(userEmailResponse)) {
                            cotnet += profile.getUserNm();
                        }

                    }
                    Log.i("FOR CHECK", cotnet);
                    proNmTv.setText("득근하라" + cotnet);
                } else {
                    proNmTv.setText("EROR" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DataResponse>> call, Throwable t) {
                proNmTv.setText("Errrrrrorrr" + t.getMessage());
            }
        });

        proBtnQrScanOut = (Button) findViewById(R.id.proBtnQrScanOut);

        proBtnQrScanOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, ScanQRActivity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        proRestDayBtn = (Button) findViewById(R.id.proRestDayBtn);
        proRestDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentD = new Intent(MyPage.this, DDay.class);
                intentD.putExtra("ID", ID);
                intentD.putExtra("gNumber", "00");
                startActivityForResult(intentD, 0);
            }
        });


        customSQLHelper = new CustomSQLHelper(this);
        sqlDB = customSQLHelper.getWritableDatabase();



        //===========time을 받아오는 sqlite=============================================//
        // id 중에 가장 최근에 입장한 행
        cursor = sqlDB.rawQuery("SELECT * " +
                "FROM groupTBL " +
                "WHERE ID = '" + ID + "' " +
                "AND CHECK_IN = 1 " +
                "AND CHECK_OUT = 0 " +
                "ORDER BY _id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range")
            long checkInTime = cursor.getLong(cursor.getColumnIndex("CHECK_IN_TIME"));
            String dateForMyPageIn = epochToDate(checkInTime);
            proCheckTv.setText(dateForMyPageIn + "출석!");
        } else {
            //퇴장했다면 가장 최근에 퇴장한 raw가져오기
            cursor = sqlDB.rawQuery("SELECT * FROM groupTBL WHERE ID = '" + ID + "' " +
                    "AND CHECK_IN = 1 " +
                    "AND CHECK_OUT = 1 " +
                    "ORDER BY _id DESC LIMIT 1", null);

            cursor.moveToFirst();

            @SuppressLint("Range")
            long checkOutTime = cursor.getLong(cursor.getColumnIndex("CHECK_OUT_TIME"));
            String dateForMyPageOut = epochToDate(checkOutTime);
            proCheckTv.setText(dateForMyPageOut + "퇴장!");
        }

        cursor = sqlDB.rawQuery("SELECT * FROM groupDD WHERE clickTime = (SELECT MAX(clickTime) FROM groupDD);", null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range")
            String gNumber = cursor.getString(cursor.getColumnIndex("gNumber"));

            proRestDayBtn.setText(gNumber);
        } else {
            proRestDayBtn.setText("000");
        }


        //==profile_image===================================================//
        iv = (CircleImageView) findViewById(R.id.proIv);

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM images WHERE ID = '" + ID + "' ORDER BY _id DESC LIMIT 1;", null);
        if (cursor.moveToFirst()) {

            byte[] imageBytes = cursor.getBlob(1);
            String idForImg = cursor.getString(2);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            iv.setImageBitmap(bitmap);
        } else {
            iv.setImageResource(R.drawable.armicon);
        }


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("야야");
                //갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                activityResultLauncher.launch(intent);

//                Cursor cursor = sqlDB.query("images", new String[]{"image", "ID"}, null, null, null, null, null);

            }
        });

        setupProgressBar(ID);


    }

    // ViewPager 어댑터 클래스
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (sqlDB != null) sqlDB.close();
//        handler.removeCallbacks(runnable);
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
            db.execSQL("DROP TABLE IF EXISTS groupTBL");


            String createTable = "CREATE TABLE groupTBL ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID TEXT, " +
                    "CHECK_IN BOOLEAN, " +
                    "CHECK_IN_TIME LONG, " +
                    "CHECK_OUT BOOLEAN, " +
                    "CHECK_OUT_TIME LONG, " +
                    "CHECK_COUNT INTEGER, " +
                    "TIME INTEGER" +
                    ");";

            String createDDTable = "CREATE TABLE groupDD (" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " gName TEXT, gNumber INTEGER, " +
                    "clickTime LONG);";

            String createImagesTable = "CREATE TABLE images (" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " image TEXT, ID TEXT);";


            db.execSQL(createTable);
            db.execSQL(createDDTable);
            db.execSQL(createImagesTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }

    public String epochToDate(long epochTime) {
        // 에포크 시간을 Date 객체로 변환합니다.
        Date date = new Date(epochTime);

        // 날짜 형식을 지정합니다. 여기서는 "yyyy-MM-dd HH:mm:ss"를 사용합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 날짜 형식에 맞게 문자열로 변환하여 반환합니다.
        return dateFormat.format(date);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    //profile_img=======================================================================//

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == RESULT_OK) {
//                        Intent intent = result.getData();
//                        Uri uri = intent.getData();
//                    }
                    if (result.getResultCode() != RESULT_OK) return;

                    if (result.getResultCode() == RESULT_OK) {
                        // 선택한 이미지 URI 가져오기
                        Uri uri = result.getData().getData();

                        // 이미지 뷰에 이미지 설정
                        try {
                            InputStream is = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);


                            // 이미지를 바이트 배열로 변환
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();

                            // 이미지 바이트 배열을 데이터베이스에 저장
                            ContentValues values = new ContentValues();
                            values.put("ID", ID); // 문자열 ID 저장
                            values.put("image", imageBytes);
                            long rowId = sqlDB.insert("images", null, values);


                            iv.setImageBitmap(bitmap);

                            Toast.makeText(MyPage.this, "이미지 저장 완료", Toast.LENGTH_SHORT).show();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @SuppressLint({"Range", "SetTextI18n"})
    private void setupProgressBar(String ID) {
        // ID 별로 CHECK_COUNT를 합산하는 SQL 쿼리
        levelPgBar.setMax(30);

        String query = "SELECT SUM(CHECK_COUNT) AS TOTAL_CHECK_COUNT FROM groupTBL WHERE ID = '" + ID + "'";
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            // 결과를 ProgressBar에 반영
            int totalCheckCount = cursor.getInt(cursor.getColumnIndex("TOTAL_CHECK_COUNT"));
            levelPgBar.setProgress(totalCheckCount);
            float totalText = ((float) totalCheckCount / 30) * 100;


            @SuppressLint("DefaultLocale")
            String totalTextString = String.format("%.2f", totalText);


            levelTV.setText("이번달!" + totalTextString + "% 출석중");

        }
    }

    private void slideUp(View view) {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        view.startAnimation(slideUpAnimation);
        view.setVisibility(View.INVISIBLE);
    }

//    private void slideDown(View view) {
//        Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//        view.startAnimation(slideDownAnimation);
//        view.setVisibility(View.VISIBLE);
//    }

}

