package cn.yiidii.openapi.entity.xjj;

import lombok.Data;
import lombok.Builder;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

/**
 * cdk
 */
@Data
@Builder
@TableName("cdk")
public class Cdk {

    /**
     * id 主键
     */
    @TableId(type = IdType.ASSIGN_UUID )
    private String id;
    /**
     * cdk
     */
    private String cdk;
    /**
     * 备注
     */
    private String remark;
    /**
     * cdk失效时间
     */
    private Date expireTime;
    /**
     * cdk创建时间
     */
    private Date createTime;
}
