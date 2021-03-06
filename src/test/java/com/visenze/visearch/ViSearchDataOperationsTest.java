package com.visenze.visearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.visenze.visearch.internal.DataOperations;
import com.visenze.visearch.internal.DataOperationsImpl;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import com.visenze.visearch.internal.json.ViSearchModule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ViSearchDataOperationsTest {

    private ViSearchHttpClient mockClient;
    private ObjectMapper objectMapper;

    @Before
    public void beforeTest() {
        mockClient = mock(ViSearchHttpClient.class);
        objectMapper = new ObjectMapper().registerModule(new ViSearchModule());
    }

    @Test
    public void testInsertParams() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("test-param", "123");
        when(mockResponse.getHeaders()).thenReturn(responseHeaders);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        Image image0 = new Image("test_im_0", "http://www.example.com/test_im_0.jpeg");
        Map<String, String> image1Metadata = Maps.newHashMap();
        image1Metadata.put("field_int", "99");
        image1Metadata.put("field_float", "1.0");
        image1Metadata.put("field_string", "visearch");
        image1Metadata.put("field_text", "java sdk");
        Image image1 = new Image("test_im_1", "http://www.example.com/test_im_1.jpeg", image1Metadata);
        imageList.add(image0);
        imageList.add(image1);
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(responseHeaders, insertTrans.getHeaders());
        assertEquals(null, insertTrans.getErrorMessage());
        assertEquals(null, insertTrans.getCause());
        assertEquals(null, insertTrans.getRawResponseMessage());
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name[0]", "test_im_0");
        expectedParams.put("im_url[0]", "http://www.example.com/test_im_0.jpeg");
        expectedParams.put("im_name[1]", "test_im_1");
        expectedParams.put("im_url[1]", "http://www.example.com/test_im_1.jpeg");
        expectedParams.put("field_int[1]", "99");
        expectedParams.put("field_float[1]", "1.0");
        expectedParams.put("field_string[1]", "visearch");
        expectedParams.put("field_text[1]", "java sdk");
        verify(mockClient).post("/insert", expectedParams);
    }

    @Test
    public void testInsertCustomParams() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":1}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        Image image0 = new Image("test_im_0", "http://www.example.com/test_im_0.jpeg");
        imageList.add(image0);
        Map<String, String> customParam = Maps.newHashMap();
        customParam.put("custom_param", "custom_value");
        InsertTrans insertTrans = dataOperations.insert(imageList, customParam);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name[0]", "test_im_0");
        expectedParams.put("im_url[0]", "http://www.example.com/test_im_0.jpeg");
        expectedParams.put("custom_param", "custom_value");
        verify(mockClient).post("/insert", expectedParams);
    }

    @Test
    public void testInsertStatusParams() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("test-param", "123");
        when(mockResponse.getHeaders()).thenReturn(responseHeaders);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(responseHeaders, insertStatus.getHeaders());
        verify(mockClient).get("/insert/status/317503499455827968", HashMultimap.<String, String>create());
    }

    @Test
    public void testInsertStatusParamsWithCustomParams() {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("test-param", "123");
        when(mockResponse.getHeaders()).thenReturn(responseHeaders);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        Map<String, String> customParam = Maps.newHashMap();
        customParam.put("custom_param", "custom_value");
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968", customParam);
        assertEquals(responseHeaders, insertStatus.getHeaders());
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("custom_param", "custom_value");
        verify(mockClient).get("/insert/status/317503499455827968", expectedParams);
    }

    @Test
    public void testRemoveParams() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"remove\",\"result\":[],\"error\":[],\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        dataOperations.remove(imNameList);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("im_name[0]", "test_im_0");
        expectedParams.put("im_name[1]", "test_im_1");
        verify(mockClient).post("/remove", expectedParams);
    }

    @Test
    public void testRemoveNoStatus() throws Exception {
        String insertResponse = "{\"method\":\"remove\",\"result\":[],\"error\":[],\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        RemoveStatus removeStatus = dataOperations.remove(imNameList);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), removeStatus.getErrorMessage());
        assertEquals(insertResponse, removeStatus.getRawResponseMessage());
    }

    @Test
    public void testRemoveNoTotal() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"remove\",\"result\":[],\"error\":[]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        RemoveStatus removeStatus = dataOperations.remove(imNameList);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), removeStatus.getErrorMessage());
        assertEquals(insertResponse, removeStatus.getRawResponseMessage());
    }

    @Test
    public void testRemoveFailNoError() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"method\":\"remove\",\"result\":[],\"error\":[],\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        RemoveStatus removeStatus = dataOperations.remove(imNameList);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), removeStatus.getErrorMessage());
        assertEquals(insertResponse, removeStatus.getRawResponseMessage());
    }

    @Test
    public void testRemoveFailWithError() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"method\":\"remove\",\"result\":[],\"error\":[\"Error Message\"],\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        RemoveStatus removeStatus = dataOperations.remove(imNameList);
        assertEquals("Error Message", removeStatus.getErrorMessage());
        assertEquals(insertResponse, removeStatus.getRawResponseMessage());
    }

    @Test
    public void testRemoveJsonFormatError() throws Exception {
        String insertResponse = "{\"status\":\"fail\" \"method\":\"remove\",\"result\":[],\"error\":[\"Error Message\"],\"total\":2}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<String> imNameList = Lists.newArrayList("test_im_0", "test_im_1");
        RemoveStatus removeStatus = dataOperations.remove(imNameList);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), removeStatus.getErrorMessage());
        assertEquals(insertResponse, removeStatus.getRawResponseMessage());
    }

    @Test
    public void testInsertStatusErrorPageParams() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":1,\"fail_count\":1,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968", 1, 10);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("error_page", "1");
        expectedParams.put("error_limit", "10");
        verify(mockClient).get("/insert/status/317503499455827968", expectedParams);
    }

    @Test
    public void testInsertStatusErrorPageParamsWithCustomParameter() {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":1,\"fail_count\":1,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        Map<String, String> customParam = Maps.newHashMap();
        customParam.put("custom_param", "custom_value");
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968", 1, 10, customParam);
        Multimap<String, String> expectedParams = HashMultimap.create();
        expectedParams.put("error_page", "1");
        expectedParams.put("error_limit", "10");
        expectedParams.put("custom_param", "custom_value");
        verify(mockClient).get("/insert/status/317503499455827968", expectedParams);
    }

    @Test
    public void testInsertNoImage() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"error_code\":104,\"error_message\":\"No image inserted.\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(0), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(104), error.getErrorCode());
        assertEquals("No image inserted.", error.getErrorMessage());
    }

    @Test
    public void testInsertOneImageSuccess() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":1}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(1), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNull(insertTrans.getErrorList());
    }

    @Test
    public void testInsertOneImageNoStatus() throws Exception {
        String insertResponse = "{\"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":1}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), insertTrans.getErrorMessage());
        assertEquals(insertResponse, insertTrans.getRawResponseMessage());
    }

    @Test
    public void testInsertOneImageErrorJsonFormat() throws Exception {
        String insertResponse = "{\"status\":\"OK\" \"trans_id\":317503499455827968,\"method\":\"insert\",\"total\":1}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), insertTrans.getErrorMessage());
        assertEquals(insertResponse, insertTrans.getRawResponseMessage());
    }

    @Test
    public void testInsertOneImageFail() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"index\":0,\"im_name\":\"test im\",\"error_code\":107,\"error_message\":\"Invalid im_name.\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(0), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(0), error.getIndex());
        assertEquals(new Integer(107), error.getErrorCode());
        assertEquals("Invalid im_name.", error.getErrorMessage());
    }

    @Test
    public void testInsertMultipleImagesMix() throws Exception {
        String insertResponse = "{\"status\":\"warning\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":1,\"error\":[{\"index\":1,\"im_name\":\"test im\",\"error_code\":107,\"error_message\":\"Invalid im_name.\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(new Integer(1), insertTrans.getTotal());
        assertEquals("317503499455827968", insertTrans.getTransId());
        assertNotNull(insertTrans.getErrorList());
        List<InsertError> errorList = insertTrans.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals(new Integer(107), error.getErrorCode());
        assertEquals("Invalid im_name.", error.getErrorMessage());
    }

    @Test
    public void testInsertStatusFail() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":0,\"fail_count\":2,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im_0\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"},{\"im_name\":\"test_im_1\",\"error_code\":202,\"error_message\":\"Unsupported image format.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(0), insertStatus.getSuccessCount());
        assertEquals(new Integer(2), insertStatus.getFailCount());
        assertEquals(new Integer(1), insertStatus.getErrorPage());
        assertEquals(new Integer(10), insertStatus.getErrorLimit());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertNotNull(insertStatus.getErrorList());
        List<InsertError> errorList = insertStatus.getErrorList();
        assertEquals(2, errorList.size());
        InsertError error0 = errorList.get(0);
        assertEquals("test_im_0", error0.getImName());
        assertEquals(new Integer(201), error0.getErrorCode());
        assertEquals("Could not download the image from im_url.", error0.getErrorMessage());
        InsertError error1 = errorList.get(1);
        assertEquals("test_im_1", error1.getImName());
        assertEquals(new Integer(202), error1.getErrorCode());
        assertEquals("Unsupported image format.", error1.getErrorMessage());
    }

    @Test
    public void testInsertStatusSuccess() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(2), insertStatus.getSuccessCount());
        assertEquals(new Integer(0), insertStatus.getFailCount());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertNull(insertStatus.getErrorPage());
        assertNull(insertStatus.getErrorLimit());
        assertNull(insertStatus.getErrorList());
    }

    @Test
    public void testInsertStatusNoStatus() throws Exception {
        String insertResponse = "{\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":2,\"fail_count\":0,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), insertStatus.getErrorMessage());
        assertEquals(insertResponse, insertStatus.getRawResponseMessage());
    }

    @Test
    public void testInsertStatusMixed() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":1,\"fail_count\":1,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(1), insertStatus.getSuccessCount());
        assertEquals(new Integer(1), insertStatus.getFailCount());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertEquals(new Integer(1), insertStatus.getErrorPage());
        assertEquals(new Integer(10), insertStatus.getErrorLimit());
        assertNotNull(insertStatus.getErrorList());
        List<InsertError> errorList = insertStatus.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals("test_im", error.getImName());
        assertEquals(new Integer(201), error.getErrorCode());
        assertEquals("Could not download the image from im_url.", error.getErrorMessage());
    }

    @Test
    public void testInsertStatusErrorPage() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":1,\"fail_count\":1,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"}],\"error_page\":2,\"error_limit\":1}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968", 2, 1);
        assertEquals("317503499455827968", insertStatus.getTransId());
        assertEquals(new Integer(100), insertStatus.getProcessedPercent());
        assertEquals(new Integer(2), insertStatus.getTotal());
        assertEquals(new Integer(1), insertStatus.getSuccessCount());
        assertEquals(new Integer(1), insertStatus.getFailCount());
        assertTrue(insertStatus.getStartTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertTrue(insertStatus.getUpdateTime().equals(new DateTime("2015-01-02T03:04:05.678+0000").toDate()));
        assertEquals(new Integer(2), insertStatus.getErrorPage());
        assertEquals(new Integer(1), insertStatus.getErrorLimit());
        assertNotNull(insertStatus.getErrorList());
        List<InsertError> errorList = insertStatus.getErrorList();
        assertEquals(1, errorList.size());
        InsertError error = errorList.get(0);
        assertEquals("test_im", error.getImName());
        assertEquals(new Integer(201), error.getErrorCode());
        assertEquals("Could not download the image from im_url.", error.getErrorMessage());
    }

    public void testInsertResponseMissingStatus() throws Exception {
        String insertResponse = "{\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"error_code\":104,\"error_message\":\"No image inserted.\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), insertTrans.getErrorMessage());
    }

    public void testInsertResponseMalformed0() throws Exception {
        String insertResponse = "{\"status\":\"fail\" \"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":[{\"error_code\":104,\"error_message\":\"No image inserted.\"}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), insertTrans.getErrorMessage());
    }

    public void testInsertResponseMalformed1() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"trans_id\":317503499455827968,\"method\":\"insert\"," +
                "\"total\":0,\"error\":{\"error_code\":104,\"error_message\":\"No image inserted.\"}}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.post(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        List<Image> imageList = new ArrayList<Image>();
        InsertTrans insertTrans = dataOperations.insert(imageList);
        assertEquals("No image inserted.", insertTrans.getErrorMessage());
        assertEquals(insertResponse, insertTrans.getRawResponseMessage());
    }

    public void testInsertStatusNotFound() throws Exception {
        String insertResponse = "{\"status\":\"fail\",\"method\":\"insert/status\",\"result\":[],\"error\":[\"Unable to find trans_id\"]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals("Unable to find trans_id", insertStatus.getErrorMessage());
        assertEquals(null, insertStatus.getCause());
        assertEquals(insertResponse, insertStatus.getRawResponseMessage());
    }

    public void testhttpResponseMissingStatus() throws Exception {
        String insertResponse = "{\"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":0,\"fail_count\":2,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im_0\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"},{\"im_name\":\"test_im_1\",\"error_code\":202,\"error_message\":\"Unsupported image format.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(ResponseMessages.INVALID_RESPONSE_FORMAT.getMessage(), insertStatus.getErrorMessage());
    }

    public void testhttpResponseMalformed0() throws Exception {
        String insertResponse = "{\"status\":\"OK\" \"method\":\"insert/status\",\"result\":[{\"trans_id\":317503499455827968,\"processed_percent\":100,\"total\":2,\"success_count\":0,\"fail_count\":2,\"start_time\":\"2015-01-02T03:04:05.678+0000\",\"update_time\":\"2015-01-02T03:04:05.678+0000\",\"error_list\":[{\"im_name\":\"test_im_0\",\"error_code\":201,\"error_message\":\"Could not download the image from im_url.\"},{\"im_name\":\"test_im_1\",\"error_code\":202,\"error_message\":\"Unsupported image format.\"}],\"error_page\":1,\"error_limit\":10}]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), insertStatus.getErrorMessage());
    }

    public void testhttpResponseMalformed1() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":[]}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), insertStatus.getErrorMessage());
    }

    public void testhttpResponseMalformed2() throws Exception {
        String insertResponse = "{\"status\":\"OK\",\"method\":\"insert/status\",\"result\":{}}";
        ViSearchHttpResponse mockResponse = mock(ViSearchHttpResponse.class);
        when(mockResponse.getBody()).thenReturn(insertResponse);
        when(mockClient.get(anyString(), Matchers.<Multimap<String, String>>any())).thenReturn(mockResponse);
        DataOperations dataOperations = new DataOperationsImpl(mockClient, objectMapper);
        InsertStatus insertStatus = dataOperations.insertStatus("317503499455827968");
        assertEquals(ResponseMessages.PARSE_RESPONSE_ERROR.getMessage(), insertStatus.getErrorMessage());
    }
}
