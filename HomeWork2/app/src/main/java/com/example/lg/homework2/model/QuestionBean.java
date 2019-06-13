package com.example.lg.homework2.model;

public class QuestionBean {
    public static final String TYPE_TEXT = "text"; // Toast Length 할 때 처럼 짧고 긺을 정해주면 좋다.
    public static final String TYPE_IMAGE = "image"; // 문제 개수에 따라서 바뀌는 게 아니기 때문에 이렇게 해주자.
    // 누가 건드리지 않도록 상수 설정 방법을 쓴 것이다.
    // text, image 설정


    private int qid;            // primary key
    private String question;    // 질문
    private int score;          // 배점
    private int answer;         // 답
    private String ex1;
    private String ex2;
    private String ex3;
    private String ex4;

    private String type; // 9개  저장 일시는?

    public static String getTypeText() {
        return TYPE_TEXT;
    }

    public static String getTypeImage() {
        return TYPE_IMAGE;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getEx1() {
        return ex1;
    }

    public void setEx1(String ex1) {
        this.ex1 = ex1;
    }

    public String getEx2() {
        return ex2;
    }

    public void setEx2(String ex2) {
        this.ex2 = ex2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }

    public String getEx4() {
        return ex4;
    }

    public void setEx4(String ex4) {
        this.ex4 = ex4;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
