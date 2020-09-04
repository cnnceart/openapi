package cn.yiidii.openapi.xjj.service;

import cn.yiidii.openapi.entity.XJJInfo;

import java.util.List;

/**
 * @author yiidii Wang
 * @desc This class is used to ...
 */
public interface XJJService {

    /**
     * 获取收费放列表
     */
    @Deprecated
    List<XJJInfo> getVipRoomList(String token);

    /**
     * 过去单个的收费房
     * @param token     用户token
     * @param roomId    currroomnum房间号
     * @return
     */
    XJJInfo getSingleVipRoom(String token, String roomId);

}
