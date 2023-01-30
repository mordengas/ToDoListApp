package com.dominik_machnik.ToDoApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dominik_machnik.ToDoApp.R;
import com.dominik_machnik.ToDoApp.model.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    private List<Task> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private Context context;

    public TaskAdapter(Context context, List<Task> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Task task = mData.get(position);

        holder.name.setText(task.getName());
        holder.description.setText(task.getDescription());
        holder.date.setText(task.getDate());
        holder.time.setText(task.getTime());

        if (task.getStatus().equals("incomplete")){
            holder.statusText.setText(R.string.incomplete);
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.iconStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.orange_circle));
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.statusView)
        View statusView;

        @BindView(R.id.iconStatus)
        View iconStatus;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.statusText)
        TextView statusText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) mLongClickListener.onItemLongClick(v, getAdapterPosition());
            return false;
        }
    }

    public Task getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }



}
