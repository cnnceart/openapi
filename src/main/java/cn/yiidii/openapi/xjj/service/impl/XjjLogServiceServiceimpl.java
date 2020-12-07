package cn.yiidii.openapi.xjj.service.impl;

import cn.yiidii.openapi.entity.xjj.XjjLog;
import cn.yiidii.openapi.entity.xjj.XjjUsageData;
import cn.yiidii.openapi.xjj.common.NewsType;
import cn.yiidii.openapi.xjj.mapper.XjjLogMapper;
import cn.yiidii.openapi.xjj.service.INewsService;
import cn.yiidii.openapi.xjj.service.IXjjLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XjjLogServiceServiceimpl implements IXjjLogService {

    @Autowired
    private XjjLogMapper xjjLogMapper;
    @Autowired
    private INewsService newsService;

    @Override
    public int addXjjLog(XjjLog log) {
        return xjjLogMapper.insert(log);
    }

    @Override
    public List<XjjUsageData> getXjjUsage(String cdk) {
        List<XjjUsageData> xjjUsage = xjjLogMapper.getXjjUsage(cdk);
        newsService.addNews(NewsType.CDK_USE.getType(), "查看了自己的卫生纸使用情况");
        return xjjUsage;
    }
}
