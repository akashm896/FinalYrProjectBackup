/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
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
