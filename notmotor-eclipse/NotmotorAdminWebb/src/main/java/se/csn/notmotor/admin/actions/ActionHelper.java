package se.csn.notmotor.admin.actions;

import java.sql.Connection;

import javax.sql.DataSource;

import se.csn.notmotor.admin.ResourceFactory;
import se.csn.notmotor.admin.ResourceFactoryImpl;


public class ActionHelper {

    private static DataSource ds;
    private static int isolationLevel = Connection.TRANSACTION_READ_COMMITTED;

    public static ResourceFactory getResourceFactory() {
        if (ds == null) {
            throw new IllegalStateException("DataSourcen har inte satts än!");
        }
        return new ResourceFactoryImpl(ds, isolationLevel);
    }

    public static void setDatasource(DataSource ds) {
        ActionHelper.ds = ds;
    }

    public static void setTransactionIsolationLevel(int isolationLevel) {
        ActionHelper.isolationLevel = isolationLevel;
    }

    private ActionHelper() {
    }
}
