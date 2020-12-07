package cn.yiidii.openapi.unicommeituan.service;

import cn.yiidii.openapi.entity.uincommeituan.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomInfoFrom;

import java.util.List;

public interface ChinaUnicomInfoService {

    /**
     *获取所有信息
     * @return
     */
    List<ChinaUnicomInfo> getAllChinaUnicom();

    ChinaUnicomInfo getChinaUnicomById(Integer id);

    /**
     * 通过手机号获取
     * @param phoneNum  手机号
     * @return
     */
    ChinaUnicomInfo getChinaUnicomByPhoneNum(String phoneNum);

    /**
     * 添加一个手机号
     * @param chinaUnicomInfoFrom   中国联通账户信息
     * @return
     */
    int addChinaUnicomInfo(ChinaUnicomInfoFrom chinaUnicomInfoFrom);

    int updateChinaUnicomInfo(ChinaUnicomInfoFrom chinaUnicomInfoFrom);

    int delChinaUnicomInfoById(Integer id);

    /**
     * 通过手机号删除
     * @param phoneNum  手机号
     * @return
     */
    int delChinaUnicomInfoByPhoneNum(String phoneNum);

}
