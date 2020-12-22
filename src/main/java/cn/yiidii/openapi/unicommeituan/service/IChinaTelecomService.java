package cn.yiidii.openapi.unicommeituan.service;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * 中国电信获取Cookie接口
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-20 22:26
 */
public interface IChinaTelecomService {

    /**
     * 获取验证码
     * @return
     */
    JSONObject getValidationCode();

}
