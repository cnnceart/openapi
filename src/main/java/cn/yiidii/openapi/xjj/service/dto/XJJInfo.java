package cn.yiidii.openapi.xjj.service.dto;

import lombok.Builder;
import lombok.Data;

/**
 * xjj信息
 */
@Data
@Builder
public class XJJInfo {
    private String id;
    private String city;
    private String nickname;
    private String currRoomNum;
    private String prerequisite;
    private String ptname;
    private String addr;
    private Long txTime;
}
