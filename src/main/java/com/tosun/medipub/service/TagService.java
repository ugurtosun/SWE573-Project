package com.tosun.medipub.service;


public interface TagService {

    boolean createTag(String customTagName, String customDescription, String wikiTagName
                                , String wikiID, String  wikiURL, String articleID);

}
