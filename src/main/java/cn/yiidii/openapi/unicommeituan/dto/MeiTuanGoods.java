package cn.yiidii.openapi.unicommeituan.dto;

import lombok.Data;

/**
 * 美团商品dto
 */
@Data
public class MeiTuanGoods {
    private String goodsName;
    private String linkUrl;
    private String goodsSkuId;
    /**
     * 市场价
     */
    private double marketPrice;
    private Long beginTime;
    private Long endTime;
    private Integer state;
    /**
     * 当前价格
     */
    private double currSalePrice;
}
