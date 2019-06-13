package com.example.lg.homework2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class QuestionDBHelper extends SQLiteOpenHelper {

    private ArrayList<QuestionBean> data;

    public QuestionDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        data = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table question (qid integer primary key autoincrement, ";
        sql += "question text, score integer, answer integer, ex1 text, ex2 text, ex3 text, ex4 text, type text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table question";
        db.execSQL(sql);
        onCreate(db);
    }

    public long insert(QuestionBean question){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question", question.getQuestion());
        values.put("score", question.getScore());
        values.put("answer", question.getAnswer());
        values.put("ex1", question.getEx1());
        values.put("ex2", question.getEx2());
        values.put("ex3", question.getEx3());
        values.put("ex4", question.getEx4());
        values.put("type", question.getType());
        return db.insert("question", null, values);
    }

    public long update(QuestionBean question){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question", question.getQuestion());
        values.put("score", question.getScore());
        values.put("answer", question.getAnswer());
        values.put("ex1", question.getEx1());
        values.put("ex2", question.getEx2());
        values.put("ex3", question.getEx3());
        values.put("ex4", question.getEx4());
        values.put("type", question.getType());
        String idStr = String.valueOf(question.getQid());
        return db.update("question", values, "qid=?", new String[]{idStr});
    }

    public long delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String idStr = String.valueOf(id);
        return db.delete("question", "qid=?", new String[]{idStr});
    }

    public QuestionBean get(int id){
        SQLiteDatabase db = getReadableDatabase();
        String idStr = String.valueOf(id);
        Cursor c = db.query("question", null, "qid=?", new String[]{idStr}, null, null, null);
        if(c.moveToNext()){
            QuestionBean question = new QuestionBean();
            question.setQid(c.getInt(c.getColumnIndex("qid")));
            question.setQuestion(c.getString(c.getColumnIndex("question")));
            question.setScore(c.getInt(c.getColumnIndex("score")));
            question.setAnswer(c.getInt(c.getColumnIndex("answer")));
            question.setEx1(c.getString(c.getColumnIndex("ex1")));
            question.setEx2(c.getString(c.getColumnIndex("ex2")));
            question.setEx3(c.getString(c.getColumnIndex("ex3")));
            question.setEx4(c.getString(c.getColumnIndex("ex4")));
            question.setType(c.getString(c.getColumnIndex("type")));
            return question;
        }else {
            return null;
        }
    }

    public ArrayList<QuestionBean> get(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("question", null,  null,null, null, null, null);
        data.clear();
        while(c.moveToNext()){
            QuestionBean question = new QuestionBean();
            question.setQid(c.getInt(c.getColumnIndex("qid")));
            question.setQuestion(c.getString(c.getColumnIndex("question")));
            question.setScore(c.getInt(c.getColumnIndex("score")));
            question.setAnswer(c.getInt(c.getColumnIndex("answer")));
            question.setEx1(c.getString(c.getColumnIndex("ex1")));
            question.setEx2(c.getString(c.getColumnIndex("ex2")));
            question.setEx3(c.getString(c.getColumnIndex("ex3")));
            question.setEx4(c.getString(c.getColumnIndex("ex4")));
            question.setType(c.getString(c.getColumnIndex("type")));
            data.add(question);
        }
        return data;
    }
}
