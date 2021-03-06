package com.smartapp.depc_ice.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.Agenda.Adapter.GridAdapterCalendar;
import com.smartapp.depc_ice.Activities.Agenda.PlanficadorPedidosActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarCustomView extends LinearLayout {
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private TextView txtMes;
    private Button addEventButton;
    List<Date> dayValueInCells = new ArrayList<Date>();
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    String[] MONTHS = new String[] {"ENERO", "FEBRERO", "MARZO","ABRIL", "MAYO", "JUNIO","JULIO", "AGOSTO", "SEPTIEMBRE","OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    private Context context;
    int mesTo = 0;
    private GridAdapterCalendar mAdapter;
    public CalendarCustomView(Context context) {
        super(context);
    }
    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        //setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();

    }
    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        txtMes = (TextView)view.findViewById(R.id.txt_mes);
        addEventButton = (Button)view.findViewById(R.id.add_calendar_event);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);

        mesTo = cal.get(Calendar.MONTH);
    }
    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setGridCellClickEvents(){
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
                Log.e("pilas",""+eventIndicator.getText());


                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                String time = sdf.format(dayValueInCells.get(position));
                Calendar c = Calendar.getInstance();
                c.setTime(dayValueInCells.get(position));
                int day = c.get(Calendar.DAY_OF_MONTH);
                int dayWeek = c.get(Calendar.DAY_OF_WEEK);

                int clickMes = c.get(Calendar.MONTH);
                if (mesTo == clickMes) {

                    Intent intent = new Intent(context, PlanficadorPedidosActivity.class);
                    intent.putExtra("fecha", time);
                    intent.putExtra("dia", ""+(dayWeek - 1));
                    context.startActivity(intent);
                }


            }
        });
    }
    public void setUpCalendarAdapter(int mes,int year){
        cal.set(year,mes-1,1);

        mesTo = cal.get(Calendar.MONTH);
        List<EventObjects> mEvents = new ArrayList<EventObjects>();

        String mesString = ""+mes;
        if (mes <= 9){
            mesString = "0"+mes;
        }
        try {

            //List<ClientesVisitas> clientes = DataBaseHelper.getClienteVisita(DepcApplication.getApplication().getClientesVisitasDao(), mesString, ""+year);
            List<ClientesVisitas> clientes = DataBaseHelper.getClienteVisita(DepcApplication.getApplication().getClientesVisitasDao());

            if (clientes != null) {

                String fecha = null;

                DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, mesTo,calendar.get(Calendar.DAY_OF_MONTH));
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String[] days = new String[7];
                for (int i = 0; i < 7; i++)
                {
                    days[i] = format1.format(calendar.getTime());
                    Log.e("Fechas:",""+days[i]);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }


                EventObjects evente;
                for (ClientesVisitas c : clientes) {


                    if (c.getDia_visita() != null){
                        if (Utils.isNumber(c.getDia_visita())){
                            int dia = Integer.parseInt(c.getDia_visita());
                            fecha = days[dia - 1];
                        }

                    }



                    if (fecha != null) {
                        if (mEvents.contains(new EventObjects(fecha))){
                            int index = mEvents.indexOf(new EventObjects(fecha));
                            mEvents.get(index).setContador(mEvents.get(index).getContador() + 1);
                            mEvents.get(index).setMessage("# vist. " + mEvents.get(index).getContador());
                        }else{
                            evente = new EventObjects();
                            evente.setContador(1);
                            evente.setFecha(fecha);
                            evente.setMessage("# vist. " + evente.getContador());
                            evente.setDate(ConvertToDate("" + evente.getFecha()));
                            mEvents.add(evente);
                        }
                    }

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /************/

        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;



        dayValueInCells = new ArrayList<Date>();
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        calendarGridView.setAdapter(null);
        mAdapter = new GridAdapterCalendar(context, dayValueInCells, cal, mEvents);
        calendarGridView.setAdapter(mAdapter);
    }


    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
}