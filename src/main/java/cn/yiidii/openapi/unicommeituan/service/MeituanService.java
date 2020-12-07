package cn.yiidii.openapi.unicommeituan.service;

import cn.yiidii.openapi.entity.uincommeituan.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomInfoFrom;
import cn.yiidii.openapi.unicommeituan.dto.MeiTuanGoods;

import java.util.List;

public interface MeituanService {

    /**
     * 执行抢券
     */
    void robTicket();

    /**
     * 测试美团是否可行
     * @param chinaUnicomInfoFrom
     * @return
     */
    String meituanTest(ChinaUnicomInfoFrom chinaUnicomInfoFrom);

    /**
     * 获取中国联通美团商品列表
     * @param chinaUnicomInfo   中国联通商账户信息
     * @return
     * @throws Exception
     */
    List<MeiTuanGoods> getGoodsList(ChinaUnicomInfo chinaUnicomInfo) throws Exception;

    double getCurrSalePrice(ChinaUnicomInfo chinaUnicomInfo, String goodsSkuId) throws Exception;

}
