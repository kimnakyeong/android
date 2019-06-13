package com.example.lg.homework2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lg.homework2.model.QuestionBean;
import com.example.lg.homework2.model.QuestionDBHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class QuestionActivity extends AppCompatActivity {

    private int[] layoutIds = {R.id.layoutText, R.id.layoutImage};
    private ConstraintLayout[] layouts;
    private ToggleButton buttonToggle;
    RadioGroup radioGroup2_1, radioGroup2_2, radioGroup1; // radioGroup 부분
    RadioButton answerImage1,answerImage2,answerImage3,answerImage4; // image 부분 radio
    RadioButton firstRadio, secondRadio, thirdRadio, forthRadio; // text 부분 radio
    ImageView imag1,imag2,imag3,imag4;
    int image, answer=0;
    String type=QuestionBean.TYPE_TEXT;

    // database 관련
    private int qid;
    private QuestionBean question;
    private QuestionDBHelper dbHelper;

    private EditText questionEdit;
    private EditText scoreEdit;
    private EditText firstEdit, secondEdit, thirdEdit, forthEdit;

    TextView questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

         // log
        questionText = findViewById(R.id.questionText);
        //

        questionEdit = findViewById(R.id.questionEdit);
        scoreEdit = findViewById(R.id.scoreEdit);
        firstEdit = findViewById(R.id.firstEdit);
        secondEdit = findViewById(R.id.secondEdit);
        thirdEdit = findViewById(R.id.thirdEdit);
        forthEdit = findViewById(R.id.forthEdit);

        dbHelper = new QuestionDBHelper(this, "DB", null, 1);

        radioGroup1 = findViewById(R.id.radioGroup1);
        View.OnClickListener listenerText = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.firstRadio){
                    answer = 1;
                }else  if(v.getId() == R.id.secondRadio){
                    answer = 2;
                }else  if(v.getId() == R.id.thirdRadio){
                    answer = 3;
                }else {
                    answer = 4;
                }
            }
        };
        firstRadio = findViewById(R.id.firstRadio);
        firstRadio.setOnClickListener(listenerText);
        secondRadio = findViewById(R.id.secondRadio);
        secondRadio.setOnClickListener(listenerText);
        thirdRadio = findViewById(R.id.thirdRadio);
        thirdRadio.setOnClickListener(listenerText);
        forthRadio = findViewById(R.id.forthRadio);
        forthRadio.setOnClickListener(listenerText);

        radioGroup2_1 = findViewById(R.id.radioGroup2_1);
        radioGroup2_2 = findViewById(R.id.radioGroup2_2);
        View.OnClickListener listenerImage = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.answerImage1 || v.getId() == R.id.answerImage2){
                    radioGroup2_2.clearCheck();
                    if(v.getId() == R.id.answerImage1) answer = 1;
                    else answer = 2;
                }else {
                    radioGroup2_1.clearCheck();
                    if(v.getId() == R.id.answerImage3) answer = 3;
                    else answer = 4;
                }
            }
        };
        answerImage1 = findViewById(R.id.answerImage1);
        answerImage1.setOnClickListener(listenerImage);
        answerImage2 = findViewById(R.id.answerImage2);
        answerImage2.setOnClickListener(listenerImage);
        answerImage3 = findViewById(R.id.answerImage3);
        answerImage3.setOnClickListener(listenerImage);
        answerImage4 = findViewById(R.id.answerImage4);
        answerImage4.setOnClickListener(listenerImage);

        Button.OnClickListener listener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(v.getId() == R.id.Image1)image=1;
                else if(v.getId() == R.id.Image2)image=2;
                else if(v.getId() == R.id.Image3)image=3;
                else image=4;

                Intent i = new Intent();
                i.setAction(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        };
        imag1= findViewById(R.id.Image1);
        imag2= findViewById(R.id.Image2);
        imag3= findViewById(R.id.Image3);
        imag4= findViewById(R.id.Image4);
        imag1.setOnClickListener(listener);
        imag2.setOnClickListener(listener);
        imag3.setOnClickListener(listener);
        imag4.setOnClickListener(listener);


        layouts = new ConstraintLayout[layoutIds.length];
        for(int i=0;i<layouts.length;i++){
            layouts[i] = findViewById(layoutIds[i]);
        }

        buttonToggle = findViewById(R.id.buttonToggle);
        buttonToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { // image
                if(buttonToggle.isChecked()){
                    show(R.id.layoutImage);
                    type = QuestionBean.TYPE_IMAGE;
                }else { // text
                    show(R.id.layoutText);
                    type = QuestionBean.TYPE_TEXT;
                }
            }
        });

        qid = getIntent().getIntExtra("qid", -1);
        if(qid>-1){
            question = dbHelper.get(qid);
            if(question.getType().equals(QuestionBean.TYPE_TEXT)){ // text
                buttonToggle.setChecked(false);
                show(R.id.layoutText);

                if(question.getAnswer() == 1){ firstRadio.setChecked(true); answer = 1;}
                else if(question.getAnswer() == 2){ secondRadio.setChecked(true); answer = 2;}
                else if(question.getAnswer() == 3){ thirdRadio.setChecked(true); answer = 3;}
                else{ forthRadio.setChecked(true); answer = 4;}

                firstEdit.setText(question.getEx1());
                secondEdit.setText(question.getEx2());
                thirdEdit.setText(question.getEx3());
                forthEdit.setText(question.getEx4());
            }else { // image
                buttonToggle.setChecked(true);
                show(R.id.layoutImage);
                //
                type = QuestionBean.TYPE_IMAGE;

                if(question.getAnswer() == 1){
                    answer = 1;
                    answerImage1.setChecked(true);
                    radioGroup2_2.clearCheck();
                }else if(question.getAnswer() == 2){
                    answer = 2;
                    answerImage2.setChecked(true);
                    radioGroup2_2.clearCheck();
                }else if(question.getAnswer() == 3){
                    answer = 3;
                    answerImage3.setChecked(true);
                    radioGroup2_1.clearCheck();
                }else {answer = 4;
                    answerImage4.setChecked(true);
                    radioGroup2_1.clearCheck();
                }
                imag1.setImageURI(Uri.parse(question.getEx1()));
                imag2.setImageURI(Uri.parse(question.getEx2()));
                imag3.setImageURI(Uri.parse(question.getEx3()));
                imag4.setImageURI(Uri.parse(question.getEx4()));
            }
            questionEdit.setText(question.getQuestion());
            scoreEdit.setText(question.getScore()+"");
        }else {
            question = new QuestionBean();
        }
        //DB생성 create

    }

    public void onSave(View v){

        String questionCheck, scoreCheck, answerCheck, ex1Check, ex2Check, ex3Check, ex4Check; // id, type 은 자동 저장 score 부분은 inputtype을 숫자로 해줘서 ok
        questionCheck = questionEdit.getText().toString();
        scoreCheck = scoreEdit.getText().toString();
        answerCheck = answer+"";
        ex1Check = firstEdit.getText().toString();
        ex2Check = secondEdit.getText().toString();
        ex3Check = thirdEdit.getText().toString();
        ex4Check = forthEdit.getText().toString();
        // scoreCheck 부분 .이 있던데 10.으로 들어왔을 때 어떻게 처리?

        if(questionCheck.trim().getBytes().length<=0 || scoreCheck.trim().getBytes().length<=0 || answerCheck.getBytes().length<=0){
            Toast.makeText(QuestionActivity.this, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }else if(answer == 0){
            Toast.makeText(QuestionActivity.this, "정답을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        if(type == QuestionBean.TYPE_TEXT){ // text
            if(ex1Check.trim().getBytes().length<=0 || ex2Check.trim().getBytes().length<=0 ||ex3Check.trim().getBytes().length<=0 ||ex4Check.trim().getBytes().length<=0){
                Toast.makeText(QuestionActivity.this, "입력되지 않은 보기가 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }else { // 문제 없으니까 data 삽입
                question.setEx1(ex1Check);
                question.setEx2(ex2Check);
                question.setEx3(ex3Check);
                question.setEx4(ex4Check);
            }
        }else { // image
            if(imag1.getDrawable() == null || imag2.getDrawable() == null || imag3.getDrawable() == null || imag4.getDrawable() == null){
                Toast.makeText(QuestionActivity.this, "선택되지 않은 이미지가 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        question.setQuestion(questionEdit.getText().toString());
        question.setScore(Integer.parseInt(scoreCheck));
        question.setAnswer(answer);
        question.setType(type);

        if(qid>-1) dbHelper.update(question);
        else dbHelper.insert(question);
        setResult(RESULT_OK);

        finish();
    }

    private void show(int id){ // 레이아웃 바꾸는 메서드
        // layoutLogin, layoutList : 처음 화면이 login, 다음 메모장이 list
        // showLayout(R.id.LayoutLogin); .. 이렇게 하면 Login 화면이 나온다.
        for(ConstraintLayout layout: layouts){
            if(layout.getId() == id){
                layout.setVisibility(View.VISIBLE);
            }else {
                layout.setVisibility(View.GONE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode ==1){
            if(resultCode == RESULT_OK){
                try{
                    //InputStream in = getContentResolver().openInputStream(data.getData());
                    //Bitmap img = BitmapFactory.decodeStream(in);
                    //in.close(); 비트맵 방식. 어렵당..
                    // uri로 가자.

                    Uri uri = data.getData();
                    if(image == 1){
                        imag1.setImageURI(uri);
                        question.setEx1(uri.toString());
                    }else if(image == 2){
                        imag2.setImageURI(uri);
                        question.setEx2(uri.toString());
                    }else if(image == 3){
                        imag3.setImageURI(uri);
                        question.setEx3(uri.toString());
                    }else {
                        imag4.setImageURI(uri);
                        question.setEx4(uri.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void onDelete(View v) {
        if (qid == -1) finish();
        dbHelper.delete(qid);
        setResult(RESULT_OK);
        finish();
    }
}
