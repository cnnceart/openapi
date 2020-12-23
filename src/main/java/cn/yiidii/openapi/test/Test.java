package cn.yiidii.openapi.test;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;

import java.util.HashMap;
import java.util.Map;

public class Test {
    private static final String CLIENT_ID = "5231057D21C425826AEBD9219A677A4D";
    private static final String CLIENT_SECRET = "9B2009F510EA4723D947E6CE6D7148D2";
    private static final String URL_GET_CODE = "https://dopen.weimob.com/fuwu/b/oauth2/authorize";
    private static final String URL_GET_TOKEN = "https://dopen.weimob.com/fuwu/b/oauth2/token";
    private static final String URL_REFRESH_TOKEN = "https://dopen.weimob.com/fuwu/b/oauth2/token";
    private static final String URL_GET_GOODS_DETAIL = "https://dopen.weimob.com/api/1_0/ec/goods/queryGoodsDetail";
    private static final String URL_REDIRECT = "https://yiidii.cn";
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36";


    public static void main(String[] args) throws Exception {
        getWeiobCode(CLIENT_ID, CLIENT_SECRET);
    }

    private static String getWeiobCode(String clientId, String clientSecret) throws Exception {
        Map<String, String> params = new HashMap<>(8);
        params.put("enter", "vm");
        params.put("view", "pc");
        params.put("response_type", "code");
        params.put("scope", "default");
        params.put("client_id", clientId);
        params.put("redirect_uri", URL_REDIRECT);
        params.put("state", "");
        Map<String, String> header = new HashMap<>(1);
        header.put("User-Agent", UA);
        HttpClientResult result = new HttpClientUtil().doGet(URL_GET_CODE, null, params);
        System.out.println(result.getContent());
        return null;
    }
}
