package cn.yiidii.openapi.xjj.service;

import cn.yiidii.openapi.xjj.service.dto.XJJInfo;

import java.util.List;

/**
 * @author yiidii Wang
 * @desc This class is used to ...
 */
public interface IXjjService {

    /**
     * 获取收费放列表
     */
    List<XJJInfo> getVipRoomList(String token);

    /**
     * 获取单个的收费房地址
     * @param token     用户token
     * @param roomId    currroomnum房间号
     * @return
     */
    XJJInfo getSingleVipRoomAddr(String token, String roomId);

}
