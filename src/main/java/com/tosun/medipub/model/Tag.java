package com.tosun.medipub.model;

import java.util.ArrayList;
import java.util.UUID;

public class Tag {

    private String tagID;
    private String customTagName;
    private String wikiTagName;
    private String wikiURL;
    private ArrayList<String> tagRelatedKeywords;

    public Tag(String customTagName) {
        this.customTagName = customTagName;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getCustomTagName() {
        return customTagName;
    }

    public String getWikiTagName() {
        return wikiTagName;
    }

    public void setWikiTagName(String wikiTagName) {
        this.wikiTagName = wikiTagName;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public void setWikiURL(String wikiURL) {
        this.wikiURL = wikiURL;
    }

    public ArrayList<String> getTagRelatedKeywords() {
        return tagRelatedKeywords;
    }

    public void setTagRelatedKeywords(ArrayList<String> tagRelatedKeywords) {
        this.tagRelatedKeywords = tagRelatedKeywords;
    }
}
