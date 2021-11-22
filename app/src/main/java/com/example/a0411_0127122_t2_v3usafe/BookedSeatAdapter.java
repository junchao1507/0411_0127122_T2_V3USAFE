package com.example.a0411_0127122_t2_v3usafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a0411_0127122_t2_v3usafe.BookedLesson;
import com.example.a0411_0127122_t2_v3usafe.R;
import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;
import java.util.List;

public class BookedSeatAdapter extends ArrayAdapter<BookedLesson> {
    List<BookedLesson> items_list = new ArrayList<>();
    int menu_layout_id;

    public BookedSeatAdapter(@NonNull Context context, int resource, @NonNull List<BookedLesson> objects) {
        super(context, resource, objects);
        items_list = objects;
        menu_layout_id = resource;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public BookedLesson getItem(int position) {
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

        TextView tvModuleName = v.findViewById(R.id.tv_module_name);
        TextView tvDayDate = v.findViewById(R.id.tv_day_date);
        TextView tvTime = v.findViewById(R.id.tv_time);
        TextView tvLocation = v.findViewById(R.id.tv_location);
        TextView tvSeats = v.findViewById(R.id.tv_seats);
        EditSpinner spnSeatNo = v.findViewById(R.id.spn_select_seat);

        // get the item using the  position param
        BookedLesson bookedLesson = items_list.get(position);

        tvModuleName.setText(bookedLesson.getModuleName());
        tvDayDate.setText(bookedLesson.getDayDate());
        tvTime.setText(bookedLesson.getStartTime() + " - " + bookedLesson.getEndTime());
        tvLocation.setText(bookedLesson.getLocation());
        tvSeats.setText(bookedLesson.getSeat());

        return v;
    }
}
