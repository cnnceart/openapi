package cn.yiidii.openapi.unicommeituan.service;

import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomLoginForm;
import cn.yiidii.openapi.unicommeituan.controller.form.TelecomForm;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 *
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-11-26 14:16
 */
public interface ChinaUnicomService {

    String sendRandomNum(String mobile) throws ServiceException;

    Result randomLogin(ChinaUnicomLoginForm chinaUnicomLoginForm) throws ServiceException;

    JSONObject queryUserInfo4HomePage(TelecomForm telecomForm);

    JSONObject queryUserInfo(TelecomForm telecomForm);

}
