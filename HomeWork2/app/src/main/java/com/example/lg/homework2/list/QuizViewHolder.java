package com.example.lg.homework2.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lg.homework2.R;

public class QuizViewHolder extends RecyclerView.ViewHolder {
    public TextView viewQuestionText, viewDate, viewType;

    public QuizViewHolder(@NonNull View itemView) {
        super(itemView);
        viewQuestionText = itemView.findViewById(R.id.viewQuestionText);
        viewDate = itemView.findViewById(R.id.viewDate);
        viewType = itemView.findViewById(R.id.viewType);
    }
}
