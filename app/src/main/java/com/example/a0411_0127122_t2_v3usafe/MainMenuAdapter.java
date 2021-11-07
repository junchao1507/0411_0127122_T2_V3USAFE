package com.example.a0411_0127122_t2_v3usafe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainMenuAdapter extends ArrayAdapter<MainMenuItem> {

    List<MainMenuItem> items_list = new ArrayList<>();
    int menu_layout_id;

    public MainMenuAdapter(@NonNull Context context, int resource, @NonNull List<MainMenuItem> objects) {
        super(context, resource, objects);
        items_list = objects;
        menu_layout_id = resource;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public MainMenuItem getItem(int position) {
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
        ImageView btn_menu = v.findViewById(R.id.imv_menu);
        TextView txt_menu = v.findViewById(R.id.tv_menu);

        // get the item using the  position param
        MainMenuItem menuItem = items_list.get(position);

        btn_menu.setImageResource(menuItem.getImgId());
        txt_menu.setText(menuItem.getMenuName());

//        // Set different colors for each card, but the card settings will become default
//        switch(position){
//            case 0: case 3:
//                v.setBackgroundColor(Color.parseColor("#ECEC00"));
//                break;
//            case 1: case 4:
//                v.setBackgroundColor(Color.parseColor("#73E977"));
//                break;
//            case 2: case 5:
//                v.setBackgroundColor(Color.parseColor("#5EEEEC"));
//                break;
//        }

        return v;
    }
}
