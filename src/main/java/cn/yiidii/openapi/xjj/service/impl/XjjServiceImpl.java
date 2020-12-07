package cn.yiidii.openapi.xjj.service.impl;

import cn.yiidii.openapi.base.Constant;
import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.common.util.AESUtil;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.xjj.service.ICdkService;
import cn.yiidii.openapi.xjj.service.dto.XJJInfo;
import cn.yiidii.openapi.xjj.service.IXjjService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.*;

/**
 * XJJ业务类
 */
@Service
@Slf4j
public class XjjServiceImpl implements IXjjService {

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private ICdkService cdkService;

    private static Map<String, String> headers = new HashMap<>();

    @PostConstruct
    private void initHeaders() {
        headers.put("X-Live-Butter2", "YxE4NA+07NjzfqbSEcFMk1aJSxZLIvXsbE1GYPToEoMQLPeKZJlieKuREBpk97evEOAx87kXboRi/tkVxWFmSsdgkEnVMXeUtQXjmhSM9WrF+FP0xAPpYSUHADzy6xwGfZoHBnjHso7JuGoBSGe3e/nHrM6zSA8VwI7ut8COy0kiQ5bk+MfVYNWGxzV6S4Bg2ASd1EOLt9rrf9tJ3K5VNYesYy0VGhQdhJudMzTf+40=");
        headers.put("X-Live-Pretty", "spring");
        headers.put("knockknock", "synergy");
    }


    @Override
    public List<XJJInfo> getVipRoomList(String token) {
        HttpClientResult result = null;
        try {
            result = httpClientUtil.doGet(Constant.URL_VIP_ROOM_LIST + token, headers, null);
        } catch (Exception e) {
            log.info("{}", e);
            return null;
        }
        if (200 != result.getCode()) {
            return null;
        }
        String content = result.getContent();
        JSONObject contentJo = JSONObject.parseObject(content);
        List<XJJInfo> infoList = parseXjjList(token, contentJo.getJSONObject("data").getJSONArray("list"));
        return infoList;
    }

    @Override
    public XJJInfo getSingleVipRoomAddr(String token, String roomId) {
        try {
            String roomAddr = getRoomAddr(token, roomId);
            Long txTime = getTxTimeStampFromAddr(roomAddr);
            return XJJInfo.builder().addr(roomAddr).txTime(txTime).build();
        } catch (Exception e) {
            throw new ServiceException("获取房间号发生异常");
        }
    }

    public String getRoomAddr(String token, String roomId) {
        String addr = "";
        try {
            String addrUrl = Constant.URL_VIP_ROOM_ADDR + roomId;
            HttpClientResult httpClientResult = httpClientUtil.doGet(addrUrl, headers, null);
            String result = httpClientResult.getContent();
            JSONObject resultJo = JSONObject.parseObject(result);
            String enAddr = resultJo.getJSONObject("data").getString("data");
            addr = decodeAddr(enAddr);
        } catch (Exception e) {

        }
        return addr;
    }

    private static String decodeAddr(String enAddr) {
        byte[] org = Base64.decodeBase64(enAddr.getBytes());
        byte[] eKey = AESUtil.parseHexStr2Byte("00000000000000000000000000000000");
        byte[] iv = "pk];pk,o876nkwdd".getBytes();
        String addr = new String(new AESUtil().decrypt(org, eKey, iv));
        addr = addr.replaceAll("\\u001a\\u000f", ":/");
        addr = addr.replaceAll("\\u000e", ".");
        return addr;
    }

    private List<XJJInfo> parseXjjList(String token, JSONArray listJa) {
        List<XJJInfo> list = new ArrayList<>();
        for (int i = 0; i < listJa.size(); i++) {
            JSONObject singleRoom = listJa.getJSONObject(i);
            String id = singleRoom.getString("id");
            String city = singleRoom.getString("city");
            String nickname = singleRoom.getString("nickname");
            String curroomnum = singleRoom.getString("curroomnum");
            String prerequisite = singleRoom.getJSONObject("limit").getString("prerequisite");
            prerequisite = Objects.isNull(prerequisite) ? "密码房" : prerequisite + "金币";
            String ptname = singleRoom.getJSONObject("limit").getString("ptname");
            String addr = this.getRoomAddr(token, curroomnum);
            Long txtime = getTxTimeStampFromAddr(addr);
            XJJInfo xjjInfo = XJJInfo.builder()
                    .id(id)
                    .city(city)
                    .nickname(nickname)
                    .currRoomNum(curroomnum)
                    .prerequisite(prerequisite)
                    .ptname(ptname)
                    .addr(addr)
                    .txTime(txtime).build();
            list.add(xjjInfo);
        }
        return list;
    }

    private Long getTxTimeStampFromAddr(String addr) {
        if (StringUtils.isBlank(addr)) {
            return null;
        }
        try {
            addr = addr.substring(addr.indexOf("?") + 1);
            String[] params = addr.split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                if(StringUtils.equals(kv[0],"txTime")){
                    BigInteger timeHex = new BigInteger(kv[1], 16);
                    return timeHex.longValue();
                }else if(StringUtils.equals(kv[0],"timestamp")){
                    return Long.valueOf(kv[1]);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
