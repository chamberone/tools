package com.chamberone.tools.remoteExe;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 执行临时代码，不影响正常业务
 * 
 * @author guoping
 *
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String path =
                ".\\target\\test-classes\\org\\service\\api\\RemoteTest.class";
        InputStream is = new FileInputStream(path);
        byte[] b = new byte[is.available()];
        is.read(b);
        is.close();
        String output = JavaClassExecuter.execute(b);
        System.out.print("[" + output + "]");
    }

}
