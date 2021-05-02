package com.smartapp.depc_ice.Activities.Agenda.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.EventObjects;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridAdapterCalendar extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<EventObjects> allEvents;
    public GridAdapterCalendar(Context context, List<Date> monthlyDates, Calendar currentDate, List<EventObjects> allEvents) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;

        if(view == null){
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            cellNumber.setTextColor(Color.parseColor("#505050"));
        }else{
            view.setBackgroundColor(Color.parseColor("#F0F0F0"));
            cellNumber.setTextColor(Color.parseColor("#B0B0B0"));
        }
        //Add day to calendar
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
        eventIndicator.setText("");
        Calendar eventCalendar = Calendar.getInstance();
        //linearLayout.setVisibility(View.GONE);
        for(int i = 0; i < allEvents.size(); i++){
            eventCalendar.setTime(allEvents.get(i).getDate());
            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){
                eventIndicator.setBackgroundColor(Color.parseColor("#000000"));
                eventIndicator.setTextColor(Color.parseColor("#FFFFFF"));
                eventIndicator.setText(""+allEvents.get(i).getMessage());
                //linearLayout.setVisibility(View.VISIBLE);
                view.setBackgroundColor(Color.parseColor("#66ccff"));
            }
        }


        return view;
    }
    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}