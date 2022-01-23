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
