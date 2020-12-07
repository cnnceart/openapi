package cn.yiidii.openapi.xjj.mapper;

import cn.yiidii.openapi.entity.xjj.Cdk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CdkMapper extends BaseMapper<Cdk> {

    /**
     * 查询冗余的cdk
     * @return
     */
    List<Cdk> selectRedundantCdk();

}
