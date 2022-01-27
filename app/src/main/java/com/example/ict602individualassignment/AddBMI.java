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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AddBMI extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button buttonSave, buttonBack;
    EditText height, weight;
    String strHeight, strWeight, textCategory, textRisk, formatDateTime;
    double calHeight, calWeight, calBMI;
    LocalDateTime currentDateTime;
    DateTimeFormatter formatting;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bmi);

        dbHelper = new DataHelper(this);
        height = (EditText) findViewById(R.id.inputHeight);
        weight = (EditText) findViewById(R.id.inputWeight);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        currentDateTime = LocalDateTime.now();
        formatting = DateTimeFormatter.ofPattern("dd LLL yyyy | hh:mm:ss a");
        formatDateTime = currentDateTime.format(formatting);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                strHeight = height.getText().toString().replace(" ", "");
                strWeight = weight.getText().toString().replace(" ", "");

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
                    db.execSQL("insert into bmi(no, datecreated, height, weight, category, range, healthrisk) values(null, '" + formatDateTime + "', '" + strHeight + "', '" + strWeight + "', '" + textCategory + "', '" + calBMI + "', '" + textRisk + "')");
                    Toast.makeText(getApplicationContext(), "BMI Record Created", Toast.LENGTH_SHORT).show();
                    MainActivity.ma.RefreshList();
                    finish();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}