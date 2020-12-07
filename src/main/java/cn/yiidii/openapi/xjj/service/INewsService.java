package cn.yiidii.openapi.xjj.service;

import cn.yiidii.openapi.entity.xjj.News;

import java.util.List;

public interface INewsService {

    int addNews(Integer type, String tplSuffix);

    List<News> getTopNNews(int n);

}
