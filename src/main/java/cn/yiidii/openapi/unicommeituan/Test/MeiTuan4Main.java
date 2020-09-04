package cn.yiidii.openapi.unicommeituan.Test;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.ServerJNotify;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.entity.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.dto.MeiTuanGoods;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class MeiTuan4Main {

    //    final static String COOKIE = "wmf=bffc0ae5b469d70917d7edf806058ccd;MUT_S=android5.1.1;ecs_acc=Ql22w4IGNHG9rygGbjkMZqiW5Jr5vIOGib4z/nBj/jHJFCQwGU4k3j4joQbE+yyTX/+EI3sjUNqctf40W+hZ/GG/DqKe/v2kFN3OqVoLCzRc4y9s6sbYljoACfQ2KR40e/NmdgUAsjj8/YzHWMTTCp2/CCphwvJcpI8pX8hvS1w=;mobileServiceAll=5460eac03933c2be82b9875e246307d7;mobileService1=fLxNJUuj1cYh4UYzNcDGqiHjia7JLu8i0g7OBHQBJJCbT_ul3O-3!2084491184;logHostIP=null;c_sfbm=234g_00;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTk2MjY5MjEsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NjAwMDE1ODAwIiwicmFuZG9tU3RyIjoieWhmMjYzOGIxNTk5MDIyMTIxIn0sImlhdCI6MTU5OTAyMjEyMX0.oLaIViKdtqhzBT1LGFiAebsz3mVPVcoCxawouIfl8VQo6fOBkwTg7us3HEqGLcHmswsIzv2QapACqp9ucGIhtA;c_id=7634d33a3ca6ce48a2f1128abb3a63974c8774e0d3258076517653c4c32eb667;u_type=11;login_type=06;login_type=06;u_account=15600015800;city=011|110|90356344|-99;c_version=android@7.0402;d_deviceCode=860551848155470;enc_acc=Ql22w4IGNHG9rygGbjkMZqiW5Jr5vIOGib4z/nBj/jHJFCQwGU4k3j4joQbE+yyTX/+EI3sjUNqctf40W+hZ/GG/DqKe/v2kFN3OqVoLCzRc4y9s6sbYljoACfQ2KR40e/NmdgUAsjj8/YzHWMTTCp2/CCphwvJcpI8pX8hvS1w=;ecs_acc=Ql22w4IGNHG9rygGbjkMZqiW5Jr5vIOGib4z/nBj/jHJFCQwGU4k3j4joQbE+yyTX/+EI3sjUNqctf40W+hZ/GG/DqKe/v2kFN3OqVoLCzRc4y9s6sbYljoACfQ2KR40e/NmdgUAsjj8/YzHWMTTCp2/CCphwvJcpI8pX8hvS1w=;random_login=1;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e1bb0491f63e88e426d32258405bfad165df1379e2b4dbe8a3157524e940dbdd64ad2a976c9b2d40493c6dd3a354037d;t3_token=6c9333e0edf0ff0130408fe717782688;invalid_at=6be73b1523617260f13221eb6efff357370d4ebdca91fee6dc8eb9ddb15919d5;c_mobile=15600015800;wo_family=0;u_areaCode=110;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDIzZDhhNWIxYmMyNjY0YjZiYzczZTFiOWVlNDc4NjM3NjY3ZGI5NGIyZDVhYTI4M2I3YmQ3OWNkOTM3MGU3NWI2ODU4N2Y5MTBhNGQ4YzhiNWQwOTRkNTI1ZGRkNmIwNWQyZDUxYjZkZGIzMTFhOWRlYTA1OGU4ZjA0ZGRlOWQ0NyIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NGU4YTVmYWZlYTQ2YTJmYmFiODNmNjZjMWQwMjkyYTI5YmJkYWQxNjJmNmVkNjU3OGMwYjBhOTMzZTVhMDgxN2IzZDZjOTA1ZjIwMzlhYmVjM2ZiMzA1N2UwNDFmODk0YWE0NzQ5MzczZjg2N2E5ZjBhMDNlNzc1YWYyODIyNTk3IiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTYwMDAxNTgwMCIsInBybyI6IjAxMSIsImNpdHkiOiIxMTAiLCJpZCI6ImI0OTM4NTYyZTdmMDRhZTFjYzM3Y2Y2ZDlkMzIwYWJiIn0.1nHehkC4qcw13s4gvsKrDsg7UM4j-QSS0Tixca2wFNw;on_info=b7ebdc40ed1791ad2f844a90b2dc06c5;mobileServiceAll=b073abc2467febfa3285c19a1cf4aa9f;mobileService1=jsTCfPkL2JXkPSVJQQVZXBnqFbBJn0htT7Dv2QR4N2DCYHnf1JLp!1145878233;importantroute=1599022123.94.224259.694505;JSESSIONID=FECCB1E00E7A61C8BCB1477EDD9E3AAE;ecs_acc=Ql22w4IGNHG9rygGbjkMZqiW5Jr5vIOGib4z/nBj/jHJFCQwGU4k3j4joQbE+yyTX/+EI3sjUNqctf40W+hZ/GG/DqKe/v2kFN3OqVoLCzRc4y9s6sbYljoACfQ2KR40e/NmdgUAsjj8/YzHWMTTCp2/CCphwvJcpI8pX8hvS1w=;logHostIP=null;c_sfbm=234g_00";
    final static String COOKIE = "wmf=0849d20ac328a01ca093f3a352c898ab;MUT_S=android8.1.0;devicedId=865300046557938;mobileServiceAll=aa182da76540347f10f8b2ccf50fe533;mobileService1=palWmQnHPIQtQpyhzKDJQyPQC_FALXtd_lRAr2_bszFuSv_iR4vP!671924717;logHostIP=null;c_sfbm=234g_00;ecs_acc=CC2brclw6XOF1lt6xGqfvS/5KZ1hylWWiZAOVQahygo6Etfm8XkV0KSZjAMx3pMVqI6P0oY+xpHe3jooWV9Aj/Vmm+NQ21n8wuNUw4+HkGSVKiWnJC4nh5Ysm3lvmmE0l5LwzOPtFodx7x16YEL0TannyNqbjdAcbI0Yhd5qMEQ=;req_mobile=15510804272;req_serial=;clientid=98|0;req_wheel=ssss;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTk3ODU1MDMsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NTEwODA0MjcyIiwicmFuZG9tU3RyIjoieWhlMWJhODUxNTk5MTgwNzAzIn0sImlhdCI6MTU5OTE4MDcwM30.qfYDd0CFcx0wC3GYObqwarwrf0K73cPT7sk9mptOO3Q6x8A1J2j9BqSLZjKok0bheUwXkkGU5z5yQ2LrqXY-Xw;c_id=b1d9a947ec07d4e3ff1b2c4e8b854f23249b384b423247ce9ca375c04b25fe39;u_type=11;login_type=06;login_type=06;u_account=15510804272;city=013|130|90311178|-99;c_version=android@7.0403;d_deviceCode=865300046557938;enc_acc=CC2brclw6XOF1lt6xGqfvS/5KZ1hylWWiZAOVQahygo6Etfm8XkV0KSZjAMx3pMVqI6P0oY+xpHe3jooWV9Aj/Vmm+NQ21n8wuNUw4+HkGSVKiWnJC4nh5Ysm3lvmmE0l5LwzOPtFodx7x16YEL0TannyNqbjdAcbI0Yhd5qMEQ=;ecs_acc=CC2brclw6XOF1lt6xGqfvS/5KZ1hylWWiZAOVQahygo6Etfm8XkV0KSZjAMx3pMVqI6P0oY+xpHe3jooWV9Aj/Vmm+NQ21n8wuNUw4+HkGSVKiWnJC4nh5Ysm3lvmmE0l5LwzOPtFodx7x16YEL0TannyNqbjdAcbI0Yhd5qMEQ=;random_login=1;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c32178e15fd62b2e29643a2a243f73b9acf7046b7a0774e844a8288346ec847bb300d8d8f212a72f5f281009289f7d7845da;t3_token=c46cf259777faef900171430fb76a8cb;invalid_at=89ccb98ccd036e8f864f59ae98e721889476351d39a9762f28d1afe2536299a1;c_mobile=15510804272;wo_family=0;u_areaCode=130;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDJhYzEwZTg3NWRlZGYxMjM2M2M0OGI0YjRiNWMzMGYwMjcwNjJiZDkxYzYwZGE4OGZhYWRlZmY3MWJiZTZkN2ZkYjJjMjU2NzE0MzlmMGQ0ZDg2ZTc4M2Q4MGRmYTliMzAyYmY5ZDBkMGJiYzIyMjUyZmEyNDdmN2Y5ODMxNjJiZSIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDYwMWYyMWE1ZDM2Mzc5MDE5MGVkYTIwMjMxYzZlMGI0MTYwNDQ2OWY3ZTFlOWE1MjdhYWUwYWEzNmVkMzI2ZjFjMzEwYWM2NDZhY2M1NjFmODMwYTdhZTAzMjk4MTM3YjY2YzdjNmI5ZGM5MTY0NTc2NWI3NTczZjEyZjAzOGIzIiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTUxMDgwNDI3MiIsInBybyI6IjAxMyIsImNpdHkiOiIxMzAiLCJpZCI6ImQ0ODc3MTU0N2MxMzJmZTlhMDI0MmYzY2ZmZGQ5OGNhIn0.-EZsjcn9R_3EIbkZ24Z-gpSTZezj3u153UfRQaCQf1U;on_info=b7ebdc40ed1791ad2f844a90b2dc06c5;mobileServiceAll=e1cacb1bd0ee86e6bf14cfb94ca47c66;mobileService1=XIpWmQg7-vH_hZm9vpmQtS8RZ-YcIvOZKfWm71CU5KTh4Azsewt7!-403752350;ecs_acc=CC2brclw6XOF1lt6xGqfvS/5KZ1hylWWiZAOVQahygo6Etfm8XkV0KSZjAMx3pMVqI6P0oY+xpHe3jooWV9Aj/Vmm+NQ21n8wuNUw4+HkGSVKiWnJC4nh5Ysm3lvmmE0l5LwzOPtFodx7x16YEL0TannyNqbjdAcbI0Yhd5qMEQ=;logHostIP=null;c_sfbm=234g_00;importantroute=1599180711.066.225288.562209;JSESSIONID=6541D482C8149916BA88194692C23D2F;route=bbe6f34980ab2bc5137a29292b7e2ef2;welfareroute=f5e683fd9e6037b38e273720c69816c31add635a";
    final static String PHONE_NUM = "15510804272";
    final static String SC_KEY = "SCU86980T340dc43602aad5ca9bdeb5bff70577c35e59cff78a15e";

    static {
        // 关闭log
        Set<String> loggers = new HashSet<>(Arrays.asList("org.apache.http"));
        for (String log : loggers) {
            Logger logger = (Logger) LoggerFactory.getLogger(log);
            logger.setLevel(Level.INFO);
            logger.setAdditive(false);
        }
    }

    public static void main(String[] args) throws Exception {
        robTicket();
    }

    private static void robTicket() throws Exception {
        ChinaUnicomInfo info = new ChinaUnicomInfo();
        info.setPhoneNum(PHONE_NUM);
        info.setCookie(COOKIE);
        info.setScKey(SC_KEY);
        List<MeiTuanGoods> goodsList = getGoodsList(info);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        while (true) {
            goodsList.forEach(singleGoods -> {
                MeituanThread thread = new MeituanThread();
                thread.setChinaUnicomInfo(info);
                thread.setMeiTuanGoods(singleGoods);
                service.schedule(thread, 200, TimeUnit.MILLISECONDS);
            });
            TimeUnit.MICROSECONDS.sleep(1);
        }

    }

    /**
     * 獲取商品信息
     */
    private static List<MeiTuanGoods> getGoodsList(ChinaUnicomInfo chinaUnicomInfo) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/get619Activity/v1?whetherFriday=YES&from=955000006";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            JSONArray goodsListJa = resdata.getJSONArray("goodsList");
            List<MeiTuanGoods> goodsList = new LinkedList<>();
            goodsListJa.forEach(obj -> {
                JSONObject jo = (JSONObject) obj;
                MeiTuanGoods info = new MeiTuanGoods();
                info.setGoodsName(jo.getString("gOODS_NAME"));
                info.setLinkUrl(jo.getString("lINKURL"));
                String goodsSkuId = jo.getString("gOODS_SKU_ID");
                info.setGoodsSkuId(goodsSkuId);
                info.setMarketPrice(jo.getDouble("mARKET_PRICE"));
                info.setBeginTime(jo.getLong("beginTime"));
                info.setEndTime(jo.getLong("endTime"));
                info.setState(jo.getInteger("state"));
                try {
                    info.setCurrSalePrice(getCurrSalePrice(chinaUnicomInfo, goodsSkuId));
                } catch (Exception e) {
                }
                goodsList.add(info);
            });
            return goodsList;
        } else {
            System.out.println(String.format("%s获取商品失败，响应:%s", chinaUnicomInfo.getPhoneNum(), JSONObject.toJSONString(httpClientResult)));
            return null;
        }
    }

    private static double getCurrSalePrice(ChinaUnicomInfo chinaUnicomInfo, String goodsSkuId) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/getGoodsTradePrice/v2";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            double currSalePrice = resdata.getDouble(goodsSkuId);
            return currSalePrice;
        } else {
            System.out.println(String.format("%s获取商品失败，响应:%s", chinaUnicomInfo.getPhoneNum(), JSONObject.toJSONString(httpClientResult)));
            return 0D;
        }
    }

    /**
     * 下单
     */
    private static HttpClientResult submitOrder(String cookie, MeiTuanGoods meiTuanGoods) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front/mobile/api/bj2402/v1";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", cookie);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");

        Map<String, String> params = new TreeMap<>();
        Map<String, Object> reqDataMap = new TreeMap<>();
        reqDataMap.put("goodsId", meiTuanGoods.getGoodsSkuId());
        reqDataMap.put("payWay", "01");
        reqDataMap.put("amount", meiTuanGoods.getCurrSalePrice());
        reqDataMap.put("saleTypes", "C");
        reqDataMap.put("points", 0);
        reqDataMap.put("beginTime", meiTuanGoods.getBeginTime());
        reqDataMap.put("imei", "d2575c3322c14c4cbda477bda4cc519e");
        reqDataMap.put("sourceChannel", "955000300");
        reqDataMap.put("proFlag", "");
        reqDataMap.put("scene", "");
        reqDataMap.put("promoterCode", "");
        reqDataMap.put("maxcash", "");
        params.put("reqdata", JSONObject.toJSONString(reqDataMap));
        return new HttpClientUtil().doWWWFormUrlencodePost(url, headers, params);
    }

    @Data
    static class MeituanThread implements Callable<Object> {

        private ChinaUnicomInfo chinaUnicomInfo;
        private MeiTuanGoods meiTuanGoods;
        private ServerJNotify serverJNotify = new ServerJNotify();

        @Override
        public Object call() throws Exception {
            String msg = "";
            try {
                HttpClientResult httpClientResult = submitOrder(chinaUnicomInfo.getCookie(), meiTuanGoods);
                JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
                String code = respJo.getString("code");
                String message = respJo.getString("msg");
                msg = String.format("手机号:%s,商品:%s 下单结果:[code: %s, msg: %s]",
                        this.chinaUnicomInfo.getPhoneNum(),
                        this.meiTuanGoods.getGoodsName(),
                        code,
                        message);
                if (StringUtils.equals("0", code)) {
                    System.out.println(msg);
                } else if (StringUtils.equals("1", code)) {
                    System.err.println(msg);
                }

            } catch (Exception e) {

            }
            return msg;
        }
    }
}
