package cn.yiidii.openapi.unicommeituan.meituan;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.ServerJNotify;
import cn.yiidii.openapi.common.util.SpringContextUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.entity.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.dto.MeiTuanGoods;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

@Data
@Slf4j
public class MeituanTaskProxy implements Callable<Object> {

    private HttpClientUtil httpClientUtil = SpringContextUtil.getBean(HttpClientUtil.class);

    private ServerJNotify serverJNotify = SpringContextUtil.getBean(ServerJNotify.class);


    public MeituanTaskProxy(ChinaUnicomInfo chinaUnicomInfo, MeiTuanGoods meiTuanGoods) {
        this.chinaUnicomInfo = chinaUnicomInfo;
        this.meiTuanGoods = meiTuanGoods;
    }

    private ChinaUnicomInfo chinaUnicomInfo;
    private MeiTuanGoods meiTuanGoods;

    @Override
    public Object call() throws Exception {
        String msg = "";//for meituan test
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = submitOrder(chinaUnicomInfo.getCookie(), meiTuanGoods);
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            String code = respJo.getString("code");
            String message = respJo.getString("msg");
            log.info("手机号:{},商品:{} 下单结果:[code: {}, msg: {}]",
                    this.chinaUnicomInfo.getPhoneNum(),
                    this.meiTuanGoods.getGoodsName(),
                    respJo.getString("code"),
                    respJo.getString("msg"));
            msg = String.format("手机号:%s,商品:%s 下单结果:[code: %s, msg: %s]",
                    this.chinaUnicomInfo.getPhoneNum(),
                    this.meiTuanGoods.getGoodsName(),
                    code,
                    message);
            String scKey = chinaUnicomInfo.getScKey();
            if (StringUtils.equals("0", code) && StringUtils.isNotBlank(scKey)) {
                serverJNotify.triggerNotify(scKey, "联通星期五（http://yiidii.cn）", msg);
            }
        } catch (Exception e) {
            log.info("手机号{}下单异常: {}", chinaUnicomInfo.getPhoneNum(), httpClientResult);
            msg = String.format("手机号%s下单异常: %s", chinaUnicomInfo.getPhoneNum(), httpClientResult);
        }
        return msg;
    }

    public HttpClientResult submitOrder(String cookie, MeiTuanGoods meiTuanGoods) throws Exception {
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
        return httpClientUtil.doWWWFormUrlencodePost(url, headers, params);
    }
}
