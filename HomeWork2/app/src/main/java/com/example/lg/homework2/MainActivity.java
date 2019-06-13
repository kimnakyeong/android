package com.example.lg.homework2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lg.homework2.model.QuestionBean;
import com.example.lg.homework2.model.QuestionDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RadioGroup radioGroup1;
    private RadioButton buttonEasy, buttonHard;
    private Button buttonStart;
    private ImageView change;
    private String str;
    private TextView textView;

    private QuestionDBHelper dbHelper;
    private ArrayList<QuestionBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);


        //
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        //읽기 권한 요청
        radioGroup1 = findViewById(R.id.radioGroup1);
        buttonEasy = findViewById(R.id.buttonEasy);
        buttonEasy.setOnClickListener(this);
        buttonHard = findViewById(R.id.buttonHard);
        buttonHard.setOnClickListener(this);
        textView = findViewById(R.id.textView);
        str = "";
        change = findViewById(R.id.adminChange);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuestionListActivity.class);
                startActivity(i);
            }
        });

        dbHelper = new QuestionDBHelper(this, "DB", null, 1);

        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = dbHelper.get();
                if(data.isEmpty()){
                    Toast.makeText(MainActivity.this, "Question is not defined", Toast.LENGTH_LONG).show();
                    return;
                }else if(str != ""){
                    Intent i = new Intent(MainActivity.this, QuizActivity.class);
                    i.putExtra("difficulty",str);
                    startActivity(i);
                    radioGroup1.clearCheck();
                    textView.setText("Difficulty explanation");
                }else if(!buttonEasy.isChecked() && !buttonHard.isChecked()){
                    Toast.makeText(MainActivity.this, "The Difficulty has not been chosen yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String strDifficulty;
        if(v.getId() == R.id.buttonEasy){
            str = "Easy";
            strDifficulty   = "Easy Mode, All question is multiple choice mode";
        }else {
            str = "Hard";
            strDifficulty    = "Hard Mode, Text question is short answer mode";
            }
        textView.setText(strDifficulty);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"Permission allowed.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"permission denied. You should set yourself",Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }

        }
    }
}
