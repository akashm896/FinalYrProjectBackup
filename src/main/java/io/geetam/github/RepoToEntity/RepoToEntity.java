package io.geetam.github.RepoToEntity;

import dbridge.analysis.eqsql.EqSQLDriver;
import io.geetam.github.CMDOptions;
import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Signature;
import org.w3c.dom.Attr;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.apache.bcel.Repository.lookupClass;

public class RepoToEntity {
    public static String getEntityForRepo(String repo) {
        try {
            debug d = new debug("RepoToEntity.java", "getEntityForRepo");
            String benchDir = CMDOptions.benchDir;
            String pwd = System.getProperty("user.dir");
            d.dg("pwd: " + pwd);
            d.dg("repo: " + repo);
            String benchDirAbs = null;
            if(benchDir.startsWith("/")) {
                benchDirAbs = benchDir;
            } else {
                benchDirAbs = pwd + (pwd.endsWith("/") == false ? "/" : "") + benchDir + (benchDir.endsWith("/") ? "" : "/");
            }
            d.dg("benchDirAbs: " + benchDirAbs);
            URL[] classLoaderURLs = new URL[]{new URL("file://" + benchDirAbs)};
            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderURLs);
            Class<?> repoClass = urlClassLoader.loadClass(repo);
            JavaClass repocls = Repository.lookupClass(repoClass);
            Attribute[] atts = repocls.getAttributes();
            for(Attribute att : atts) {
                if(att instanceof Signature) {
                    Signature sig  = (Signature) att;
                    String sigstr = sig.getSignature();
                    int indAngledOpen = sigstr.indexOf("<");
                    String fromAngledOpen = sigstr.substring(indAngledOpen);
                    int indsemicol = fromAngledOpen.indexOf(";");
                    String entity = fromAngledOpen.substring(2,indsemicol).replace("/", ".");
                    return entity;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
