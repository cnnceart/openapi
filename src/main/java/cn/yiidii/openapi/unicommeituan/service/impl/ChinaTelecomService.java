package cn.yiidii.openapi.unicommeituan.service.impl;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.unicommeituan.service.IChinaTelecomService;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中国电信获取Cookie服务
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-20 22:27
 */
@Service
@RequiredArgsConstructor
public class ChinaTelecomService implements IChinaTelecomService {

    private static final String URL_GET_VALIDATION_CODE = "https://appgo.189.cn:9200/query/getValidationCode";

    @Override
    public JSONObject getValidationCode() {
        return null;
    }
}
