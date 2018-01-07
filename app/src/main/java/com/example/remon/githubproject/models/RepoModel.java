package com.example.remon.githubproject.models;

/**
 * Created by Remon on 1/4/2018.
 */

public class RepoModel {

    //int id;
    private String repoName;
    private String repoDescription;
    private String ownerName;
    private Boolean forkStatus = false;
    private String repoURL;

    public Boolean getForkStatus() {
        return forkStatus;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public String getOwnerURL() {
        return ownerURL;
    }

    private String ownerURL;

    public RepoModel( String repoName, String repoDescription, String ownerName, Boolean forkStatus, String repoURL, String ownerURL){
        //this.id = id;
        this.repoName = repoName;
        this.repoDescription = repoDescription;
        this.ownerName = ownerName;
        this.forkStatus = forkStatus;
        this.repoURL = repoURL;
        this.ownerURL = ownerURL;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDescription() {
        return repoDescription;
    }

    public void setRepoDescription(String repoDescription) {
        this.repoDescription = repoDescription;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
