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
