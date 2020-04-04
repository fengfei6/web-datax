package com.feng.project.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

public class XxlUtil {

    final static String url = "http://192.144.129.188:8081/xxl-job-admin";

    final static String username = "admin";

    final static String password = "123456";


    @PostConstruct
    public static RestTemplate before() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        return new RestTemplate(requestFactory);
    }


    private static JSONObject doPostFrom(String urlpath, MultiValueMap linkedMultiValueMap) {
        //  一定要设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(linkedMultiValueMap, headers);
        //"http://127.0.0.1:8081/xxl-job-admin/jobinfo/update"
        ResponseEntity<String> entity = before().postForEntity(urlpath, request, String.class);
        return JSONObject.parseObject(entity.getBody());
    }

    private static String doGetString(String urlpath) {
        ResponseEntity<String> exchange = before().getForEntity(urlpath, String.class);
        String r = exchange.getBody();
        return r;
    }


    private static String preparePara(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append('?');
        for (Map.Entry<String, String> m : map.entrySet()) {
            sb.append(m.getKey()).append("=").append(m.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }


    private static JSONObject doGet(String urlpath) {
        return JSONObject.parseObject(doGetString(urlpath));
    }


    public static JSONObject handleSubmitJobinfoWithAdd(MultiValueMap<String, String> linkedMultiValueMap) {
        String request_url = url.concat("/jobinfo/add");
        return doPostFrom(request_url,linkedMultiValueMap);
    }

    public static JSONObject handleSubmitJobinfoWithUpdate(MultiValueMap<String, String> linkedMultiValueMap) {
        String request_url = url.concat("/jobinfo/update");
        return doPostFrom(request_url,linkedMultiValueMap);
    }

    public static JSONObject onSchedulingWithCron(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/start").concat(preparePara(map));
        return doGet(request_url);

    }

    public static JSONObject onSchedulingWithDepend(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/startRely").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject cancelSchedulingWithCron(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/stop").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject cancelSchedulingWithDepend(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/stopRely").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject getHandleInfo(Map<String, String> map) {

        String request_url = url.concat("/joblog/getJobLog").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject getJobInfo(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/getJobinfo").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject getAllHandleInfo() {
        Map<String, String> map = Maps.newHashMap();

        String request_url = url.concat("/joblog/getAllJobLog").concat(preparePara(map));
        return doGet(request_url);

    }

    public static JSONObject getExeclog(Map<String, String> map) {

        String request_url = url.concat("/joblog/logDetailCat").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject executeUnder(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/trigger").concat(preparePara(map));
        return doGet(request_url);
    }
    public static JSONObject executeNoUnder(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/trigger/once").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject handleSubmitJobCode(MultiValueMap<String, String> linkedMultiValueMap) {
        String request_url = url.concat("/jobcode/save");
        return doPostFrom(request_url,linkedMultiValueMap);
    }

    public static JSONObject terminate(Map<String, String> map) {

        String request_url = url.concat("/joblog/logKill").concat(preparePara(map));
        return doGet(request_url);
    }

    public static JSONObject delete(Map<String, String> map) {

        String request_url = url.concat("/jobinfo/remove").concat(preparePara(map));
        return doGet(request_url);
    }
}
