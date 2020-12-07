package cn.yiidii.openapi.entity.xjj;

import lombok.Data;

/**
 * xjj数据分析实体
 */
@Data
public class XjjUsageData {

    /**
     * 小时（0-23）
     */
    private Integer hour;
    /**
     * 每小时的数量
     */
    private Long count;

}
