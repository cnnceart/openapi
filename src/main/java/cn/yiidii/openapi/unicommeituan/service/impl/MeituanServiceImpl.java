package cn.yiidii.openapi.unicommeituan.service.impl;

import cn.yiidii.openapi.base.Constant;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.entity.uincommeituan.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomInfoFrom;
import cn.yiidii.openapi.unicommeituan.dto.MeiTuanGoods;
import cn.yiidii.openapi.unicommeituan.meituan.MeituanTaskProxy;
import cn.yiidii.openapi.unicommeituan.service.ChinaUnicomInfoService;
import cn.yiidii.openapi.unicommeituan.service.MeituanService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeituanServiceImpl implements MeituanService {

    private final ChinaUnicomInfoService chinaUnicomInfoService;

    /**
     * 美团下单线程池
     */
    @Resource(name = "meituanTaskExecutor")
    private ThreadPoolTaskExecutor meituanTaskExecutor;

    /**
     * 异步执行抢券
     */
//    @Async("scheduleTaskExecutor")
//    @Scheduled(fixedDelay = 300)
//    @Scheduled(fixedDelay = 100)
    public void robTicket() {
        List<ChinaUnicomInfo> allInfo = chinaUnicomInfoService.getAllChinaUnicom();
        if (Objects.isNull(allInfo) || allInfo.size() <= 0) {
            return;
        }
        allInfo.forEach(chinaUnicomInfo -> {
            List<MeiTuanGoods> goodsList = null;
            try {
                goodsList = getGoodsList(chinaUnicomInfo);
            } catch (Exception e) {
                return;
            }
            if (Objects.isNull(goodsList) || goodsList.size() == 0) {
                return;
            }
            goodsList.forEach(singleGoods -> {
                try {
                    MeituanTaskProxy proxy = new MeituanTaskProxy(chinaUnicomInfo, singleGoods);
                    meituanTaskExecutor.submit(proxy);
                } catch (Exception e) {
                }
            });

        });
    }

    @Override
    public String meituanTest(ChinaUnicomInfoFrom chinaUnicomInfoFrom) {
        ChinaUnicomInfo chinaUnicomInfo = new ChinaUnicomInfo();
        BeanUtils.copyProperties(chinaUnicomInfoFrom, chinaUnicomInfo);
        List<MeiTuanGoods> goodsList = null;
        try {
            goodsList = this.getGoodsList(chinaUnicomInfo);
        } catch (Exception e) {
            return "获取商品失败, 可能是Cookie失效了!";
        }
        if (Objects.isNull(goodsList) || goodsList.size() <= 0) {
            return "获取商品失败, 可能是Cookie失效了!";
        }
        MeiTuanGoods meiTuanGoods = goodsList.get(0);
        try {
            MeituanTaskProxy proxy = new MeituanTaskProxy(chinaUnicomInfo, meiTuanGoods);
            Future<Object> future = meituanTaskExecutor.submit(proxy);
            Object o = future.get(1500, TimeUnit.MILLISECONDS);
            return o.toString();
        } catch (Exception e) {
            log.info("提交订单发生异常: {}", e.toString());
            return String.format("提交订单发生异常: %s", e.toString());
        }
    }

    @Override
    public List<MeiTuanGoods> getGoodsList(ChinaUnicomInfo chinaUnicomInfo) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/get619Activity/v1?whetherFriday=YES&from=955000006";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", Constant.USER_AGENT);
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            JSONArray goodsListJa = resdata.getJSONArray("goodsList");
            List<MeiTuanGoods> goodsList = new LinkedList<>();
            goodsListJa.forEach(obj -> {
                JSONObject jo = (JSONObject) obj;
                MeiTuanGoods info = new MeiTuanGoods();
                String goodsName = jo.getString("gOODS_NAME");
                if (!StringUtils.contains(goodsName, "10")) {
                    return;
                }
                info.setGoodsName(goodsName);
                info.setLinkUrl(jo.getString("lINKURL"));
                String goodsSkuId = jo.getString("gOODS_SKU_ID");
                info.setGoodsSkuId(goodsSkuId);
                info.setMarketPrice(jo.getDouble("mARKET_PRICE"));
                info.setBeginTime(jo.getLong("beginTime"));
                info.setEndTime(jo.getLong("endTime"));
                info.setState(jo.getInteger("state"));
                try {
                    double currSalePrice = getCurrSalePrice(chinaUnicomInfo, goodsSkuId);
                    if (currSalePrice <= 0) {
                        return;
                    }
                    info.setCurrSalePrice(currSalePrice);
                } catch (Exception e) {
                    return;
                }
                goodsList.add(info);
            });
            return goodsList;
        } else {
            return null;
        }
    }

    @Override
    public double getCurrSalePrice(ChinaUnicomInfo chinaUnicomInfo, String goodsSkuId) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/getGoodsTradePrice/v2";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", Constant.USER_AGENT);
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            double currSalePrice = resdata.getDouble(goodsSkuId);
            return currSalePrice;
        } else {
            return 0D;
        }
    }
}
