package cn.yiidii.openapi.xjj.service;

import cn.yiidii.openapi.entity.xjj.Cdk;

import java.util.Date;

public interface ICdkService {

    /**
     *
     * @param id    cdk的主键d
     * @return
     */
    Cdk getCdkById(String id);

    /**
     * 根据cdk获取cdk信息
     * @param cdk
     * @return
     */
    Cdk getCdkByCdk(String cdk);

    /**
     * 添加一个cdk
     * @param expireTime   失效时间
     * @return cdk
     */
    String addCdk(Date expireTime);

    /**
     * 更新cdk
     * @param cdk   cdk信息
     * @return
     */
    int updateCdk(Cdk cdk);

    /**
     * 为邀请当前的cdk的cdk—promote添加3天奖励
     * @param cdk   cdk信息（当前）
     * @return
     */
    void add10Day4Promote(Cdk cdk);

    /**
     *生成唯一的cdk字符串
     * @return
     */
    String generateUniqueCdkStr();

    /**
     * 定时清除冗余cdk
     */
    void timerClearRedundantCdk();

}
