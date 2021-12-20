package io.geetam.github.RepoToEntity;

import mytest.debug;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Signature;


public class RepoToEntity {
    public static String getEntityForRepo(String repo) {
        try {
            debug d = new debug("RepoToEntity.java", "getEntityForRepo");
            JavaClass repocls =  Repository.lookupClass(repo);
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
        }
        return null;
    }
}
