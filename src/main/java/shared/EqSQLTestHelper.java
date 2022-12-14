/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package shared;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by K. Venkatesh Emani on 5/12/2017.
 */
public class EqSQLTestHelper {

    public static final String TEST_ROOT = "C:/DBridge/test";
    public static final String TEST_RECENT_DIR = TEST_ROOT + "/recent";
    public static final String TEST_OLD_DIR = TEST_ROOT + "/old";

    public static final String TEST_RES_RECENT_PATH = TEST_RECENT_DIR + "/res.ser";
    public static final String TEST_RES_OLD_PATH = TEST_OLD_DIR + "/res.ser";
    public static final String TEST_TIME_RECENT_PATH = TEST_RECENT_DIR + "/time.ser";
    public static final String TEST_TIME_OLD_PATH = TEST_OLD_DIR + "/time.ser";

    public static final String SAVE_OR_COMPARE_FLAG_PATH = TEST_ROOT + "/saveOrCompare.txt";
    public static final String SAVE = "SAVE";
    public static final String COMPARE = "COMPARE";
    public static final double BAD_DOUBLE = -1.0;

    public static void doTest(EqSQLTest test, EqSQLComparator comparator){
        //run test and get time
        long startTime = System.currentTimeMillis();
        Collection results = test.test();
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime-startTime)*1.0/1000; //seconds
        String saveOrCompare = findFlag();

        if(saveOrCompare != null && saveOrCompare.equals(SAVE)) {
            doSave(results, timeTaken);
        }
        else if (saveOrCompare != null && saveOrCompare.equals(COMPARE)){
            if(!doCompare(results, timeTaken, comparator)){
                System.err.println("Results did not match");
            }
            else {
                double oldTimeTaken = findOldTime();
                printSuccess(timeTaken, oldTimeTaken);
            }
        }
        else {
            System.err.println("Expected SAVE or COMPARE. Didn't find either.");
        }
    }

    private static void printSuccess(double timeTaken, double oldTimeTaken) {
        System.out.println("-----------SUCCESS: Results matched successfully.-----------");
        System.out.println("Old time taken(s): " + oldTimeTaken);
        System.out.println("New time taken(s): " + timeTaken);
    }

    private static boolean doCompare(Collection results, double timeTaken, EqSQLComparator comparator) {
        if(!doSave(results, timeTaken)){
            System.err.println("Results could not be saved for comparison");
            return false;
        }
        Collection resultsOld = null;
        try {
            resultsOld = SerializerUtil.deserializeEqSQLTestResult(TEST_RES_OLD_PATH);
            Collection resultsRecent = SerializerUtil.deserializeEqSQLTestResult(TEST_RES_OLD_PATH);
            return comparator.compare(resultsOld, resultsRecent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean doSave(Collection results, double timeTaken){
        try {
            SerializerUtil.serializeEqSQLTestResult(results, TEST_RES_RECENT_PATH);
            System.out.println("Serialized results to: " + TEST_RES_RECENT_PATH);

            SerializerUtil.serializeEqSQLTestTime(timeTaken, TEST_TIME_RECENT_PATH);
            System.out.println("Serialized time to: " + TEST_TIME_RECENT_PATH);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Results saved successfully.");
        return true;
    }

    private static String findFlag() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(SAVE_OR_COMPARE_FLAG_PATH));
            String s = new String(bytes, StandardCharsets.UTF_8);
            s = s.replaceAll("\\r","");
            s = s.replaceAll("\\n","");
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double findOldTime() {
        try {
            return SerializerUtil.deserializeEqSQLTestTime(TEST_TIME_OLD_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BAD_DOUBLE;
    }
}
