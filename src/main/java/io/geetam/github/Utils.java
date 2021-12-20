package io.geetam.github;

import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Utils {
//    public static JavaClass bcelLookupClass(String classQualName) throws ClassNotFoundException {
//        debug d = new debug("io.geetam.github/Utils.java", "bcelLookupClass");
//        String benchDirAbs = getAbsBenchDir();
//        d.dg("benchDirAbs: " + benchDirAbs);
//        try {
//            URL[] classLoaderURLs = new URL[]{new URL("file://" + benchDirAbs)};
//            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderURLs);
//            Class<?> bcelClass = urlClassLoader.loadClass(classQualName);
//            return Repository.lookupClass(bcelClass);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static String getAbsBenchDir()
    {
        debug d = new debug("github/Utils.java", "getAbsBenchDir()");
        String benchDir = CMDOptions.benchDir;
        String pwd = System.getProperty("user.dir");
        d.dg("pwd: " + pwd);
        String benchDirAbs = null;
        if(benchDir.startsWith("/")) {
            benchDirAbs = benchDir;
        } else {
            benchDirAbs = pwd + (pwd.endsWith("/") == false ? "/" : "") + benchDir;
        }
        benchDirAbs = benchDirAbs + (benchDir.endsWith("/") ? "" : "/");
        return benchDirAbs;
    }
}
