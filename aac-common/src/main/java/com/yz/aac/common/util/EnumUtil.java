package com.yz.aac.common.util;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 枚举工具类
 */
public final class EnumUtil {

    /**
     * 根据枚举的code获取该枚举
     * @param clz
     * @param code
     * @param <E>
     * @return
     */
    public static <E> E getByCode(Class<E> clz, int code) {
        try {
            for (E e : clz.getEnumConstants()) {
                Field field = clz.getDeclaredField("code");
                field.setAccessible(true);
                if (Objects.equals(field.get(e), code)) {
                    return e;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("根据code获取枚举实例异常" + clz + " code:" + code, e);
        }
        return null;
    }

}
