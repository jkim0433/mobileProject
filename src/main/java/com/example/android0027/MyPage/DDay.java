package com.example.android0027.MyPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.android0027.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DDay extends AppCompatActivity {
    private CustomSQLHelper customSQLHelper;
    private SQLiteDatabase sqlDB;
    private Cursor cursor;
    private MyCursorAdapter adapter;


    Button btnInit;
    Button btnInsert;
    Button btnSelect ;
    Button btnUpdate ;
    Button btnDelete ;
    Button edtDateBtn;
    HorizontalScrollView scrollViewD;
    EditText edtName;
    EditText edtNumber ;
    ListView listView ;


    private TextView setDDay;
    String ID = "";
    String gNumber1 = "";

    String currentTime = getCurrentTime();
    long mTMillis = modifyCurrentTimeToEpoch(currentTime);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dday);

        //import
         btnInit = findViewById(R.id.btnInit);
         btnInsert = findViewById(R.id.btnInsert);
         btnSelect = findViewById(R.id.btnSelect);
         btnUpdate = findViewById(R.id.btnUpdate);
         btnDelete = findViewById(R.id.btnDelete);
         edtDateBtn = findViewById(R.id.edtDateBtn);
         scrollViewD = findViewById(R.id.scrollView);
         edtName = findViewById(R.id.edtName);
         edtNumber = findViewById(R.id.edtNumber);
         listView = findViewById(R.id.listView);



        customSQLHelper = new CustomSQLHelper(this);
        adapter = new MyCursorAdapter(this, cursor, 0);



        listView.setAdapter(adapter);

        View headerView = getLayoutInflater().inflate(R.layout.list_header, null);
        listView.addHeaderView(headerView);



        refreshData();

        Intent inIntent = getIntent();
        ID = (inIntent.getStringExtra("ID"));
        gNumber1 = (inIntent.getStringExtra("gNumber"));

        //button
        btnInit.setOnClickListener(v -> {
            sqlDB = customSQLHelper.getWritableDatabase();
            customSQLHelper.onUpgrade(sqlDB, 1, 2); // 뒤의 버전은 버전 구분 정보가 필요할때 활용
            sqlDB.close();

            refreshData();
        });

        btnInsert.setOnClickListener(v -> {
            sqlDB = customSQLHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO groupDD (gName, gNumber, clickTime) VALUES ( '" + edtName.getText().toString() + "', '" + edtNumber.getText().toString() + "','');");
            sqlDB.close();
            edtName.setText("");
            edtNumber.setText("");
            refreshData();
            scrollViewD.post(new Runnable() {
                @Override
                public void run() {
                    scrollViewD.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            });
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = customSQLHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE  FROM groupDD WHERE gName = '"+edtName.getText().toString()+"' ;");
                sqlDB.close();
                btnSelect.callOnClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = customSQLHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupDD SET gNumber = '"+ edtNumber.getText().toString()+"' ," +
                        " gName = '"+edtName.getText().toString()+"' , clickTime = '"+mTMillis+"' WHERE gName = '"+edtName.getText().toString()+"' ;");
                sqlDB.close();
                btnSelect.callOnClick();
            }
        });

        btnSelect.setOnClickListener(v -> {
            refreshData();
        });


        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Log.i("보자보자", "position : " + String.valueOf(position));
            Log.i("보자보자", "id : " + String.valueOf(id));
            sqlDB = customSQLHelper.getWritableDatabase();
            Cursor cursor = sqlDB.rawQuery("SELECT gName, gNumber FROM groupDD WHERE _id = ?",
                    new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                String gName = cursor.getString(0);
                String gNumber = cursor.getString(1);

                // 가져온 값을 EditText에 설정합니다.
                edtName.setText(gName);
                edtNumber.setText(gNumber);
            }
            sqlDB.close();

            edtDateBtn.callOnClick();

            refreshData();
            return false;

        });

        edtDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new DatePickerFragment();
                ((DatePickerFragment) dialogFragment).setEditText(edtNumber);
                dialogFragment.show(getSupportFragmentManager(), "datePicker");


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (sqlDB != null) sqlDB.close();
    }

    private void refreshData() {
        sqlDB = customSQLHelper.getWritableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM groupDD", null);
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
            db.execSQL("DROP TABLE IF EXISTS groupDD");
            db.execSQL("CREATE TABLE groupDD ( _id INTEGER PRIMARY KEY AUTOINCREMENT, gName TEXT, gNumber TEXT, clickTime LONG);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupDD");
            onCreate(db);
        }
    }

    public class MyCursorAdapter extends CursorAdapter {
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // 새로운 아이템 뷰를 생성하는 메서드
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_item, parent, false);

            setDDay = view.findViewById(R.id.setDDay);
            TextView gName = view.findViewById(R.id.gName);
            TextView gNumber = view.findViewById(R.id.gNumber);


            setDDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.callOnClick();
                    btnUpdate.callOnClick();

                    Intent outIntent = new Intent(DDay.this, MyPage.class);
                    outIntent.putExtra("ID",ID);
                    startActivity(outIntent);



                }
            });

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // 아이템 뷰에 데이터를 바인딩하는 메서드
            TextView tvSeq = view.findViewById(R.id.tvSeq);
            TextView gName = view.findViewById(R.id.gName);
            TextView gNumber = view.findViewById(R.id.gNumber);

            int seqId = 0;
            String strName = null;
            String iNum = null;
            seqId = cursor.getInt(0);
            strName = cursor.getString(1);
            iNum = cursor.getString(2);

//            try {
//                seqId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
//                strName = cursor.getString(cursor.getColumnIndexOrThrow("gName"));
//                iNum = cursor.getInt(cursor.getColumnIndexOrThrow("gNumber"));
//            } catch (IllegalArgumentException e) {
//            }

            tvSeq.setText(String.valueOf(seqId));
            gName.setText(strName);
            gNumber.setText(iNum);
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




}
