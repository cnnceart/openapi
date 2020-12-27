package cn.yiidii.openapi.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Server酱通知
 */
@Component
@Slf4j
public class ServerJNotify {

    @Autowired
    HttpClientUtil httpClientUtil;

    public void triggerNotify(String scKey, String text, String desp) {
        try {
            String url = "https://sc.ftqq.com/" + scKey + ".send";
            Map<String, String> params = new HashMap<>();
            params.put("text", text);
            params.put("desp", desp);
            httpClientUtil.doPost(url, params);
        } catch (Exception e) {
            log.info("{}", e);
        }
    }

}
