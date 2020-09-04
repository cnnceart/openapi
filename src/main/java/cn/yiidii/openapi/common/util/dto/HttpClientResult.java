package cn.yiidii.openapi.common.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc: 封装httpClient响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpClientResult {

    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应数据
     */
    private String content;

}
