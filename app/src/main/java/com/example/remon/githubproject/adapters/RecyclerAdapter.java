package com.example.remon.githubproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.remon.githubproject.R;
import com.example.remon.githubproject.models.RepoModel;

import java.util.ArrayList;

/**
 * Created by Remonda on 6/15/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    ArrayList<RepoModel> repoModels;
    Context context;
    LinearLayout layoutLinear;
    public RecyclerAdapter(ArrayList<RepoModel> repoModels, Context context){
        this.repoModels = repoModels;
        this.context = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txtRepoName.setText(repoModels.get(position).getRepoName());
        holder.txtRepoDescription.setText(repoModels.get(position).getRepoDescription());
        holder.txtOwnerName.setText(repoModels.get(position).getOwnerName());
        Boolean forkStatus = repoModels.get(position).getForkStatus();
        //holder.txtForkStatus.setText(repoModels.get(position).getForkStatus().toString());
        //TODO:SET background color
        if (!forkStatus){
            layoutLinear.setBackgroundResource(R.color.lightGreen);
        }else{
            layoutLinear.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return repoModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtRepoName,txtRepoDescription,txtOwnerName,txtForkStatus;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtRepoName = itemView.findViewById(R.id.txtRepoName);
            txtRepoDescription = itemView.findViewById(R.id.txtRepoDescription);
            txtOwnerName = itemView.findViewById(R.id.txtOwnerName);
            layoutLinear = itemView.findViewById(R.id.layoutLinear);
//            txtForkStatus = itemView.findViewById(R.id.forkStatus);
        }
    }
}
