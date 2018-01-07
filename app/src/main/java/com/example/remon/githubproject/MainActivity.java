package com.example.remon.githubproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.remon.githubproject.adapters.RecyclerAdapter;
import com.example.remon.githubproject.adapters.RecyclerItemClickListener;
import com.example.remon.githubproject.api.MySingleton;
import com.example.remon.githubproject.models.RepoModel;
import com.example.remon.githubproject.utiles.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<RepoModel> repoModels = new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private Context mContext;
    private int resultPerPage = 10;
    private int currentPageNumber = 1;
    LinearLayoutManager linearLayoutManager;
    private Boolean isLoading = false;
    private Boolean isLastPage = false;
    private ProgressBar footerBar;
    private String cacheFileName;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //path for cache file
        cacheFileName = String.format("%s/repo_list.dat", getCacheDir());
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        footerBar = (ProgressBar) findViewById(R.id.footerBar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerAdapter = new RecyclerAdapter(repoModels, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //RecyclerView.LayoutManager mLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        prepareData();
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                //Show loading footer
                footerBar.setVisibility(View.VISIBLE);
                isLoading = true;
                currentPageNumber += 1;

                //loadNextPage();
                addPageData(currentPageNumber);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //do nothing
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("")
                        .setMessage("What do you want to browse?")
                        .setPositiveButton(getResources().getString(R.string.RepositoryURL), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue to repo
                                String strRepoURL =  repoModels.get(position).getRepoURL();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(strRepoURL));
                                startActivity(newIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.OwnerURL), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue to user profile
                                String strOwnerURL =  repoModels.get(position).getOwnerURL();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(strOwnerURL));
                                startActivity(newIntent);
                                finish();
                            }
                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }));
    }

    private void addPageData(int pageNo){
        String URL = String.format("https://api.github.com/users/square/repos?page=%d&per_page=%d", pageNo, resultPerPage);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                int size = response.length();
                RepoModel currentRepo;
                String repoName;
                String ownerName;
                String description;
                JSONObject currentRepoJSON;
                isLoading = false;
                Boolean forkStatus;
                String repo_url;
                String owner_url;

                for (int i = 0; i < size; i++){
                    try {
                        currentRepoJSON = response.getJSONObject(i);
                        repoName = currentRepoJSON.getString("name");
                        ownerName = "square";
                        description = currentRepoJSON.getString("description");
                        forkStatus = currentRepoJSON.getBoolean("fork");
                        repo_url = currentRepoJSON.getString("html_url");
                        owner_url = currentRepoJSON.getJSONObject("owner").getString("html_url");
                        currentRepo = new RepoModel(repoName, description, ownerName, forkStatus, repo_url, owner_url);
                        repoModels.add(currentRepo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(size < resultPerPage){
                    isLastPage = true;
                }
                recyclerAdapter.notifyDataSetChanged();
                final Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footerBar.setVisibility(View.GONE);
                    }
                }, 500);
                // hide refresh animation (swipe layout) before making http call
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.getMessage());
            }
            });
        MySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
    }
    public void prepareData(){

        File cacheFile = new File(cacheFileName);
        if(cacheFile.exists()){
            loadData();
        }else{
            addPageData(1);
        }
    }
    private void saveData(){
        try {
            //Don't save the repos if cache was cleared and no data was loaded
            if(repoModels.size() == 0){
                return;
            }
            //to open file for writing
            //The method openFileOutput() returns an instance of FileOutputStream
            FileOutputStream fos = getApplicationContext().openFileOutput(cacheFileName, Context.MODE_PRIVATE);
            //to write objects in file
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.write(currentPageNumber);
            os.writeBoolean(isLastPage);
            os.writeObject(repoModels);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //load data from file
    private void loadData(){
        try {
            //to open file for reading
            //The method openFileOutput() returns an instance of FileOutputStream
            FileInputStream fis = getApplicationContext().openFileInput(cacheFileName);
            //to read objects from file
            ObjectInputStream is = new ObjectInputStream(fis);
            //currentLoadedPageNumber
            currentPageNumber = is.readInt();
            isLastPage = is.readBoolean();
            repoModels = (ArrayList<RepoModel>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);
        currentPageNumber = 1;
        isLastPage = false;
        isLoading = true;
        repoModels.clear();
        recyclerAdapter.notifyDataSetChanged();
        File cacheFile = new File(cacheFileName);
        if(cacheFile.exists()) {
            cacheFile.delete();
        }
        addPageData(1);
    }
}
