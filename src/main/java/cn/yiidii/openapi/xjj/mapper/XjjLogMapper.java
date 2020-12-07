package cn.yiidii.openapi.xjj.mapper;

import cn.yiidii.openapi.entity.xjj.XjjLog;
import cn.yiidii.openapi.entity.xjj.XjjUsageData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface XjjLogMapper extends BaseMapper<XjjLog> {

    /**
     * 获取xjj使用数据分析
     *
     * @return
     */
    List<XjjUsageData> getXjjUsage(@Param("cdk") String cdk);

}
