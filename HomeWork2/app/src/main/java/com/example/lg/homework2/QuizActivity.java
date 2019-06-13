package com.example.lg.homework2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lg.homework2.list.QuizAdapter;
import com.example.lg.homework2.model.QuestionBean;
import com.example.lg.homework2.model.QuestionDBHelper;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    String difficulty;
    TextView scoreView;
    TextView difficultyText, showTextView;
    ConstraintLayout exampleView;
    private int goalScore = 100;

    private int[] layoutIds;
    private ConstraintLayout[] layouts;

    private QuestionDBHelper dbHelper;
    private ArrayList<QuestionBean> data;

    String str="", hardAnswer="", dialogString="",questionStr="",explanationStr = "";

    Button buttonAnswer;

    RadioGroup radioGroup2_1, radioGroup2_2, radioGroup1; // radioGroup 부분
    RadioButton answerImage1,answerImage2,answerImage3,answerImage4; // image 부분 radio
    RadioButton firstRadio, secondRadio, thirdRadio, forthRadio; // text 부분 radio
    ImageView imag1,imag2,imag3,imag4;
    int image, answer=0, score=0;
    int userAnswer;
    TextView firstEdit, secondEdit, thirdEdit, forthEdit, questionView, scoreExplanation;
    EditText hardUserAnswer;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = new QuestionDBHelper(this, "DB", null, 1);
        data = dbHelper.get();

        scoreView = findViewById(R.id.scoreView);

        difficulty = getIntent().getStringExtra("difficulty");
        difficultyText= findViewById(R.id.difficultyText);
        difficultyText.setText(difficulty);
        if(difficulty.equals("Hard")){
            difficultyText.setTextColor(Color.RED);
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        exampleView = findViewById(R.id.exampleView);
        inflater.inflate(R.layout.question_layout, exampleView, true);
        layoutIds = new int[]{R.id.layoutText, R.id.layoutImage};

        layouts = new ConstraintLayout[layoutIds.length];
        for (int i = 0; i < layouts.length; i++) {
            layouts[i] = findViewById(layoutIds[i]);
        }
        View.OnClickListener listenerText = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.firstRadio){
                    userAnswer = 1;
                }else  if(v.getId() == R.id.secondRadio){
                    userAnswer = 2;
                }else  if(v.getId() == R.id.thirdRadio){
                    userAnswer = 3;
                }else {
                    userAnswer = 4;
                }
            }
        };
        radioGroup1 = findViewById(R.id.radioGroup1); //text
        firstRadio = findViewById(R.id.firstRadio);
        secondRadio = findViewById(R.id.secondRadio);
        thirdRadio = findViewById(R.id.thirdRadio);
        forthRadio = findViewById(R.id.forthRadio);
        firstRadio.setOnClickListener(listenerText);
        secondRadio.setOnClickListener(listenerText);
        thirdRadio.setOnClickListener(listenerText);
        forthRadio.setOnClickListener(listenerText);

        firstEdit = findViewById(R.id.firstEdit);
        secondEdit = findViewById(R.id.secondEdit);
        thirdEdit = findViewById(R.id.thirdEdit);
        forthEdit = findViewById(R.id.forthEdit);

        showTextView = findViewById(R.id.showTextView);

        radioGroup2_1=findViewById(R.id.radioGroup2_1); // image
        View.OnClickListener listenerImage = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.answerImage1 || v.getId() == R.id.answerImage2){
                    radioGroup2_2.clearCheck();
                    if(v.getId() == R.id.answerImage1) userAnswer = 1;
                    else userAnswer = 2;
                }else {
                    radioGroup2_1.clearCheck();
                    if(v.getId() == R.id.answerImage3) userAnswer = 3;
                    else userAnswer = 4;
                }
            }
        };
        answerImage1=findViewById(R.id.answerImage1);
        answerImage1.setOnClickListener(listenerImage);
        answerImage2 = findViewById(R.id.answerImage2);
        answerImage2.setOnClickListener(listenerImage);
        radioGroup2_2 = findViewById(R.id.radioGroup2_2);
        answerImage3 = findViewById(R.id.answerImage3);
        answerImage3.setOnClickListener(listenerImage);
        answerImage4 =findViewById(R.id.answerImage4);
        answerImage4.setOnClickListener(listenerImage);

        imag1 = findViewById(R.id.image1);
        imag2 = findViewById(R.id.image2);
        imag3 = findViewById(R.id.image3);
        imag4 = findViewById(R.id.image4);
        hardUserAnswer = findViewById(R.id.hardAnswer);
        questionView = findViewById(R.id.questionView);

        scoreExplanation = findViewById(R.id.scoreExplanation);

        buttonAnswer =findViewById(R.id.buttonAnswer);
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예외

                if (difficulty.equals("Hard")){
                    if((hardUserAnswer.getText().toString().trim().getBytes().length<=0) && data.get(i).getType().equals(QuestionBean.TYPE_TEXT)){
                        Toast.makeText(QuizActivity.this, "답을 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }else if( (!answerImage1.isChecked()&&!answerImage2.isChecked()&&!answerImage3.isChecked()&&!answerImage4.isChecked()) && (data.get(i).getType().equals(QuestionBean.TYPE_IMAGE))){
                        Toast.makeText(QuizActivity.this, "답을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (hardUserAnswer.getText().toString().equals(hardAnswer)) { // hardmode text 정답일때
                        score += data.get(i).getScore();
                        dialogString = "정답입니다. " + data.get(i).getScore() + "점 획득!";
                    }else if(data.get(i).getType().equals(QuestionBean.TYPE_IMAGE) && userAnswer==answer ){ // hardmode image 정답일 때
                        score += data.get(i).getScore();
                        dialogString = "정답입니다. " + data.get(i).getScore() + "점 획득!";
                    }else { // 틀릴 때
                        dialogString = "오답입니다.";
                    }
                }else {
                    if((!answerImage1.isChecked()&&!answerImage2.isChecked()&&!answerImage3.isChecked()&&!answerImage4.isChecked()) && (data.get(i).getType().equals(QuestionBean.TYPE_IMAGE))){ // image
                        Toast.makeText(QuizActivity.this, "답을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }else if( (!firstRadio.isChecked()&&!secondRadio.isChecked()&&!thirdRadio.isChecked()&&!forthRadio.isChecked()) && data.get(i).getType().equals(QuestionBean.TYPE_TEXT)){
                        Toast.makeText(QuizActivity.this, "답을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                        return;

                    }


                    if (userAnswer == answer) { // 정답일때
                        score += data.get(i).getScore();
                        dialogString = "정답입니다. " + data.get(i).getScore() + "점 획득!";
                        // 팝업 띄우기
                    } else { // 틀릴떄
                        dialogString = "오답입니다.";
                    }
                }

                i++;
                if(i == data.size()){
                    dialogString = "최종 "+score+"점 입니다!"; }

                dialog(dialogString);
                change();
            }
        });

                scoreView.setText(score+"");
                str = data.get(i).getQuestion();
                questionStr = "Question "+(i+1);
                questionView.setText(questionStr);
                explanationStr = "Correct score = "+data.get(i).getScore();
                scoreExplanation.setText(explanationStr);

                showTextView.setText(data.get(i).getQuestion());
                // 다른 부분
                if (data.get(i).getType().equals(QuestionBean.TYPE_IMAGE)) { // type == image
                    show(R.id.layoutImage);
                    answer = data.get(i).getAnswer();
                    imag1.setImageURI(Uri.parse(data.get(i).getEx1()));
                    imag2.setImageURI(Uri.parse(data.get(i).getEx2()));
                    imag3.setImageURI(Uri.parse(data.get(i).getEx3()));
                    imag4.setImageURI(Uri.parse(data.get(i).getEx4()));

                } else { // type == text
                    show(R.id.layoutText);
                    if (difficulty.equals("Hard")) { // 주관식 나옴
                        if (data.get(i).getAnswer() == 1) {
                            hardAnswer = data.get(i).getEx1();
                        } else if (data.get(i).getAnswer() == 2) {
                            hardAnswer = data.get(i).getEx2();
                        } else if (data.get(i).getAnswer() == 3) {
                            hardAnswer = data.get(i).getEx3();
                        } else if (data.get(i).getAnswer() == 4) {
                            hardAnswer = data.get(i).getEx4();
                        }

                        radioGroup1.setVisibility(View.GONE);
                        firstEdit.setVisibility(View.GONE);
                        secondEdit.setVisibility(View.GONE);
                        thirdEdit.setVisibility(View.GONE);
                        forthEdit.setVisibility(View.GONE);
                        hardUserAnswer.setVisibility(View.VISIBLE);

                    }
                    answer = data.get(i).getAnswer();
                    firstEdit.setText(data.get(i).getEx1());
                    secondEdit.setText(data.get(i).getEx2());
                    thirdEdit.setText(data.get(i).getEx3());
                    forthEdit.setText(data.get(i).getEx4());
                }

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

    private void change(){
        if(i >= data.size()){
            return; }

        scoreView.setText(score+"");
        str = data.get(i).getQuestion();
        questionStr = "Question "+(i+1);
        questionView.setText(questionStr);
        explanationStr = "Correct score = "+data.get(i).getScore();
        scoreExplanation.setText(explanationStr);

        showTextView.setText(data.get(i).getQuestion());
        radioGroup1.clearCheck();
        radioGroup2_1.clearCheck();
        radioGroup2_2.clearCheck();
        hardUserAnswer.setText("");

        // 다른 부분
        if (data.get(i).getType().equals(QuestionBean.TYPE_IMAGE)) { // type == image
            show(R.id.layoutImage);
            answer = data.get(i).getAnswer();
            imag1.setImageURI(Uri.parse(data.get(i).getEx1()));
            imag2.setImageURI(Uri.parse(data.get(i).getEx2()));
            imag3.setImageURI(Uri.parse(data.get(i).getEx3()));
            imag4.setImageURI(Uri.parse(data.get(i).getEx4()));

        } else { // type == text
            show(R.id.layoutText);
            if (difficulty.equals("Hard")) { // 주관식 나옴
                if (data.get(i).getAnswer() == 1) {
                    hardAnswer = data.get(i).getEx1();
                } else if (data.get(i).getAnswer() == 2) {
                    hardAnswer = data.get(i).getEx2();
                } else if (data.get(i).getAnswer() == 3) {
                    hardAnswer = data.get(i).getEx3();
                } else if (data.get(i).getAnswer() == 4) {
                    hardAnswer = data.get(i).getEx4();
                }

                radioGroup1.setVisibility(View.GONE);
                firstEdit.setVisibility(View.GONE);
                secondEdit.setVisibility(View.GONE);
                thirdEdit.setVisibility(View.GONE);
                forthEdit.setVisibility(View.GONE);
                hardUserAnswer.setVisibility(View.VISIBLE);

            }
            answer = data.get(i).getAnswer();
            firstEdit.setText(data.get(i).getEx1());
            secondEdit.setText(data.get(i).getEx2());
            thirdEdit.setText(data.get(i).getEx3());
            forthEdit.setText(data.get(i).getEx4());
        }
    }

    public void dialog(String dialogString){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(i >= data.size()){
                    QuizActivity.this.finish();}
            }
        });
        builder.show();
    }

}
