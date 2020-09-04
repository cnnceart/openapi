package cn.yiidii.openapi.xjj.service.impl;

import cn.yiidii.openapi.base.Constant;
import cn.yiidii.openapi.common.util.AESUtil;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.entity.XJJInfo;
import cn.yiidii.openapi.xjj.service.XJJService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XJJ业务类
 */
@Service
public class XJJServiceImpl implements XJJService {
    
    @Autowired
    private HttpClientUtil httpClientUtil;

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
    public XJJInfo getSingleVipRoom(String token, String roomId) {
        XJJInfo xjj = null;
        try {
            String addr = getRoomAddr(token, roomId);
            xjj = XJJInfo.builder().addr(addr).build();
        } catch (Exception e) {

        }
        return xjj;
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
        addr = addr.replaceAll("\\u001a\\u000f",":/");
        addr = addr.replaceAll("\\u000e",".");
        return addr;
    }

    private List<XJJInfo> parseXjjList(String token, JSONArray listJa) {
        List<XJJInfo> list = new ArrayList<>();
        for (int i = 0; i < listJa.size(); i++) {
            JSONObject singleRoom = listJa.getJSONObject(i);
            String city = singleRoom.getString("city");
            String nickname = singleRoom.getString("nickname");
            String curroomnum = singleRoom.getString("curroomnum");
            String prerequisite = singleRoom.getJSONObject("limit").getString("prerequisite");
            String ptname = singleRoom.getJSONObject("limit").getString("ptname");
            String addr = this.getRoomAddr(token, curroomnum);
            XJJInfo xjjInfo = XJJInfo.builder()
                    .city(city)
                    .nickname(nickname)
                    .currRoomNum(curroomnum)
                    .prerequisite(prerequisite)
                    .ptname(ptname)
                    .addr(addr).build();
            list.add(xjjInfo);
        }
        return list;
    }


}
