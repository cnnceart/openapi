package cn.yiidii.openapi.xjj.mapper;

import cn.yiidii.openapi.entity.xjj.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    /**
     * 获取news TopN
     * @param n
     * @return
     */
    List<News> getTopNNews(Integer n);
}
