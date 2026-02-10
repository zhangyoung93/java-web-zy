package com.zy.demo.constant;

import java.util.regex.Pattern;

/**
 * 正则表达式匹配
 *
 * @author zy
 */
public class PatternConstant {

    /**
     * 匹配空格、制表符、换行、回车
     */
    public static final Pattern BLANK_PATTERN = Pattern.compile("^\\s*");
}
