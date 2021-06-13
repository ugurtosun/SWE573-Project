package com.tosun.medipub.controller;

import com.tosun.medipub.model.Tag;
import com.tosun.medipub.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("tags")
public class TagController {

    TagService tagService;

    public TagController() {}

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(path = "/createTag")
    public Tag createTag(@RequestParam(name = "customTagName", required = true) String customTagName,
                         @RequestParam(name = "wikiTagName", required = false) String wikiTagName,
                         @RequestParam(name = "wikiURL", required = false) String wikiURL,
                         @RequestParam(name = "articleID", required = false) String articleID){

        return tagService.createTag(customTagName, wikiTagName, wikiURL, articleID);
    }


}
