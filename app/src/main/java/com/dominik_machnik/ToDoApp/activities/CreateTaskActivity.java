package com.dominik_machnik.ToDoApp.activities;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dominik_machnik.ToDoApp.R;
import com.dominik_machnik.ToDoApp.database.DbHandler;

import com.dominik_machnik.ToDoApp.notifications.NotificationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;



public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button button;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTask)
    TextInputEditText nameTask;

    @BindView(R.id.descTask)
    TextInputEditText descTask;

    @BindView(R.id.dateTask)
    TextInputEditText dateTask;

    @BindView(R.id.timeTask)
    TextInputEditText timeTask;

    @BindView(R.id.actionBtn)
    FloatingActionButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        button = findViewById(R.id.dateTime_pick);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, CreateTaskActivity.this,year, month, day);
                datePickerDialog.show();

            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        events();

    }


    public void events(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTask.getText().toString();
                String desc = descTask.getText().toString();
                String date = dateTask.getText().toString();
                String time = timeTask.getText().toString();


                if (name.trim().length() > 0 && desc.trim().length() > 0){

                    DbHandler dbHandler = new DbHandler(CreateTaskActivity.this);
                    dbHandler.insertTasks(name, desc, date, time);

                    new NotificationUtils().showNotification(CreateTaskActivity.this, "Zadanie utworzone", "Pomyślnie dodano nowe zadanie.");
                    new NotificationUtils().showNotificationAtDateTime(CreateTaskActivity.this,"Przypomnienie o: " + name, time + "     " + date, date, time);
                    goToBack();

                }else{
                    new NotificationUtils().showNotification(CreateTaskActivity.this, "Zadanie nie zostało utworzone", "Twoje zadanie nie zostało dodane, ponieważ nie wypełniłeś wszystkich wymaganych pól.");

                }



            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            //Start your main activity here
            goToBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToBack();


    }

    public void goToBack(){
        Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this, CreateTaskActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        dateTask.setText(myday + "/" + myMonth + "/" +myYear);
        timeTask.setText(myHour + ":" + myMinute);

    }


}
