package cn.yiidii.openapi.xjj.service;

import cn.yiidii.openapi.entity.xjj.XjjLog;
import cn.yiidii.openapi.entity.xjj.XjjUsageData;

import java.util.List;

public interface IXjjLogService {

    /**
     * 添加一条xjj日志
     * @param log   xjj日志
     * @return
     */
    int addXjjLog(XjjLog log);

    List<XjjUsageData> getXjjUsage(String cdk);

}
