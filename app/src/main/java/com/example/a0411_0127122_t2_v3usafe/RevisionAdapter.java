package com.example.a0411_0127122_t2_v3usafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;
import java.util.List;

public class RevisionAdapter extends ArrayAdapter<Question> {
    List<Question> items_list = new ArrayList<>();
    int menu_layout_id;

    public RevisionAdapter(@NonNull Context context, int resource, @NonNull List<Question> objects) {
        super(context, resource, objects);
        items_list = objects;
        menu_layout_id = resource;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public Question getItem(int position) {
        return items_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            // getting reference to the main layout and
            // initializing
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(menu_layout_id, null);
        }

        // initializing the imageview and textview and
        // setting data

        TextView tvQuestion = v.findViewById(R.id.tv_question);
        TextView tvAnswer = v.findViewById(R.id.tv_answer);


        // get the item using the  position param
        Question question = items_list.get(position);

        String answer = items_list.get(position).getAnswer();
        String optA = items_list.get(position).getOptionA();
        String optB = items_list.get(position).getOptionB();
        String optC = items_list.get(position).getOptionC();
        String optD = items_list.get(position).getOptionD();
        String tempAns = "";
        String answerDisplay = "";

        // Check if the answer = "All of the above". Yes then display all options.
        if(answer.equals("All of the above")){
            if(!optA.equals(answer)){
                answerDisplay = optA;
            }
            if(!optB.equals(answer)){
                tempAns = "; " + optB;
                answerDisplay = answerDisplay.concat(tempAns);
            }
            if(!optC.equals(answer)){
                tempAns = "; " + optC;
                answerDisplay = answerDisplay.concat(tempAns);
            }
            if(!optD.equals(answer)) {
                tempAns = "; " + optD;
                answerDisplay = answerDisplay.concat(tempAns);
            }
        }
        else{
            answerDisplay = answer;
        }

        // Set text
        tvQuestion.setText(question.getQuestion());
        tvAnswer.setText(answerDisplay);


        return v;
    }
}
