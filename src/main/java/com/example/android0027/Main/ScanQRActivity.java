package com.example.android0027.Main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.android0027.MyPage.MyPage;
import com.example.android0027.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScanQRActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private TextView txtResult;
    private ImageView qrImg;

    Button btnScan, btnBack_2;
    String ID;

    String currentTime = getCurrentTime();
    long mTMillis = modifyCurrentTimeToEpoch(currentTime);
    long useChecked = useChecked(currentTime); // 현재 시간에서 하루를 뺀


    private CursorAdapter adapter;
    private CustomSQLHelper customSQLHelper;
    private SQLiteDatabase sqlDB;
    private Cursor cursor;

    // Channel에 대한 id 생성 : Channel을 구부하기 위한 ID 이다.
    private static final String CHANNEL_ID = "0";
    private static final int NOTIFICATION_ID = 0;

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        txtResult = findViewById(R.id.txtResult);
        qrImg = findViewById(R.id.qrImg);
        btnScan = findViewById(R.id.btnScan);
        btnBack_2 = findViewById(R.id.btnBack_2);

        customSQLHelper = new CustomSQLHelper(this);
        sqlDB = customSQLHelper.getWritableDatabase();


        //푸쉬 알림 설정
        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(),R.drawable.armicon );



//        PendingIntent mPendingIntent = PendingIntent.getActivity(ScanQRActivity.this, 0,
//                new Intent(getApplicationContext(),MainActivity.class),
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );


        // Channel을 생성 및 전달해 줄 수 있는 Manager 생성

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Test Notification",manager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ScanQRActivity.this,CHANNEL_ID);





        Intent inIntent = getIntent();
        ID = inIntent.getStringExtra("ID");

        // QR 코드 이미지 생성
        generateQRCode("Array");

        // QR 코드 스캔 결과 처리
        String scannedResult = "Array"; // 수동으로 설정
        // "Array"일 때의 처리


        cursor = sqlDB.rawQuery("SELECT * " +
                "FROM groupTBL " +
                "WHERE ID = '" + ID + "' " +
                "AND CHECK_IN_TIME >= "+useChecked+";", null);


        if (cursor.moveToFirst()) {
            long checkInTime = cursor.getLong(cursor.getColumnIndex("CHECK_IN_TIME"));
            int  checkInDuration = (int) ((mTMillis - checkInTime) / (1000 * 60 ));

            String updateQuery = "UPDATE groupTBL SET CHECK_OUT = 1, CHECK_OUT_TIME = ?, TIME = ? WHERE ID = ? AND _id = (SELECT MAX(_id) FROM groupTBL)";
            sqlDB.execSQL(updateQuery, new Object[]{mTMillis, checkInDuration, ID});

            cursor.close();
            Toast.makeText(this, currentTime + "퇴장하였습니다.", Toast.LENGTH_LONG).show();
            txtResult.setText(currentTime + ID +"\n"+"충분한 휴식은 필수");

            //푸쉬 알림

            mBuilder.setSmallIcon(R.drawable.sign_in_logo)
                    .setContentTitle("퇴장!")
                    .setContentText("고생했슈 스트레칭 잊지말구!")
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setLargeIcon(mLargeIconForNoti)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(false);
//                    .setContentIntent(mPendingIntent);

            manager.notify(NOTIFICATION_ID,mBuilder.build());




        } else {
            sqlDB.execSQL("INSERT INTO groupTBL (ID, CHECK_IN, CHECK_IN_TIME, CHECK_OUT, CHECK_OUT_TIME, CHECK_COUNT,TIME)" +
                    " VALUES ( '" + ID + "', 1, "+mTMillis+" , 0, null, 1, null);");
//            refreshData();




            Toast.makeText(this, currentTime + "출석하였습니다.", Toast.LENGTH_LONG).show();
            txtResult.setText(currentTime + ID +"\n"+"오늘 하루도 득근");


            mBuilder.setSmallIcon(R.drawable.sign_in_logo)
                    .setContentTitle("출석")
                    .setContentText("오늘은 어디를 조져볼까유?")
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setLargeIcon(mLargeIconForNoti)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            manager.notify(NOTIFICATION_ID,mBuilder.build());

        }




        Intent intentScanAfter = new Intent(getApplicationContext(), MainActivity.class);
        btnScan.setOnClickListener(v -> {

            intentScanAfter.putExtra("ID", ID);
            intentScanAfter.putExtra("TIME", currentTime);

            startActivity(intentScanAfter);


        });

        btnBack_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack2 = new Intent(ScanQRActivity.this, MyPage.class);
                intentBack2.putExtra("ID", ID);

                startActivity(intentBack2);
            }
        });


    }

    private void generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            qrImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private long modifyCurrentTimeToEpoch(String currentTime) {
        try {
            // 기존의 currentTime을 SimpleDateFormat을 사용하여 Date 객체로 파싱합니다.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = dateFormat.parse(currentTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            // 변경된 시간을 에포크 시간(밀리초)으로 변환하여 반환합니다.
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // 파싱에 실패한 경우 -1을 반환합니다.
        }
    }

    private long useChecked(String currentTime) {
        try {
            // 기존의 currentTime을 SimpleDateFormat을 사용하여 Date 객체로 파싱합니다.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = dateFormat.parse(currentTime);

            // Date 객체에서 3시간 전의 시간을 빼줍니다.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
                                                //퇴장시간 기준 과거 3시간동안 한번이라도 찍었다면
            calendar.add(Calendar.HOUR_OF_DAY, -3);

            // 변경된 시간을 에포크 시간(밀리초)으로 변환하여 반환합니다.
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // 파싱에 실패한 경우 -1을 반환합니다.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (sqlDB != null) sqlDB.close();
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

            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }

}