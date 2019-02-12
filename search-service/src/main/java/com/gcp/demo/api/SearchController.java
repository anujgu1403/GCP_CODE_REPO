package com.gcp.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.model.FilterOptions;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@RestController
public class SearchController {

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private SearchService service;

    @GetMapping("/keywordsearch")
    @SneakyThrows
    public SearchResult executeKeywordSearch(final String keyword,
                                             final Optional<String> filterOptions) {
        return service.searchByKeyword(keyword, getFilterOptions(filterOptions));
    }

    @GetMapping("/search")
    @SneakyThrows
    public SearchResult executeGuidedSearch(final String category1, final String category2,
                                            final Optional<String> filterOptions) {
        return service.searchByCategory(category1, category2, getFilterOptions(filterOptions));
    }

    @GetMapping("/products/{productId}")
    @SneakyThrows
    public Product getProduct(@PathVariable String productId) {
        return service.getProduct(productId);
    }

    @SneakyThrows
    private Optional<FilterOptions> getFilterOptions(Optional<String> filterOptions) throws IOException {
        if (filterOptions == null || !filterOptions.isPresent())
            return null;
        return Optional.of(mapper.readValue(filterOptions.get(), FilterOptions.class));
    }

}
