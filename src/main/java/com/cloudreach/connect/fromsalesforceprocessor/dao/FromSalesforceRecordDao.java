package com.cloudreach.connect.fromsalesforceprocessor.dao;

import com.cloudreach.connect.fromsalesforceprocessor.models.FromSalesforceRecord;
import com.cloudreach.connect.fromsalesforceprocessor.services.FeatherModule;
import com.cloudreach.connect.orm.Dao;
import org.codejargon.feather.Feather;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Singleton
public class FromSalesforceRecordDao extends Dao<FromSalesforceRecord> {

    @Inject
    public FromSalesforceRecordDao(Connection db) throws SQLException {
        super(db, FromSalesforceRecord.class);
    }

    @Override
    public String getSchema() {
        Feather feather = Feather.with(new FeatherModule());
        Properties properties = feather.instance(Properties.class);
        return properties.getProperty("db.schema");
    }

    @Override
    public String getTable() {
        return "from_salesforce_record";
    }

    public FromSalesforceRecord findOneBySalesforceId(String salesforceId) throws SQLException {
        String query = "SELECT * " +
                "FROM " + getSchema() + "." + getTable() + " " +
                "WHERE salesforce_id = ?";
        Object[] params = {salesforceId};
        return queryRunner.query(db, query, beanHandler, params);
    }

    public List<FromSalesforceRecord> findAllByProcessed(boolean processed) throws SQLException {
        String query = "SELECT * " +
                "FROM " + getSchema() + "." + getTable() + " " +
                "WHERE processed = ? " +
                "ORDER BY priority ASC, insert_date ASC";
        Object[] params = {processed};
        return queryRunner.query(db, query, listHandler, params);
    }

}
