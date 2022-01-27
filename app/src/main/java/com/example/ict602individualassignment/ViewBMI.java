package com.example.ict602individualassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewBMI extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button buttonBackView;
    TextView textHeightView, textWeightView, textCategoryView, textRangeView, textHealthRIsk, textDateTimeView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bmi);

        dbHelper = new DataHelper(this);
        textHeightView = (TextView) findViewById(R.id.textHeightView);
        textWeightView = (TextView) findViewById(R.id.textWeightView);
        textCategoryView = (TextView) findViewById(R.id.textCategoryView);
        textRangeView = (TextView) findViewById(R.id.textRangeView);
        textHealthRIsk = (TextView) findViewById(R.id.textHealthRIsk);
        textDateTimeView = (TextView) findViewById(R.id.textDateTimeView);
        buttonBackView = (Button) findViewById(R.id.buttonBackView);

        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM bmi WHERE datecreated = '" + getIntent().getStringExtra("datecreated") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            textHeightView.setText(cursor.getString(2).toString() + " cm");
            textWeightView.setText(cursor.getString(3).toString() + " kg");
            textCategoryView.setText(cursor.getString(4).toString());
            textRangeView.setText(cursor.getString(5).toString() + " kg/m\u00B2");
            textHealthRIsk.setText(cursor.getString(6).toString());
            textDateTimeView.setText("Recorded on " + cursor.getString(1).toString().replace("|", "at"));

            buttonBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }
    }
}