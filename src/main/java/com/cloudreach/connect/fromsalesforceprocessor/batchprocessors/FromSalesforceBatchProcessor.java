package com.cloudreach.connect.fromsalesforceprocessor.batchprocessors;

import com.cloudreach.connect.api.Result;
import com.cloudreach.connect.api.batch.BatchProcessor;
import com.cloudreach.connect.api.context.PluginContext;
import com.cloudreach.connect.api.event.Queue;
import com.cloudreach.connect.api.persistence.Transaction;
import com.cloudreach.connect.fromsalesforceprocessor.dao.FromSalesforceRecordDao;
import com.cloudreach.connect.fromsalesforceprocessor.models.FromSalesforceRecord;
import com.cloudreach.connect.fromsalesforceprocessor.services.FeatherModule;
import org.codejargon.feather.Feather;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FromSalesforceBatchProcessor implements BatchProcessor {

    @Override
    public int getFrequency() {
        return 1;
    }

    @Override
    public UnitOfTime getUnitOfTime() {
        return UnitOfTime.MINUTES;
    }

    @Override
    public Result implement(final PluginContext pluginContext) throws Exception {
        pluginContext.getPersistence().runTransaction(new Transaction() {
            @Override
            public void implement(Connection connection) throws Exception {
                Feather feather = Feather.with(new FeatherModule(pluginContext, connection));
                FromSalesforceRecordDao fromSalesforceRecordDao = feather.instance(FromSalesforceRecordDao.class);

                process(fromSalesforceRecordDao, pluginContext);
            }
        });

        return null;
    }

    private void process(FromSalesforceRecordDao fromSalesforceRecordDao, PluginContext pluginContext) throws SQLException {
        List<FromSalesforceRecord> unprocessedFromSalesforceRecords = fromSalesforceRecordDao.findAllByProcessed(false);
        for (FromSalesforceRecord fromSalesforceRecord : unprocessedFromSalesforceRecords) {
            Queue queue = pluginContext.getQueue("FromSalesforceRecord" + fromSalesforceRecord.getSalesforceObject());
            queue.publish(fromSalesforceRecord);
        }
    }

}
