package com.tosun.medipub.controller;


import com.tosun.medipub.model.AutoTag;
import com.tosun.medipub.service.AutoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("autotag")
public class AutoTagController {

    AutoTagService autoTagService;

    public AutoTagController() {
    }

    @Autowired
    public AutoTagController(AutoTagService autoTagService) {
        this.autoTagService = autoTagService;
    }

    @GetMapping(path = "/fetch")
    public AutoTag createTag(@RequestParam(name = "articleID", required = false) String articleID){

        return autoTagService.autoTagArticle(articleID);

    }
}
