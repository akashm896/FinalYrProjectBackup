/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package config.test;

import java.util.List;

/**
 * Created by venkatesh on 5/7/17.
 * Interface for run configuration. All run configurations specific to any project
 * must implement this to provide necessary parameters to run EqSQL.
 */
public interface EqSQLRunConfig {
    String getInputRoot();
    String getOutputRoot();
    List<FuncSignature> getFuncSignatures();
    FuncSignature getFuncSignature(int index);
}
