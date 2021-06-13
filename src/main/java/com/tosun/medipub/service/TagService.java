package com.tosun.medipub.service;

import com.tosun.medipub.model.Tag;

public interface TagService {

    Tag createTag(String customTagName, String wikiName, String wikiURL, String articleID);

    boolean deleteTag();

}
