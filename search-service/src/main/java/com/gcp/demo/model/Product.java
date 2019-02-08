package com.gcp.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;


@Document(indexName = "product_catalog", type = "_doc")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private String id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String canonicalUrl;
    private String brand;
    private String skuCode;
    private String taxCategory;
    private String currencyCode;
    private String weight;
    private String length;
    private String width;
    private String height;
    private List<String> categories;
    private boolean shipEnabled;
    private boolean taxExempt;
    private boolean inventoryManaged;
    private int stockQuantity;
    private double price;
}
