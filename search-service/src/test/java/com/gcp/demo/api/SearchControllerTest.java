package com.gcp.demo.api;

import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.Facet;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    @Mock
    private SearchService mockService;
    @InjectMocks
    private SearchController controller;

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnKeyWordSearch() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(Collections.singletonMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(Collections.singletonMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByKeyword("a")).thenReturn(mockResponse);
        SearchResult actual = controller.keywordsearch("a");
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordContainsSTAR() throws Exception {
        when(mockService.searchByKeyword(anyString())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.keywordsearch("a*");
        verify(mockService, times(1)).searchByKeyword("a*");
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordNull() throws Exception {
        when(mockService.searchByKeyword(any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.keywordsearch(null);
        verify(mockService, times(1)).searchByKeyword(null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordEmpty() throws Exception {
        when(mockService.searchByKeyword(anyString())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.keywordsearch("");
        verify(mockService, times(1)).searchByKeyword("a*");
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1AndCat2() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(Collections.singletonMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(Collections.singletonMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory("a", "b")).thenReturn(mockResponse);
        SearchResult actual = controller.guidedSearch("a", "b");
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1Only() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(Collections.singletonMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(Collections.singletonMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory("a", null)).thenReturn(mockResponse);
        SearchResult actual = controller.guidedSearch("a", null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1AndCat2Empty() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(Collections.singletonMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(Collections.singletonMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory("a", "")).thenReturn(mockResponse);
        SearchResult actual = controller.guidedSearch("a", "");
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Empty() throws Exception {
        when(mockService.searchByCategory(eq(""), anyString())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.guidedSearch("", "X");
        verify(mockService, times(1)).searchByCategory("", "X");
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Null() throws Exception {
        when(mockService.searchByCategory(any(), anyString())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.guidedSearch(null, "X");
        verify(mockService, times(1)).searchByCategory(null, "X");
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategoriesNull() throws Exception {
        when(mockService.searchByCategory(any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.guidedSearch(null, null);
        verify(mockService, times(1)).searchByCategory(null, null);
    }
}