package io.geetam.github.UniqueAttributes;

import dbridge.analysis.eqsql.hibernate.construct.Utils;
import org.objectweb.asm.attrs.Annotation;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.*;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UniqueAttributes {
    public static void main(String[] args) {
        String classSig = "com.spring.bioMedical.entity.Admin";
        System.out.println("soot cp: " + Scene.v().getSootClassPath());
        System.out.println("java cp: " + System.getProperty("java.class.path"));
        Scene.v().setSootClassPath(System.getProperty("java.class.path"));
        Scene.v().setPhantomRefs(true);
        SootClass sc = Scene.v().loadClass(classSig, 1);
        Chain<SootField> fields = sc.getFields();
        List <SootField> uniqueFields = new ArrayList<>();
        for(SootField sf : fields) {
            List <Tag> fieldTags = sf.getTags();
            List<AnnotationTag> anns = Utils.getAnnotationTags(fieldTags);
            System.out.println(anns);
            for(AnnotationTag at : anns) {
                if(at.getType().toString().equals("Ljavax/persistence/Column;")) {
                    Collection<AnnotationElem> atElems = at.getElems();
                    for(AnnotationElem atEle : atElems) {
                        System.out.println("atEle: " + atEle);
                        if(atEle.getName().equals("unique")
                                && atEle instanceof AnnotationIntElem
                                    && ((AnnotationIntElem) atEle).getValue() == 1) {
                            uniqueFields.add(sf);
                        }
                    }
                }
            }
        }
        System.out.println("uniqueFields: " + uniqueFields);
    }
}
