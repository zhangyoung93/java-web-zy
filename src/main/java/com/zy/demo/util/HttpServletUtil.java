package com.zy.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.demo.pojo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * HttpServlet工具类
 *
 * @author zy
 */
@Slf4j
public class HttpServletUtil {

    /**
     * 解析请求Body
     *
     * @param request request
     * @return 字符串
     * @throws IOException IOException
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            ServletInputStream inputStream = request.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    /**
     * 输出应答
     *
     * @param response response
     * @param code     编码
     * @param msg      描述
     * @param data     数据
     * @throws IOException IOException
     */
    public static void writeResponse(HttpServletResponse response, String code, String msg, Object data) throws IOException {
        Writer writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            BaseResponse<Object> baseResponse = BaseResponse.build(code, msg, data);
            ObjectMapper objectMapper = new ObjectMapper();
            String rspStr = objectMapper.writeValueAsString(baseResponse);
            writer = response.getWriter();
            writer.append(rspStr);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
