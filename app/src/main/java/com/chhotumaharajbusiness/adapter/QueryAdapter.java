package com.chhotumaharajbusiness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.model.QueryModel;

import java.util.ArrayList;
import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.MyViewHolder> {

    private Context context;
    private List<QueryModel> queryModels;
    int pos;
    public static QueryAdapter.ClickListener clickListener;

    public static ArrayList<Integer> video_id = new ArrayList<>();

    public static ArrayList<Integer> getVideo_id() {
        return video_id;
    }

    public static void setVideo_id(ArrayList<Integer> video_id) {
        QueryAdapter.video_id = video_id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;


        public MyViewHolder(View view) {
            super(view);
           checkBox = view.findViewById(R.id.query_checkbox);

        }
    }
    public QueryAdapter(){

    }
    public QueryAdapter(Context context, List<QueryModel> queryModels) {
        this.context = context;
        this.queryModels = queryModels;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // creates view holder and inflates
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_list, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) { // binds/adds details to the views
        final QueryModel item = queryModels.get(position);

        holder.checkBox.setText(item.getSubQueryModels().get(position).getVideo_query());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    video_id.add(item.getSubQueryModels().get(position).getId());
                    Log.d("query....", String.valueOf(QueryAdapter.getVideo_id()));
                }
                else {
                    video_id.remove(video_id.indexOf(item.getSubQueryModels().get(position).getId()));
                    Log.d("query....", String.valueOf(QueryAdapter.getVideo_id()));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return queryModels.size();
    }

    public QueryModel getWordAtPosition(int position) {
        return queryModels.get(position);
    }

    public void setOnItemClickListener(QueryAdapter.ClickListener clickListener) {
        QueryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position, String flag);
    }

}