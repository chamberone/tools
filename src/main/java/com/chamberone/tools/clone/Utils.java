package com.chamberone.tools.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 克隆对象
 * 
 * @author guoping
 *
 */
public class Utils {

    /**
     * 简单深度克隆
     * 
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T clone(T t) {
        if (null == t) {
            return null;
        }
        try {
            // 序列化
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(t);
            // 反序列化
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (T) oi.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
