package com.gcp.demo.api;

import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService service;

    @GetMapping("/keywordsearch")
    @SneakyThrows
    public SearchResult keywordsearch(String keyword) {
        return service.searchByKeyword(keyword);
    }

    @GetMapping("/search")
    @SneakyThrows
    public SearchResult guidedSearch(String category1, String category2) {
        return service.searchByCategory(category1, category2);
    }
}
