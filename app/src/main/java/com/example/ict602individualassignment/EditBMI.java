package com.example.ict602individualassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class EditBMI extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button buttonSaveEdit, buttonBackEdit;
    EditText inputHeightEdit, inputWeightEdit;
    SQLiteDatabase db;
    String datecreated, strHeight, strWeight, textCategory, textRisk;
    double calHeight, calWeight, calBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bmi);

        dbHelper = new DataHelper(this);
        inputHeightEdit = (EditText) findViewById(R.id.inputHeightEdit);
        inputWeightEdit = (EditText) findViewById(R.id.inputWeightEdit);
        buttonSaveEdit = (Button) findViewById(R.id.buttonSaveEdit);
        buttonBackEdit = (Button) findViewById(R.id.buttonBackEdit);

        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from bmi where datecreated = '" + getIntent().getStringExtra("datecreated") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            datecreated = cursor.getString(1).toString();
            inputHeightEdit.setText(cursor.getString(2).toString());
            inputWeightEdit.setText(cursor.getString(3).toString());

            buttonSaveEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strHeight = inputHeightEdit.getText().toString().replace(" ", "");
                    strWeight = inputWeightEdit.getText().toString().replace(" ", "");

                    if (TextUtils.isEmpty(strHeight) || TextUtils.isEmpty(strWeight)) {
                        Toast.makeText(getApplicationContext(), "Please fill in the form", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.equals(strHeight, "0") || TextUtils.equals(strWeight, "0")) {
                        Toast.makeText(getApplicationContext(), "0 value are not allowed", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.equals(strHeight, ".") || TextUtils.equals(strWeight, ".") || TextUtils.equals(strHeight, ",") || TextUtils.equals(strWeight, ",") || TextUtils.equals(strHeight, "-") || TextUtils.equals(strWeight, "-")) {
                        Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                    else if (!Pattern.matches("[0-9]+", strHeight) || !Pattern.matches("[0-9]+", strWeight)) {
                        Toast.makeText(getApplicationContext(), "Only number is allowed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        calHeight = Double.parseDouble(strHeight) / 100;
                        calWeight = Double.parseDouble(strWeight);

                        calBMI = calWeight / (calHeight * calHeight);
                        calBMI = Math.round(calBMI * 10.0) / 10.0;

                        if (calBMI <= 18.4) {
                            textCategory = "Underweight";
                            textRisk = "Malnutrition risk";
                        }
                        else if (calBMI >= 18.5 && calBMI <= 24.9) {
                            textCategory = "Normal weight";
                            textRisk = "Low risk";
                        }
                        else if (calBMI >= 25 && calBMI <= 29.9) {
                            textCategory = "Overweight";
                            textRisk = "Enhanced risk";
                        }
                        else if (calBMI >= 30 && calBMI <= 34.9) {
                            textCategory = "Moderately obese";
                            textRisk = "Medium risk";
                        }
                        else if (calBMI >= 35 && calBMI <= 39.9) {
                            textCategory = "Severely obese";
                            textRisk = "High risk";
                        }
                        else if (calBMI >= 40) {
                            textCategory = "Very severely obese";
                            textRisk = "Very high risk";
                        }
                        else {
                            textCategory = "Oh my";
                            textRisk = "Why this happen";
                        }

                        db = dbHelper.getWritableDatabase();
                        db.execSQL("update bmi set height = '" + strHeight + "', weight = '" + strWeight + "', category = '" + textCategory + "', range = '" + calBMI + "', healthrisk = '" + textRisk + "' where datecreated = '" + datecreated + "'");
                        Toast.makeText(getApplicationContext(), "BMI Record Updated", Toast.LENGTH_SHORT).show();
                        MainActivity.ma.RefreshList();
                        finish();
                    }

                }
            });
        }

        buttonBackEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}