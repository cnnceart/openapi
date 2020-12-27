package cn.yiidii.openapi.task.service;

/**
 * <p>
 * 多点签到
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-27 22:46
 */
public interface IDuoDianService {

    /**
     * 签到动作
     *
     * @param mobile 手机号
     * @param cookie cookie
     */
    void checkIn(String mobile, String cookie) throws Exception;

    /**
     * 用户基本信息
     *
     * @param cookie cookie
     */
    void getBasicInfo(String cookie) throws Exception;

}
