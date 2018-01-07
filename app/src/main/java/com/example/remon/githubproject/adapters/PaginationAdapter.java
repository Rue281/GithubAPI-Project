package com.example.remon.githubproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.remon.githubproject.R;
import com.example.remon.githubproject.models.RepoModel;

import java.util.ArrayList;

/**
 * Created by Remon on 1/4/2018.
 */

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.ViewHolder>{
    ArrayList<RepoModel> repoModels;
    Context context;
    public PaginationAdapter(ArrayList<RepoModel> repoModels, Context context){
        this.repoModels = repoModels;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtRepoName.setText(repoModels.get(position).getRepoName());
    }

    @Override
    public int getItemCount() {
        return repoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRepoName;
        public ViewHolder(View itemView) {
            super(itemView);
            txtRepoName = itemView.findViewById(R.id.txtRepoName);
        }
    }
}
