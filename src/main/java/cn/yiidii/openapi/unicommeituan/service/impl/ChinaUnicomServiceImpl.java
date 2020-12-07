package cn.yiidii.openapi.unicommeituan.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.IPUtil;
import cn.yiidii.openapi.common.util.RSAUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomLoginForm;
import cn.yiidii.openapi.unicommeituan.controller.form.TelecomForm;
import cn.yiidii.openapi.unicommeituan.service.ChinaUnicomService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-11-26 13:17
 */
@Service
@Slf4j
public class ChinaUnicomServiceImpl implements ChinaUnicomService {

    @Autowired
    private IPUtil ipUtil;

    @Autowired
    private HttpServletRequest request;

    private static final String DONT_TIP = "DONT_TIP";

    /**
     * 加密公钥
     */
    private static final String CHINA_UNICOM_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDc+CZK9bBA9IU+gZUOc6FUGu7yO9WpTNB0PzmgFBh96Mg1WrovD1oqZ+eIF4LjvxKXGOdI79JRdve9NPhQo07+uqGQgE4imwNnRx7PFtCRryiIEcUoavuNtuRVoBAm6qdB0SrctgaqGfLgKvZHOnwTjyNqjBUxzMeQlEC2czEMSwIDAQAB";
    /**
     * 获取验证码接口
     */
    private static final String URL_SEND_RANDOM_NUM = "https://m.client.10010.com/mobileService/sendRadomNum.htm";
    /**
     * 登陆接口
     */
    private static final String URL_RANDOM_LOGIN = "https://m.client.10010.com/mobileService/radomLogin.htm";
    /**
     * 查询用户信息(首页)
     */
    private static final String URL_QUERY_USER_INFO_HOME_PAGE = "https://m.client.10010.com/mobileService/home/queryUserInfoSeven.htm";

    /**
     * 查询用户信息
     */
    private static final String URL_QUERY_USER_INFO = "https://m.client.10010.com/mobileservicequery/operationservice/getUserinfo";

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Override
    public String sendRandomNum(String mobile) throws ServiceException {
        // 随机数
        int randomSixCode = (int) ((Math.random() * 9 + 1) * 100000);
        String encryptMobile = null;

        // 拿公钥对（mobile + 随机数）进行RSA加密
        try {
            encryptMobile = RSAUtil.encryptByPubKey(mobile + randomSixCode, CHINA_UNICOM_PUBLIC_KEY);
        } catch (Exception e) {
            throw new ServiceException("加密手机号时间异常");
        }

        // 请求参数
        Map<String, String> params = new HashMap<>(16);
        params.put("mobile", encryptMobile);
        params.put("version", "android@7.0601");
        params.put("keyVersion", "");

        // 请求验证码接口
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = httpClientUtil.doWWWFormUrlencodePost(URL_SEND_RANDOM_NUM, null, params);
        } catch (Exception e) {
            throw new ServiceException("请求验证码时异常");
        }

        // 解析响应
        int code = httpClientResult.getCode();
        if (code != 200) {
            throw new ServiceException("请求验证码失败");
        }

        JSONObject randomResp = null;
        try {
            randomResp = JSONObject.parseObject(httpClientResult.getContent());
        } catch (Exception e) {
            throw new ServiceException("请求验证码失败");
        }
        String rspCode = randomResp.getString("rsp_code");
        String rspDesc = randomResp.getString("rsp_desc");
        if (!StringUtils.equals(rspCode, "0000")) {
            throw new ServiceException(rspDesc);
        }
        this.printLog(mobile, "成功发送了联通验证码");
        return rspDesc;
    }

    @Override
    public Result randomLogin(ChinaUnicomLoginForm chinaUnicomLoginForm) throws ServiceException {
        String mobile = chinaUnicomLoginForm.getMobile();
        String password = chinaUnicomLoginForm.getPassword();
        String imei = chinaUnicomLoginForm.getImei();
        String userContent = chinaUnicomLoginForm.getUserContent();
        String imageId = chinaUnicomLoginForm.getImageId();
        imei = StringUtils.isBlank(imei) ? "7865969553f94e9c9fe6654e89cbefc0" : imei;
        int randomSixCode = (int) ((Math.random() * 9 + 1) * 100000);
        String encryptMobile = null;
        String encryptPwd = null;
        try {
            encryptMobile = RSAUtil.encryptByPubKey(mobile + randomSixCode, CHINA_UNICOM_PUBLIC_KEY);
            encryptPwd = RSAUtil.encryptByPubKey(password + randomSixCode, CHINA_UNICOM_PUBLIC_KEY);
        } catch (Exception e) {
            throw new ServiceException("加密出现异常");
        }

        Map<String, String> params = new HashMap<>(16);
        params.put("mobile", encryptMobile);
        params.put("password", encryptPwd);
        params.put("loginStyle", "0");
        params.put("isRemberPwd", "false");
        params.put("provinceChanel", "general");
        params.put("timestamp", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
        params.put("yw_code", "");
        params.put("deviceOS", "android10");
        params.put("netWay", "WIFI");
        params.put("deviceCode", imei);
        params.put("version", "android@7.0601");
        params.put("deviceId", imei);
        params.put("keyVersion", "");
        params.put("pip", ipUtil.getRandomIp());
        params.put("voice_code", "");
        params.put("appId", "ChinaunicomMobileBusiness");
        params.put("voiceoff_flag", "");
        params.put("deviceModel", "OnePlus");
        params.put("deviceBrand", "ONEPLUS A6000");

        if (StringUtils.isNotBlank(userContent) || StringUtils.isNotBlank(imageId)) {
            params.put("userContent", userContent);
            params.put("imageID", imageId);
            params.put("code", "7");
        }

        HttpClientResult loginResp = null;
        try {
            loginResp = new HttpClientUtil().doWWWFormUrlencodePost(URL_RANDOM_LOGIN, null, params);
        } catch (Exception e) {
            throw new ServiceException("登陆异常: " + e.getMessage());
        }

        // 解析响应
        int code = loginResp.getCode();
        if (code != 200) {
            throw new ServiceException(String.format("登陆失败(%s)", code));
        }

        JSONObject loginRespJo = null;
        try {
            loginRespJo = JSONObject.parseObject(loginResp.getContent());
        } catch (Exception e) {
            throw new ServiceException("请求验证码失败");
        }
        String rspCode = loginRespJo.getString("code");
        String rspDesc = loginRespJo.getString("dsc");
        log.info("{}登陆响应: {}", mobile, loginRespJo);
        if (StringUtils.equals(rspCode, "7")) {
            // 需要手动输入图片验证码
            return Result.success(loginRespJo, "请输入图片验证码");
        }
        if (!StringUtils.equals(rspCode, "0")) {
            throw new ServiceException(StringUtils.isBlank(rspDesc) ? "登陆失败" : rspDesc);
        }
        JSONObject resultJo = new JSONObject();
        resultJo.put("chinaUnicomResp", loginRespJo);
        resultJo.put("cookieStr", loginResp.getCookieStr());
        resultJo.put("cookieMap", loginResp.getCookieMap());
        this.printLog(mobile, "成功获取了联通Cookie");
        return Result.success(resultJo, "登陆成功");
    }

    public static void main(String[] args) {
        TelecomForm telecomForm = new TelecomForm();
        telecomForm.setMobile("15600015800");
        telecomForm.setCookie("on_info=b7ebdc40ed1791ad2f844a90b2dc06c5; u_type=11; mobileServiceAll=728a8c6bd28a4230ddb4b8c3e1caec08; route=751df16222283a79398e5c964ba647a1; mobileService1=zaYZNeu_sQP075PdUpKgATXLsLz8j-v0SwKxdObrKR6b9aGD5NO_!-1383246902; a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDczNDgzODYsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NjAwMDE1ODAwIiwicmFuZG9tU3RyIjoieWg0MGE5NWMxNjA2NzQzNTg2In0sImlhdCI6MTYwNjc0MzU4Nn0.GooMZh8tFsuT_0DeW62TjhMtYFn23-AcuslhexTQy3aYZqs8-5Wx4ExGUNrp0NXTRKpTki-miA63WoTYfIZT3w; c_id=7634d33a3ca6ce48a2f1128abb3a6397b9acafaddf9b8d45713a9325e841c793; login_type=06; login_type=06; u_account=15600015800; city=011|110|90356344|-99; c_version=android@7.0601; c_version=android@7.0601; d_deviceCode=7865969553f94e9c9fe6654e89cbefc0; enc_acc=sQfiZ/aNK1U/688zYkP5aSRilTA420WsBgD2JyBd7/n1cTFUqtt1Bs9QrUeykPtA9KRPZ9f6PWqs7wl0h5Q8yc8cPvvT9yY21eYVcG644+oBMJDFlNxWHygyCGTgZdqEGG2FE6YHLM+HBO5C7e/+mGaSxXbRi9lh2SYI50pSosM=; ecs_acc=sQfiZ/aNK1U/688zYkP5aSRilTA420WsBgD2JyBd7/n1cTFUqtt1Bs9QrUeykPtA9KRPZ9f6PWqs7wl0h5Q8yc8cPvvT9yY21eYVcG644+oBMJDFlNxWHygyCGTgZdqEGG2FE6YHLM+HBO5C7e/+mGaSxXbRi9lh2SYI50pSosM=; random_login=0; cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83ab3a3319edad000874a7032c84921f4bbc54a6fb938fa52a78d8c60cae61c54f6; t3_token=236f5181f6ae76552ee98f377e7e8fbf; invalid_at=d0f43d8c110c2a5db929ac6c669d9c69892e878629499bf8ba97e53f0856b416; c_mobile=15600015800; wo_family=0; u_areaCode=110; third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDJjNmY3YjRkOTk2Njk1YzdmMDE4ZTcwNDM1OTg1ZDg3OTk3MTI1YWRhM2RkNjExOWJmMWY1ZmFiY2M3ZmEzMGFhYTBjZGVjYTFiZjcyNzA0Yzc5YmY5YmVmM2VhZTNlN2ZlYTU4M2I2YWMwYzMxZGM3M2ZhNWUyNjM4ZDk2MmFlYSIsInZlcnNpb24iOiIwMCJ9; ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDZmNDdiNWZiYjQyMDk3YzNiNzZjMDE3MjEwYzg3MWM5NzM0ZDZhMzgzYjE2ODc5YzllODM3ZjliOGY2ZTI4ZmZkM2FlNTQ1NjFhMmJhM2ZlYzFhYTQwOTIxNWVjMWE5ZTM0Zjk3NDVmYjI0MWRiZWJkNzIxY2QwY2JjZjJiNzZlIiwidmVyc2lvbiI6IjAwIn0=; jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTYwMDAxNTgwMCIsInBybyI6IjAxMSIsImNpdHkiOiIxMTAiLCJpZCI6IjQ3ODZkMTBiZmQxMzE4NzU5ODg5NThiZWIzYmNiYWU3In0.h6qN44jwkC57Sa2Hj2pJh_38glfUYB6_n_nmEjIN7tY; ecs_acc=sQfiZ/aNK1U/688zYkP5aSRilTA420WsBgD2JyBd7/n1cTFUqtt1Bs9QrUeykPtA9KRPZ9f6PWqs7wl0h5Q8yc8cPvvT9yY21eYVcG644+oBMJDFlNxWHygyCGTgZdqEGG2FE6YHLM+HBO5C7e/+mGaSxXbRi9lh2SYI50pSosM=; logHostIP=null; c_sfbm=234g_00;devicedId=7865969553f94e9c9fe6654e89cbefc0");
        new ChinaUnicomServiceImpl().queryUserInfo4HomePage(telecomForm);
    }

    @Override
    public JSONObject queryUserInfo4HomePage(TelecomForm telecomForm) {
        Map<String, String> params = new HashMap<>(4);
        params.put("currentPhone", telecomForm.getMobile());
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Cookie", telecomForm.getCookie());
        HttpClientResult userInfoResult = null;
        try {
            userInfoResult = httpClientUtil.doFormDataPost(URL_QUERY_USER_INFO_HOME_PAGE, headers, params);
        } catch (Exception e) {
            throw new ServiceException(DONT_TIP + "查询用户信息[首页]异常");
        }
        // 解析响应
        int code = userInfoResult.getCode();
        if (code != 200) {
            throw new ServiceException(String.format(DONT_TIP + "查询用户信息[首页]失败(%s)", code));
        }

        JSONObject userInfoRespJo = null;
        try {
            userInfoRespJo = JSONObject.parseObject(userInfoResult.getContent());
        } catch (Exception e) {
            throw new ServiceException(DONT_TIP + "查询用户信息[首页]失败(非json): " + userInfoResult.getContent());
        }
        if (userInfoRespJo.containsKey("code") && StringUtils.equals("Y", userInfoRespJo.getString("code"))) {
            JSONObject resultJo = new JSONObject();
            resultJo.put("flushDateTime", userInfoRespJo.getString("flush_date_time"));
            JSONArray dataList = userInfoRespJo.getJSONObject("data").getJSONArray("dataList");
            resultJo.put("dataList", dataList);
            this.printLog(telecomForm.mobile, "查询用户信息[首页]");
            return resultJo;
        }
        throw new ServiceException(DONT_TIP + "查询用户信息[首页]失败, resp: " + userInfoRespJo);
    }

    @Override
    public JSONObject queryUserInfo(TelecomForm telecomForm) {
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Cookie", telecomForm.getCookie());
        HttpClientResult userInfoResult = null;
        try {
            userInfoResult = httpClientUtil.doFormDataPost(URL_QUERY_USER_INFO, headers, null);
        } catch (Exception e) {
            throw new ServiceException(DONT_TIP + "查询用户信息异常");
        }
        // 解析响应
        int code = userInfoResult.getCode();
        if (code != 200) {
            throw new ServiceException(String.format(DONT_TIP + "查询用户信息失败(%s)", code));
        }

        JSONObject userInfoRespJo = null;
        try {
            userInfoRespJo = JSONObject.parseObject(userInfoResult.getContent());
        } catch (Exception e) {
            throw new ServiceException(DONT_TIP + "查询用户信息失败(非json): " + userInfoResult.getContent());
        }
        if (userInfoRespJo.containsKey("code") && StringUtils.equals("0000", userInfoRespJo.getString("code"))) {
            userInfoRespJo.remove("code");
            userInfoRespJo.remove("userInfoHref");
            userInfoRespJo.remove("satisfactionSurveyUrl");
            userInfoRespJo.remove("limitRedirect");
            userInfoRespJo.remove("desc");
            this.printLog(telecomForm.mobile, "查询用户信息");
            return userInfoRespJo;
        }
        throw new ServiceException(DONT_TIP + "查询用户信息失败, resp: " + userInfoRespJo);
    }

    private void printLog(String mobile, String content) {
        String ip = ipUtil.getIpAddr(request);
        String location = ipUtil.getLocationByIp(ip);
        log.info("{}[{}({})] {}", mobile, ip, location, content);
    }
}
