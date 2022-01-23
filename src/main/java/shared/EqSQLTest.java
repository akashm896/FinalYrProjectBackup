/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package shared;

import java.util.Collection;

/**
 * Created by K. Venkatesh Emani on 5/12/2017.
 * Wrapper around custom test function whose results can be compared
 * using {@link EqSQLComparator}. Users who wish to compare results
 * of EqSQL rewriting should wrap their test code in the test() function.
 */
public interface EqSQLTest {
    Collection test();
}
