package com.example.lg.homework2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lg.homework2.list.ItemClickListener;
import com.example.lg.homework2.list.QuizAdapter;
import com.example.lg.homework2.model.QuestionBean;
import com.example.lg.homework2.model.QuestionDBHelper;

import java.io.InputStream;
import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity implements ItemClickListener { // Setting, QuestionList, Login으로 돌아가는 거 다 해야 됨.
    // == mainActivity

    EditText editQuestion;
    Button buttonLogin;
    String passwd;
    String check;
    private int[] layoutIds = {R.id.layoutLogin, R.id.layoutList}; // login, list, setting 부분 다 있다.
    private ConstraintLayout[] layouts;
    ImageView changeQuestionList;


    // db 붙이는 부분
    private QuestionDBHelper dbHelper;
    private RecyclerView listView;
    private QuizAdapter adapter;
    private ArrayList<QuestionBean> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        dbHelper = new QuestionDBHelper(this, "DB", null, 1);
        data = dbHelper.get();

        adapter = new QuizAdapter(data, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listView = findViewById(R.id.memoList);
        listView.setAdapter(adapter);
        listView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
        //

        editQuestion = findViewById(R.id.editQuestion);
        passwd = "passwd";

        layouts = new ConstraintLayout[layoutIds.length];
        for (int i = 0; i < layouts.length; i++) {
            layouts[i] = findViewById(layoutIds[i]);
        }
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editQuestion.getText().toString().equals(passwd)) {
                    showLayout(R.id.layoutList);
                } else {
                    Toast.makeText(QuestionListActivity.this, "Wrong Answer.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        // 2번째 layout
        changeQuestionList = findViewById(R.id.changeQuestionList);
        changeQuestionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionListActivity.this, QuestionActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void showLayout(int id) { // 레이아웃 바꾸는 메서드
        // layoutLogin, layoutList : 처음 화면이 login, 다음 메모장이 list
        // showLayout(R.id.LayoutLogin); .. 이렇게 하면 Login 화면이 나온다.
        for (ConstraintLayout layout : layouts) {
            if (layout.getId() == id) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(View v, int index) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("qid", data.get(index).getQid());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.data = dbHelper.get();
            adapter.notifyDataSetChanged();
        }
    }

}
