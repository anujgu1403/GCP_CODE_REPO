package com.gcp.demo.service;

import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchService {
    private SearchRepository repository;
    private Pattern allowedPattern;

    @Autowired
    public SearchService(SearchRepository repository, @Value("${keyword-search.allowed-pattern}") String keywordPattern) {
        this.repository = repository;
        this.allowedPattern = Pattern.compile(keywordPattern, Pattern.CASE_INSENSITIVE);
    }


    public SearchResult searchByKeyword(String keyword) throws InvalidInputException {
        if (keyword != null && allowedPattern.matcher(keyword).matches()) {
            return repository.searchByKeyword(keyword);
        } else {
            throw new InvalidInputException("Keyword: '" + keyword + "' contains invalid characters or empty");
        }
    }

    public SearchResult searchByCategory(String category1, String category2) throws InvalidInputException {
        if (!StringUtils.isEmpty(category1)) {
            return repository.searchByCategory(category1, category2);
        } else {
            throw new InvalidInputException("Category1 should not be empty");
        }

    }
}
