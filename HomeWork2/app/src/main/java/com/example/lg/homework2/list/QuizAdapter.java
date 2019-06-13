package com.example.lg.homework2.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lg.homework2.R;
import com.example.lg.homework2.model.QuestionBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizAdapter extends RecyclerView.Adapter<QuizViewHolder> {

    private ArrayList<QuestionBean> data;
    private ItemClickListener listener;

    public QuizAdapter(ArrayList<QuestionBean> data, ItemClickListener listener){
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int i) {
        QuestionBean question = data.get(i);

        holder.viewQuestionText.setText(question.getQuestion());
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.viewDate.setText(format.format(date));
        holder.viewType.setText(question.getType());

        holder.itemView.setOnClickListener(v ->{
            listener.onItemClick(v, i);
        });
    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.size();
    }
}
