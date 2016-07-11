package com.cloudreach.connect.fromsalesforceprocessor.pluginlifecycles;

import com.cloudreach.connect.api.PluginLifecycle;
import com.cloudreach.connect.api.context.PluginContext;
import com.cloudreach.connect.api.persistence.PersistenceException;
import com.cloudreach.connect.api.persistence.Transaction;
import com.cloudreach.connect.fromsalesforceprocessor.services.FeatherModule;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.IOUtils;
import org.codejargon.feather.Feather;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DbPluginLifecycle implements PluginLifecycle {

    @Override
    public void onStartup(PluginContext pluginContext) throws Exception {
        setUpDb(pluginContext);
    }

    @Override
    public void onInstallOrUpdate(PluginContext pluginContext) throws Exception {
        setUpDb(pluginContext);
    }

    private void setUpDb(PluginContext pluginContext) throws PersistenceException, IOException {
        Feather feather = Feather.with(new FeatherModule());
        final QueryRunner queryRunner = feather.instance(QueryRunner.class);
        Properties properties = feather.instance(Properties.class);

        final String ddl;
        try (InputStream inputStream = DbPluginLifecycle.class.getResourceAsStream("/from-salesforce-processor.ddl")) {
            ddl = IOUtils.toString(inputStream).replace("__SCHEMA__", properties.getProperty("db.schema"));
        }

        pluginContext.getPersistence().runTransaction(new Transaction() {
            @Override
            public void implement(Connection db) throws Exception {
                queryRunner.update(db, ddl);
            }
        });
    }

}
