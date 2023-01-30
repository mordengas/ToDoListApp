package com.dominik_machnik.ToDoApp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.dominik_machnik.ToDoApp.R;
import com.dominik_machnik.ToDoApp.database.DbHandler;
import com.dominik_machnik.ToDoApp.notifications.NotificationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;



public class UpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    Button button;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;


    @BindView(R.id.parentContainer)
    RelativeLayout parentContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameTaskUpdate)
    TextInputEditText nameTask;

    @BindView(R.id.descTaskUpdate)
    TextInputEditText descTask;

    @BindView(R.id.dateTaskUpdate)
    TextInputEditText dateTask;

    @BindView(R.id.timeTaskUpdate)
    TextInputEditText timeTask;

    @BindView(R.id.statusSwitch)
    SwitchMaterial statusSwitch;

    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;

    private DbHandler db;

    private String idTaskStr = "";
    private String nameTaskStr = "";
    private String descTaskStr = "";
    private String dateTaskStr = "";
    private String timeTaskStr = "";
    private String statusTaskStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        db = new DbHandler(this);


        button = findViewById(R.id.dateTime_pickUpdate);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, UpdateActivity.this,year, month, day);
                datePickerDialog.show();

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        setData();
        events();


    }

    public void getData(){
        idTaskStr = getIntent().getStringExtra("idTask");
        nameTaskStr = getIntent().getStringExtra("nameTask");
        descTaskStr = getIntent().getStringExtra("descTask");
        dateTaskStr = getIntent().getStringExtra("dateTask");
        timeTaskStr = getIntent().getStringExtra("timeTask");
        statusTaskStr = getIntent().getStringExtra("statusTask");
    }

    @SuppressLint("SetTextI18n")
    public void setData(){
        toolbar.setTitle(nameTaskStr.toUpperCase());

        nameTask.setText(nameTaskStr);
        descTask.setText(descTaskStr);
        dateTask.setText(dateTaskStr);
        timeTask.setText(timeTaskStr);


        if (statusTaskStr.equals("complete")){
            statusSwitch.setChecked(true);
        }


    }


    public void events(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusFinal = "incomplete";
                if (statusSwitch.isChecked()){
                    statusFinal = "complete";
                }

                String newName = nameTask.getText().toString();
                String newDesc = descTask.getText().toString();
                String newDate = dateTask.getText().toString();
                String newTime = timeTask.getText().toString();

                if (newName.trim().length() > 0 && newDesc.trim().length() > 0){


                    db.updateTask(idTaskStr, newName, newDesc, newDate, newTime, statusFinal);
                    new NotificationUtils().showNotification(UpdateActivity.this, "Zadanie zaktualizowane", "Zadanie zostało pomyślnie zaktualizowane.");

                    new NotificationUtils().showNotificationAtDateTime(UpdateActivity.this,"Przypomnienie o: " + newName, newTime + "     " + newDate, newDate, newTime);

                    goToBack();


                }else{
                    new NotificationUtils().showNotification(UpdateActivity.this, "Zadanie nie zostało zaktualizowane", "\n" + "Twoje zadanie nie zostało zaktualizowane, ponieważ nie wypełniłeś wszystkich wymaganych pól.");

                }





            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:

                alertDeleteItem(idTaskStr);

                return true;

            case android.R.id.home:
                goToBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToBack();


    }


    public void alertDeleteItem(String task_id){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteTask(task_id);
                        new NotificationUtils().showNotification(UpdateActivity.this, "Zadanie "+nameTaskStr+" usunięte", "Zadanie "+nameTaskStr+" zostału usunięte pomyślnie.");
                        goToBack();
                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();

    }


    public void goToBack(){
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this, UpdateActivity.this, hour, minute, DateFormat.is24HourFormat(this));
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
