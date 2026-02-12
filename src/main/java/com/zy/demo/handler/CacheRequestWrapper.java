package com.zy.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 缓存HttpServletRequest
 *
 * @author zy
 */
public class CacheRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存body字节流
     */
    private final byte[] body;

    public CacheRequestWrapper(HttpServletRequest request, ObjectMapper objectMapper) throws IOException {
        super(request);
        //读取原始请求体
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        byte[] bytes;
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            //格式化JSON，去掉空白
            Object json = objectMapper.readValue(sb.toString(), Object.class);
            bytes = objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8);
        } else {
            bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        }
        //初始化body
        this.body = bytes;
    }

    /**
     * 重写输入字节流获取方法
     *
     * @return ServletInputStream
     * @throws IOException IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            /**
             * 输入流读取完毕标记
             * @return boolean
             */
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            /**
             * 输入流是否准备完毕，可以read()
             * @return boolean
             */
            @Override
            public boolean isReady() {
                return true;
            }

            /**
             * 请求体是同步阻塞获取的，如果异步环境使用wraaper会抛异常
             * @param readListener 非阻塞IO监听器
             */
            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            /**
             * 读取输入流
             * @return int
             * @throws IOException IOException
             */
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * 重写输入字符流获取方法
     *
     * @return BufferedReader
     * @throws IOException IOException
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * 字节流转String
     *
     * @return String
     */
    public String getBodyAsString() {
        return new String(this.body, StandardCharsets.UTF_8);
    }
}
