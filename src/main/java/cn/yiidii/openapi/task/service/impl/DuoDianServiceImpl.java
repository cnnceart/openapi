package cn.yiidii.openapi.task.service.impl;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.ServerJNotify;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.task.service.IDuoDianService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 多点 业务实现类
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-27 22:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DuoDianServiceImpl implements IDuoDianService {

    private final HttpClientUtil httpClientUtil;
    private final ServerJNotify serverJNotify;

    @Override
    public void checkIn(String mobile, String cookie) throws Exception {
        long currTime = System.currentTimeMillis();
        String url = "https://appapis.dmall.com/static/signInProccess.jsonp?callback=jQuery22302673548686" + randSixCode() + "_" + currTime + "&isNew=1&phone=" + mobile + "&apiVersion=&platform=%E5%BE%AE%E4%BF%A1&venderId=-1&storeId=-1&addressId=&longitude=&latitude=&nowLongitude=&nowLatitude=&_=" + currTime;
        log.info("{}", url);
        HttpClientResult result = httpClientUtil.doGet(url);
        int code = result.getCode();
        if (200 != code) {
            log.error("多点签到失败({}), url: {}, resp: {}", code, url, result);
            return;
        }
        String content = result.getContent();
        JSONObject resultJo = JSONObject.parseObject(content.substring(content.indexOf("(") + 1, content.lastIndexOf(")")));
        String msg = resultJo.getJSONObject("result").getString("msg");

        JSONObject reward = resultJo.getJSONObject("result").getJSONObject("data").getJSONObject("reward");
        String title = reward.getString("title");
        String subTitle = reward.getString("subTitle");
        JSONArray signInRewardInfoVOs = reward.getJSONArray("signInRewardInfoVOs");


        List<String> rewordStrList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(signInRewardInfoVOs)) {
            for (Object o : signInRewardInfoVOs) {
                JSONObject singleReward = (JSONObject) o;
                String rewardTitle = singleReward.getString("rewardTitle");
                String rewardValidityDate = singleReward.getString("rewardValidityDate");
                String rewardType = singleReward.getString("rewardType");
                String rewardValue = singleReward.getString("rewardValue");
                String rewardUnit = singleReward.getString("rewardUnit");
                String rewardStr = String.format("%s(%s%s,%s)", rewardTitle, rewardValue, rewardUnit, rewardValidityDate);
                rewordStrList.add(rewardStr);
            }
        }

        log.info("{}-{}", mobile, rewordStrList);
        serverJNotify.triggerNotify("SCU86980T340dc43602aad5ca9bdeb5bff70577c35e59cff78a15e", String.format("%s(%s)", title, mobile), StringUtils.join(rewordStrList, ", "));
    }

    @Override
    public void getBasicInfo(String cookie) {

    }

    public String randSixCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public static void main(String[] args) {
        String s = "jQuery22302673548686892466_1609079744080({'result':{\"code\":\"0000\",\"data\":{\"hiddenReward\":{},\"reward\":{\"signInRewardInfoVOs\":[{\"rewardTitle\":\"积分奖励\",\"rewardType\":2,\"rewardUnit\":\"积分\",\"rewardValidityDate\":\"90天内使用\",\"rewardValue\":\"1\"}],\"subTitle\":\"收下以下奖励，生活不怕水逆\",\"title\":\"签到成功\"}},\"msg\":\"执行签到任务成功\"}})";
        String sub = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));
        JSONObject jo = JSONObject.parseObject(sub);
        JSONObject data = jo.getJSONObject("result").getJSONObject("data");
        System.out.println(data);
    }
}
