package cn.yiidii.openapi.entity.xjj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * cdk邀请
 */
@Data
@Builder
@TableName("cdk_promote")
public class CdkPromote {

    /**
     * id 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * cdk
     */
    private String cdk;
    /**
     * 推广cdk
     */
    private String promoteCdk;
    /**
     * cdk创建时间
     */
    private Date createTime;

    /**
     * 使用状态
     */
    private Boolean state;

}
