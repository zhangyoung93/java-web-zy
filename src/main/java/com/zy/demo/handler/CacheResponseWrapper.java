package com.zy.demo.handler;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 缓存HttpServletResponse
 *
 * @author zy
 */
public class CacheResponseWrapper extends HttpServletResponseWrapper {

    /**
     * 缓存输出字节流
     */
    private final ByteArrayOutputStream cacheOutputStream;

    private PrintWriter printWriter;

    private ServletOutputStream servletOutputStream;

    public CacheResponseWrapper(HttpServletResponse response) {
        super(response);
        this.cacheOutputStream = new ByteArrayOutputStream();
    }

    /**
     * 重写输出字节流获取方法
     *
     * @return ServletOutputStream
     * @throws IOException IOException
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.servletOutputStream == null) {
            this.servletOutputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                /**
                 * 输出流写到缓存里
                 * @param b   byte num
                 * @throws IOException IOException
                 */
                @Override
                public void write(int b) throws IOException {
                    cacheOutputStream.write(b);
                }
            };
        }
        return this.servletOutputStream;
    }

    /**
     * 重写输出字符流获取方法
     *
     * @return PrintWriter
     * @throws IOException IOException
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.printWriter == null) {
            this.printWriter = new PrintWriter(new OutputStreamWriter(this.cacheOutputStream, StandardCharsets.UTF_8));
        }
        return this.printWriter;
    }

    /**
     * 解析缓存为字节流
     *
     * @return byte[]
     */
    public byte[] getCacheAsBytes() {
        if (this.printWriter != null) {
            this.printWriter.flush();
        }
        return this.cacheOutputStream.toByteArray();
    }

    /**
     * 解析缓存为字符串
     *
     * @return String
     */
    public String getCacheAsString() {
        return new String(getCacheAsBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将缓存覆盖原始响应，输出到客户端
     *
     * @throws IOException IOException
     */
    public void writeToResponse() throws IOException {
        HttpServletResponse response = (HttpServletResponse) getResponse();
        try (ServletOutputStream sos = response.getOutputStream()) {
            sos.write(getCacheAsBytes());
            sos.flush();
        }
    }
}