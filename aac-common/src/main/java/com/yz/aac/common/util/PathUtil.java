package com.yz.aac.common.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathUtil {

    private static final PathMatcher matcher = new AntPathMatcher();

    public static boolean match(String pattern, String path) {
        return matcher.match(pattern, path);
    }
}
