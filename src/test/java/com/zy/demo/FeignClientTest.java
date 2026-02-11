package com.zy.demo;

import com.zy.demo.feign.AuthServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

@SpringBootTest
public class FeignClientTest {

    private static MockWebServer mockWebServer;

    @Autowired
    private AuthServer authServer;

    @BeforeAll
    public static void before() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        System.setProperty("feign.url.auth", "http://localhost:" + mockWebServer.getPort());
    }

    @AfterAll
    public static void after() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    public static void register(DynamicPropertyRegistry registry) {
        registry.add("feign.url.auth", () -> "http://localhost:" + mockWebServer.getPort());
    }

    @Test
    public void test() {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .setBody("{\n" +
                        "    \"timestamp\": 1770814138100,\n" +
                        "    \"status\": 500,\n" +
                        "    \"error\": \"Internal Server Error\",\n" +
                        "    \"message\": \"\",\n" +
                        "    \"path\": \"/zy/test/get\"\n" +
                        "}")
        );
        boolean result = this.authServer.checkUserAuth(11L, 2);
        System.out.println(result);
    }
}
