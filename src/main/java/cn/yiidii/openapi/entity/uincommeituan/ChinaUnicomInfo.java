package cn.yiidii.openapi.entity.uincommeituan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 中国联通信息
 */
@Data
@TableName("china_unicom_info")
public class ChinaUnicomInfo {
    @TableId(type = IdType.AUTO)
    String id;
    String phoneNum;
    String cookie;
    String scKey;
}
