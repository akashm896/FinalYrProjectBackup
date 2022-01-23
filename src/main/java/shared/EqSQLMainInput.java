/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package shared;

import java.io.Serializable;
import java.util.List;

/**
 * Created by K. Venkatesh Emani on 5/2/2017.
 * Data structure that holds information that is passed as input to
 * EqSQLMain from rewrite plugin.
 */
public class EqSQLMainInput implements Serializable {
    public String projectRootDir;
    public String inRoot;
    public String outRoot;
    public String classSignature;
    public List<String> applicableFuncSignatures;

    public EqSQLMainInput(String projectRootDir, String inRoot, String outRoot, String classSignature, List<String> applicableFuncSignatures) {
        this.projectRootDir = projectRootDir;
        this.inRoot = inRoot;
        this.outRoot = outRoot;
        this.classSignature = classSignature;
        this.applicableFuncSignatures = applicableFuncSignatures;
    }
}
