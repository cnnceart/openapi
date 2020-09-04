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

    //    final static String COOKIE = "";
    final static String COOKIE = "";
    final static String PHONE_NUM = "";
    final static String SC_KEY = "";

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
