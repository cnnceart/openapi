package cn.yiidii.openapi.xjj.service.impl;

import cn.yiidii.openapi.common.util.IPUtil;
import cn.yiidii.openapi.entity.xjj.News;
import cn.yiidii.openapi.xjj.mapper.NewsMapper;
import cn.yiidii.openapi.xjj.service.INewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceService implements INewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IPUtil ipUtil;


    @Override
    public int addNews(Integer type, String tplSuffix) {
        String ip = ipUtil.getIpAddr(request);
        String location = ipUtil.getLocationByIp(ip);
        String[] ipArr = ip.split("\\.");
        ipArr[3] = "*";
        ip = StringUtils.join(ipArr,".");
        String content = String.format("%s[%s]" + tplSuffix, location, ip);
        News news = News.builder().type(type).content(content).createTime(new Date()).build();
        return newsMapper.insert(news);
    }

    @Override
    public List<News> getTopNNews(int n) {
        List<News> topNNews = newsMapper.getTopNNews(n);
        return topNNews;
    }


}
