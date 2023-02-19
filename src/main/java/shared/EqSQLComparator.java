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
 * Wrapper for function to compare two collections.
 */
public interface EqSQLComparator {
    /**
     * Users writing tests should implement this function to compare
     * results of two collections.
     * @param c1 Any collection
     * @param c2 Any collection
     * @return True if results of c1 and c2 match, false otherwise.
     */
    boolean compare(Collection c1, Collection c2);
}
