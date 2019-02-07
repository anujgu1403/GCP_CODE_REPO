package com.gcp.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.demo.model.Facet;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Repository
@Slf4j
public class SearchRepository {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ElasticsearchTemplate searchTemplate;

    public SearchResult searchByKeyword(String keyword) {
        SearchQuery searchQuery = withDefaultAggregations(new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(keyword)
                        .field("name")
                        .field("brand")
                        .field("shortDescription")
                        .field("categories")
                        .field("attributes.name")
                        .field("attributes.value")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)))
                .build();

        SearchResult result = performSearchOperation(searchQuery);

        List<Product> products = searchTemplate.queryForList(searchQuery, Product.class);
        result.setProducts(products);
        return result;
    }


    public SearchResult searchByCategory(String category1, String category2) {
        SearchQuery searchQuery = withDefaultAggregations(new NativeSearchQueryBuilder()
                .withQuery(getCategoryMatchQuery(category1, category2))).build();

        SearchResult result = performSearchOperation(searchQuery);

        List<Product> products = searchTemplate.queryForList(searchQuery, Product.class);
        result.setProducts(products);
        return result;
    }

    private QueryBuilder getCategoryMatchQuery(String cat1, String cat2) {
        BoolQueryBuilder qb = QueryBuilders.boolQuery().must(termQuery("category1", cat1));
        if (!StringUtils.isEmpty(cat2)) {
            return qb.must(termQuery("category2", cat2));
        }
        return qb;
    }

    private SearchResult performSearchOperation(SearchQuery searchQuery) {
        return searchTemplate.query(searchQuery, response -> {
            SearchResult sr = new SearchResult();
            sr.setTotal(response.getHits().getTotalHits());
            SearchHit[] hits = response.getHits().getHits();
            sr.setProducts(Arrays.stream(hits).map(this::mapSearchHitToProduct).filter(Objects::nonNull).collect(Collectors.toList()));
            Optional.ofNullable(response.getAggregations()).ifPresent(aggregations -> {
                Map<String, Aggregation> aggs = aggregations.asMap();
                if (!CollectionUtils.isEmpty(aggs)) {
                    sr.setFacets(aggs.entrySet().stream()
                            .filter(entry -> entry.getValue() instanceof MultiBucketsAggregation)
                            .map(this::mapAggregationToFacet).collect(Collectors.toList()));
                }
            });
            return sr;
        });
    }

    private Facet mapAggregationToFacet(Map.Entry<String, Aggregation> entry) {
        MultiBucketsAggregation agg = (MultiBucketsAggregation) entry.getValue();
        Facet facet = new Facet();
        facet.setName(entry.getKey());
        facet.setBuckets(agg.getBuckets().stream().collect(Collectors.toMap(b -> b.getKeyAsString(), b -> b.getDocCount())));
        return facet;
    }

    private Product mapSearchHitToProduct(SearchHit hit) {
        Product product = null;
        String source = hit.getSourceAsString();
        try {
            product = objectMapper.readValue(source, Product.class);
        } catch (Exception e) {
            log.error("Error occurred while parsing search hit. Source: {}. Error={}", source, e);
        }
        return product;
    }

    private NativeSearchQueryBuilder withDefaultAggregations(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms("Category").field("category1").order(BucketOrder.key(true));
        TermsAggregationBuilder subCategoryAgg = AggregationBuilders.terms("Sub category").field("category2").order(BucketOrder.key(true));
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("Brand").field("brand").order(BucketOrder.key(true));
        TermsAggregationBuilder heightAgg = AggregationBuilders.terms("Height").field("height").order(BucketOrder.key(true));
        TermsAggregationBuilder widthAgg = AggregationBuilders.terms("Width").field("width").order(BucketOrder.key(true));
        TermsAggregationBuilder lengthAgg = AggregationBuilders.terms("Length").field("length").order(BucketOrder.key(true));
        TermsAggregationBuilder weightAgg = AggregationBuilders.terms("Weight").field("weight").order(BucketOrder.key(true));
        nativeSearchQueryBuilder
                .addAggregation(categoryAgg)
                .addAggregation(subCategoryAgg)
                .addAggregation(brandAgg)
                .addAggregation(heightAgg)
                .addAggregation(widthAgg)
                .addAggregation(lengthAgg)
                .addAggregation(weightAgg);
        return nativeSearchQueryBuilder;
    }
}
