package cn.yiidii.openapi.smsbomb.service.dto;

import cn.yiidii.openapi.common.util.HttpClientUtil;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-28 23:52
 */
@Data
public class SmsBombRunnable implements Runnable {
    private String url;

    @Override
    public void run() {
        HttpClientUtil client = new HttpClientUtil();
        try {
            client.doGet(url);
        } catch (Exception e) {
            try {
                client.doPost(url);
            } catch (Exception exception) {
                return;
            }
        }
    }
}
