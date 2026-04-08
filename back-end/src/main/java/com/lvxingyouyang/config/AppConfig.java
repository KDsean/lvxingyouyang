package com.lvxingyouyang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 应用通用配置类
 */
@Configuration
public class AppConfig {

    /**
     * 注册 RestTemplate Bean，用于调用 Python LangChain 服务
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);   // 10 秒
        factory.setReadTimeout(300_000);     // 5 分钟
        return new RestTemplate(factory);
    }
}
