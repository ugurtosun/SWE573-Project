package com.tosun.medipub.model;

import java.util.ArrayList;
import java.util.UUID;

public class Tag {

    private String tagID;
    private String customTagName;
    private String customDescription;
    private String label;
    private String url;
    private String articleID;
    private String wikiID;

    public Tag(String customTagName) {
        this.customTagName = customTagName;
    }

    public Tag(){

    }

    public void setCustomTagName(String customTagName) {
        this.customTagName = customTagName;
    }

    public String getCustomDescription() {
        return customDescription;
    }

    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getWikiID() {
        return wikiID;
    }

    public void setWikiID(String wikiID) {
        this.wikiID = wikiID;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
