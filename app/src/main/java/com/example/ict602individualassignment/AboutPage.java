package com.example.ict602individualassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    TextView textView_aboutlink;
    Button about_buttonBackView;
    ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        textView_aboutlink = (TextView) findViewById(R.id.textView_aboutlink);
        textView_aboutlink.setMovementMethod(LinkMovementMethod.getInstance());
        about_buttonBackView = (Button) findViewById(R.id.about_buttonBackView);

        imageProfile = findViewById(R.id.imageView_about);
        imageProfile.setImageResource(R.drawable.profile_picture);

        about_buttonBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}