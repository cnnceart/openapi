package cn.yiidii.openapi.xjj.controller.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CdkVo {
    /**
     * id 主键
     */
    private String id;
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
    private Date creatTime;
}
