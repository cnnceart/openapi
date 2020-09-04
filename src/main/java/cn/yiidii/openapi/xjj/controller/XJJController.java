package cn.yiidii.openapi.xjj.controller;

import cn.yiidii.openapi.entity.XJJInfo;
import cn.yiidii.openapi.xjj.service.XJJService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author yiidii Wang
 * @desc This class is used to ...
 */
@RestController
@Api(tags = "小奶猫解析")
public class XJJController {
    @Autowired
    XJJService xjjService;

    @GetMapping("singleVipRoom")
    @ApiOperation(value = "获取单个收费房")
    public Object getSingleVIPRoom(@RequestParam String token,
                                   @RequestParam String roomId) {
        XJJInfo info = xjjService.getSingleVipRoom(token,roomId);
        JSONObject jo = new JSONObject();
        jo.put("token",token);
        jo.put("roomId",roomId);
        jo.put("addr",info.getAddr());
        return jo;
    }

    @GetMapping("vipRooms")
    @ApiIgnore
    @Deprecated
    public Object getSingleVIPRoom(@RequestParam String token){
        List<XJJInfo> infoList = xjjService.getVipRoomList(token);
        return infoList;
    }

}
