package cn.yiidii.openapi.entity.xjj;

import lombok.Data;
import lombok.Builder;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

@Data
@Builder
@TableName("news")
public class News {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer type;
    private String content;
    private Date createTime;
}
