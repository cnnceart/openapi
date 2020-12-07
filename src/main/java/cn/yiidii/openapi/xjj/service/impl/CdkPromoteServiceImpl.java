package cn.yiidii.openapi.xjj.service.impl;

import cn.yiidii.openapi.entity.xjj.CdkPromote;
import cn.yiidii.openapi.xjj.common.NewsType;
import cn.yiidii.openapi.xjj.mapper.CdkPromoteMapper;
import cn.yiidii.openapi.xjj.service.ICdkPromoteService;
import cn.yiidii.openapi.xjj.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CdkPromoteServiceImpl implements ICdkPromoteService {

    @Autowired
    private CdkPromoteMapper cdkPromoteMapper;
    @Autowired
    private INewsService newsService;


    @Override
    public int addCdkPromote(String cdkStr, String promoteCdkStr) {
        CdkPromote cdkPromote = CdkPromote.builder().cdk(cdkStr).promoteCdk(promoteCdkStr).createTime(new Date()).build();
        newsService.addNews(NewsType.CDK_PROMOTE.getType(), "通过邀请加入会员");
        return cdkPromoteMapper.insert(cdkPromote);
    }

}
