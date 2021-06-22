package com.tosun.medipub.model;

import java.util.ArrayList;

public class AutoTag {

    private ArrayList<String> sementityList;
    private ArrayList<String> semldList;
    private ArrayList<String> verbList;
    private ArrayList<String> subjectlist;

    public AutoTag() {
    }

    public ArrayList<String> getSementityList() {
        return sementityList;
    }

    public void setSementityList(ArrayList<String> sementityList) {
        this.sementityList = sementityList;
    }

    public ArrayList<String> getSemldList() {
        return semldList;
    }

    public void setSemldList(ArrayList<String> semldList) {
        this.semldList = semldList;
    }

    public ArrayList<String> getVerbList() {
        return verbList;
    }

    public void setVerbList(ArrayList<String> verbList) {
        this.verbList = verbList;
    }

    public ArrayList<String> getSubjectlist() {
        return subjectlist;
    }

    public void setSubjectlist(ArrayList<String> subjectlist) {
        this.subjectlist = subjectlist;
    }
}
