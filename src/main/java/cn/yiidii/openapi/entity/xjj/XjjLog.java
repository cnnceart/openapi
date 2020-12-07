package cn.yiidii.openapi.entity.xjj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * xjj请求日志
 */
@Data
@Builder
@TableName("xjj_log")
public class XjjLog {

    /**
     * id主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * cdk
     */
    private String cdk;
    /**
     * 备注
     */
    private String remark;
    /**
     * 浏览器ua
     */
    private String ua;
    /**
     * 位置信息
     */
    private String location;
    /**
     * 请求时间
     */
    private Date requestTime;

}
