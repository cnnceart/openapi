package cn.yiidii.openapi.smsbomb.service;

import cn.yiidii.openapi.base.exception.ServiceException;

/**
 * <p>
 * 短信轰炸服务接口
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-28 22:54
 */
public interface ISmsBombService {

    /**
     * 轰炸
     * @param mobile    手机号
     */
    void bomb(String mobile) throws ServiceException;

}
