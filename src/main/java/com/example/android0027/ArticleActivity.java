package com.example.android0027;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android0027.Article.ArticleAdapter;
import com.example.android0027.Article.ArticleClient;
import com.example.android0027.Article.ArticleResponse;
import com.example.android0027.Article.ArticleSevice;
import com.example.android0027.Article.CommentActivity;
import com.example.android0027.Main.MainActivity;
import com.example.android0027.Main.ScanQRActivity;
import com.example.android0027.MyPage.MyPage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticleActivity extends AppCompatActivity {
    private ListView listView;
    //======sql================================//
    private CursorAdapter adapter;
    private MainActivity.CustomSQLHelper customSQLHelper;
    private SQLiteDatabase sqlDB;
    private Cursor cursor;


    //======================lineChart=====================================//
    LineChart lineChart;
    Button btnBack_2;
    EditText xEditText, yEditText;
    Button btnInsert, btnShow;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData lineData;
    //===================================retrofit====================//
    List<ArticleResponse>  articleResponseList = new ArrayList<>();

    private ArticleAdapter Aadapter;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        Intent inIntent = getIntent();
        ID = inIntent.getStringExtra("ID");

        //==================SQLITE===========================//

        customSQLHelper = new MainActivity.CustomSQLHelper(this);
        sqlDB = customSQLHelper.getWritableDatabase();



        //==================CHART===========================//

        lineChart = findViewById(R.id.line_chart);
        btnShow = findViewById(R.id.btnShow);
        btnBack_2 = findViewById(R.id.btnBack_2);


        exqShowBtn();

        lineDataSet.setLineWidth(4);

        LineDataSet lineDataSet1 = new LineDataSet(data1(), "Data set1");
        LineDataSet lineDataSet2 = new LineDataSet(data2(), "Data set2");
        LineDataSet lineDataSet3 = new LineDataSet(data3(), "Data set3");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);


        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setNoDataText("NO DATA");
        lineChart.setDrawGridBackground(true);

//        lineChart.getXAxis().setValueFormatter(new DateAxisValueFormatter());

        Description description = new Description();
        description.setPosition(150f, 15f);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
        legend.setForm(Legend.LegendForm.CIRCLE);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.invalidate();

       //==================Article===========================//

        listView = findViewById(R.id.listView);

        ArticleSevice articleSevice = ArticleClient.getArticleInstance().create(ArticleSevice.class);
        Call<List<ArticleResponse>> call = articleSevice.getArticleData();
        call.enqueue(new Callback<List<ArticleResponse>>() {
            @Override
            public void onResponse(Call<List<ArticleResponse>> call, Response<List<ArticleResponse>> response) {
                if (response.isSuccessful()) {
                    List<ArticleResponse> articleResponses = response.body();

                    Aadapter = new ArticleAdapter(ArticleActivity.this, articleResponses);
                    listView.setAdapter(Aadapter);


                }else{
                    Toast.makeText(getApplicationContext(),
                            "Error: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ArticleResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleResponse article = (ArticleResponse) adapter.getItem(position);
                // 클릭한 게시글에 대한 정보(article)를 사용하여 원하는 작업 수행
                Intent intent = new Intent(ArticleActivity.this, CommentActivity.class );
                // 예를 들어, 다른 액티비티로 이동하거나 다이얼로그를 표시하는 등의 동작을 수행할 수 있습니다.

                // 여기서는 예시로 Toast 메시지를 표시하는 것으로 구현합니다.

                Toast.makeText(ArticleActivity.this, "Clicked on article: " + article.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void exqShowBtn() {
        // 30일간의 날짜 생성
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.getDefault());
        for (int i = 0; i < 30; i++) {
            dates.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineDataSet.setValues(getDataValues());
                dataSets.clear();
                dataSets.add(lineDataSet);
                lineData = new LineData(dataSets);
                lineChart.setVisibility(View.VISIBLE);

// X축 설정
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return dates.get((int) value) + "일";
                    }
                });
                xAxis.setLabelCount(8, true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                lineChart.clear();
                lineChart.setData(lineData);
                lineChart.invalidate();
            }
        });

        btnBack_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMypage = new Intent(ArticleActivity.this, MyPage.class);
                intentMypage.putExtra("ID", ID);

                startActivity(intentMypage);
            }
        });

    }


    @SuppressLint("Recycle")
    private ArrayList<Entry> getDataValues() {
        ArrayList<Entry> dataVals = new ArrayList<>();
        String[] columns = {"CHECK_OUT_TIME", "TIME"};
        Cursor cursor = sqlDB.query("groupTBL", columns,
                "ID=?", new String[]{ID}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long xFl = cursor.getLong(0);
                String x = epochToDate(xFl);
                String Ym = x.substring(0, 6);


                int num2 = Integer.parseInt(Ym) * 100;
                int num = Integer.parseInt(x) - num2;
                Entry entry = new Entry(num, cursor.getLong(1));

                dataVals.add(entry);
            } while (cursor.moveToNext());
        } else {
            lineChart.setVisibility(View.INVISIBLE);
            Toast.makeText(ArticleActivity.this, "운동을 시작도 안한거야? 시작하면 보여줄께!",
                    Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        return dataVals;
    }

    public String epochToDate(long epochTime) {
        // 에포크 시간을 Date 객체로 변환합니다.
        Date date = new Date(epochTime);

        // 날짜 형식을 지정합니다. 여기서는 "yyyy-MM-dd HH:mm:ss"를 사용합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // 날짜 형식에 맞게 문자열로 변환하여 반환합니다.
        return dateFormat.format(date);
    }

    private ArrayList<Entry> data1() {
        ArrayList<Entry> dataList = new ArrayList<>();

        dataList.add(new Entry(0, 10));
        dataList.add(new Entry(1, 20));
        dataList.add(new Entry(2, 30));
        dataList.add(new Entry(2, 30));
        dataList.add(new Entry(3, 40));

        return dataList;
    }

    private ArrayList<Entry> data2() {
        ArrayList<Entry> dataList = new ArrayList<>();

        dataList.add(new Entry(0, 15));
        dataList.add(new Entry(1, 25));
        dataList.add(new Entry(3, 35));
        dataList.add(new Entry(5, 45));

        return dataList;
    }

    private ArrayList<Entry> data3() {
        ArrayList<Entry> dataList = new ArrayList<>();

        dataList.add(new Entry(0, 28));
        dataList.add(new Entry(3, 38));
        dataList.add(new Entry(6, 48));
        dataList.add(new Entry(9, 58));

        return dataList;
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

            String createGroupDD ="CREATE TABLE IF NOT EXISTS groupDD ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "gName TEXT, gNumber INTEGER, clickTime LONG);";


            String createImages =" CREATE TABLE IF NOT EXISTS images ( " +
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
}
