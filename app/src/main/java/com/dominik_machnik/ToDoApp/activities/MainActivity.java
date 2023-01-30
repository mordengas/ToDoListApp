package com.dominik_machnik.ToDoApp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dominik_machnik.ToDoApp.R;
import com.dominik_machnik.ToDoApp.adapter.TaskAdapter;
import com.dominik_machnik.ToDoApp.database.DbHandler;

import com.dominik_machnik.ToDoApp.notifications.NotificationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.dominik_machnik.ToDoApp.model.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener, TaskAdapter.ItemLongClickListener {

    @BindView(R.id.imageView)
    ImageView empty;

    @BindView(R.id.textView2)
    TextView emptyText;

    @BindView(R.id.parentContainer)
    RelativeLayout parentContainer;

    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private DbHandler db;
    private ArrayList<Task> tasks;
    private TaskAdapter taskAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        db = new DbHandler(this);


    }


    @Override
    protected void onStart() {
        super.onStart();

        events();
        setData();
    }

    public void setData(){
        tasks = db.getAllTasks();
        if(tasks.isEmpty()){
            empty.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }else {
            empty.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);

        }
        taskAdapter = new TaskAdapter(this, tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        taskAdapter.setClickListener(this);
        taskAdapter.setLongClickListener(this);
        taskAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(taskAdapter);

    }


    public void events(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivity(intent);
                finish();
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

                    deleteAllData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void deleteAllData(){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog_all)
                .setMessage(R.string.message_alert_dialog_all)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteAllTasks();
                        tasks = db.getAllTasks();

                        if(tasks.isEmpty()){
                            empty.setVisibility(View.VISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                        }else {
                            empty.setVisibility(View.GONE);
                            emptyText.setVisibility(View.GONE);

                        }
                        taskAdapter = new TaskAdapter(MainActivity.this, tasks);
                        recyclerView.swapAdapter(taskAdapter, true);
                        new NotificationUtils().showNotification(MainActivity.this, "Usunięto Zadania", "Wszystkie zadania zostały pomyślnie usunięte.");


                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();



    }


    @Override
    public void onItemClick(View view, int position) {
        Task task = tasks.get(position);
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra("idTask", task.getId());
        intent.putExtra("nameTask", task.getName());
        intent.putExtra("descTask", task.getDescription());
        intent.putExtra("dateTask", task.getDate());
        intent.putExtra("timeTask", task.getTime());
        intent.putExtra("statusTask", task.getStatus());
        startActivity(intent);
        finish();

    }

    @Override
    public void onItemLongClick(View view, int position) {
        alertDeleteItem(tasks.get(position), position);
    }

    public void alertDeleteItem(Task task, int position){

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_alert_dialog)
                .setMessage(R.string.message_alert_dialog)
                .setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteTask(task.getId());
                        tasks.remove(position);
                        if(tasks.isEmpty()){
                            empty.setVisibility(View.VISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                        }else {
                            empty.setVisibility(View.GONE);
                            emptyText.setVisibility(View.GONE);

                        }

                        taskAdapter = new TaskAdapter(MainActivity.this, tasks);
                        recyclerView.swapAdapter(taskAdapter, true);
                        new NotificationUtils().showNotification(MainActivity.this, "Zadanie "+ task.getName()+" zostało usunięte", "Zadanie "+ task.getName()+" zostało pomyślnie usunięte.");


                    }
                })
                .setNegativeButton(R.string.button_negative, null)
                .setIcon(R.drawable.ic_alert)
                .show();

    }


}

