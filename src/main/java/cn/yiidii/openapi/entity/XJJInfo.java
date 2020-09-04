package cn.yiidii.openapi.entity;

import lombok.Builder;
import lombok.Data;

/**
 * xjj信息
 */
@Data
@Builder
public class XJJInfo {
    String city;
    String nickname;
    String currRoomNum;
    String prerequisite;
    String ptname;
    String addr;
}
