package com.gcp.demo.api;

import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.Facet;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
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
        when(mockService.searchByKeyword(eq("a"), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeKeywordSearch("a", null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordContainsSTAR() throws Exception {
        when(mockService.searchByKeyword(anyString(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch("a*", null);
        verify(mockService, times(1)).searchByKeyword("a*", null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordNull() throws Exception {
        when(mockService.searchByKeyword(any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch(null, null);
        verify(mockService, times(1)).searchByKeyword(null, null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordEmpty() throws Exception {
        when(mockService.searchByKeyword(anyString(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch("", null);
        verify(mockService, times(1)).searchByKeyword("a*", null);
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
        when(mockService.searchByCategory(eq("a"), eq("b"), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", "b",null);
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
        when(mockService.searchByCategory(eq("a"), any(), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", null, null);
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
        when(mockService.searchByCategory(eq("a"), eq(""), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", "", null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Empty() throws Exception {
        when(mockService.searchByCategory(eq(""), anyString(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch("", "X", null);
        verify(mockService, times(1)).searchByCategory("", "X", null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Null() throws Exception {
        when(mockService.searchByCategory(any(), anyString(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch(null, "X", null);
        verify(mockService, times(1)).searchByCategory(null, "X", null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategoriesNull() throws Exception {
        when(mockService.searchByCategory(any(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch(null, null,null);
        verify(mockService, times(1)).searchByCategory(null, null, null);
    }
}