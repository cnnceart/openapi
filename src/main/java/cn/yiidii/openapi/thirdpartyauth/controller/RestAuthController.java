package cn.yiidii.openapi.thirdpartyauth.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/oauth")
@Slf4j
public class RestAuthController {
    @Value("${justauth.type.github.client-id}")
    private String clientId;
    @Value("${justauth.type.github.client-secret}")
    private String clientSecret;
    @Value("${justauth.type.github.redirect-uri}")
    private String redirectUri;

    @RequestMapping("/github/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        AuthResponse authResp = null;
        try {
            authResp = authRequest.login(callback);
        } catch (Exception e) {
            log.info("{}", e);
            log.info("{}", JSONObject.toJSON(authResp));
            return authResp.getMsg();
        }
        log.info("Auth Response: {}", JSONObject.toJSONString(authResp));
        return authResp.getData();
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .build());
    }
}
