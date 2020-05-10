package com.github.integration;

import com.github.Application;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class MyIntegrationTest {

    @Inject
    Environment environment;

    @Test
    public void testNotLoggedInByDefault() throws IOException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://localhost:" + port + "/auth");
            httpclient.execute(httpget, (ResponseHandler<String>) httpResponse -> {
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                Assertions.assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("用户未登录"));
                return null;
            });
        } finally {
            httpclient.close();
        }
    }

    @Test
    public void testCanRegisterNewUser() throws IOException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost("http://localhost:" + port + "/auth/register");
            post.setEntity(new StringEntity("{\"username\":\"abc\", \"password\":\"123456\"}", ContentType.APPLICATION_JSON));
            httpclient.execute(post, (ResponseHandler<String>) httpResponse -> {
                System.out.println("Register resp = " + httpResponse);
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                Assertions.assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("注册成功"));
                return null;
            });

            post = new HttpPost("http://localhost:" + port + "/auth/login");
            post.setEntity(new StringEntity("{\"username\":\"abc\", \"password\":\"123456\"}", ContentType.APPLICATION_JSON));
            httpclient.execute(post, (ResponseHandler<String>) httpResponse -> {
                System.out.println("Login resp = " + httpResponse);
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                Assertions.assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("登录成功"));
                return null;
            });
        } finally {
            httpclient.close();
        }
    }

    @Test
    public void testCanShowBlogList() throws IOException, URISyntaxException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder("http://localhost:" + port + "/blog")
                        .addParameter("page", "1")
                        .build();
            HttpGet httpget = new HttpGet(uri);
            httpclient.execute(httpget, (ResponseHandler<String>) httpResponse -> {
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                Assertions.assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("获取成功"));
                return null;
            });
        } finally {
            httpclient.close();
        }
    }
}
