package com.cloudreach.connect.fromsalesforceprocessor.restservices;

import com.cloudreach.alumina.utils.W;
import com.cloudreach.connect.api.RestService;
import com.cloudreach.connect.api.Result;
import com.cloudreach.connect.api.context.SynchronousContext;
import com.cloudreach.connect.api.persistence.Transaction;
import com.cloudreach.connect.fromsalesforceprocessor.dao.FromSalesforceRecordDao;
import com.cloudreach.connect.fromsalesforceprocessor.models.FromSalesforceRecord;
import com.cloudreach.connect.fromsalesforceprocessor.services.FeatherModule;
import com.cloudreach.connect.fromsalesforceprocessor.services.RequestParameters;
import org.apache.commons.io.IOUtils;
import org.codejargon.feather.Feather;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Properties;

import static jersey.repackaged.com.google.common.base.Preconditions.checkArgument;

public class FromSalesforceRestService implements RestService {

    @Override
    public double getVersion() {
        return 1;
    }

    @Override
    public String getEndpoint() {
        return "/from-salesforce-record";
    }

    @Override
    public String getApplicationKey() {
        Feather feather = Feather.with(new FeatherModule());
        Properties properties = feather.instance(Properties.class);
        return properties.getProperty("plugin.appKey");
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public Result implement(final SynchronousContext synchronousContext) throws Exception {
        final W wrapper = new W();

        synchronousContext.getPersistence().runTransaction(new Transaction() {
            @Override
            public void implement(Connection connection) throws Exception {
                Feather feather = Feather.with(new FeatherModule(synchronousContext, connection));
                FromSalesforceRecordDao fromSalesforceRecordDao = feather.instance(FromSalesforceRecordDao.class);
                RequestParameters requestParameters = feather.instance(RequestParameters.class);

                String requestBody;
                try (InputStream inputStream = synchronousContext.getRequestBody()) {
                    requestBody = IOUtils.toString(inputStream);
                }

                String salesforceObject = requestParameters.getRequiredString("salesforce-object");
                String salesforceId = requestParameters.getRequiredString("salesforce-id");
                long priority = requestParameters.getRequiredLong("priority");

                wrapper.value = save(fromSalesforceRecordDao, requestBody, salesforceObject, salesforceId, priority);
            }
        });

        return synchronousContext.createResult(wrapper.value);
    }

    private FromSalesforceRecord save(FromSalesforceRecordDao fromSalesforceRecordDao, String requestBody, String salesforceObject, String salesforceId, long priority) throws Exception {
        FromSalesforceRecord fromSalesforceRecord = fromSalesforceRecordDao.findOneBySalesforceId(salesforceId);
        if (fromSalesforceRecord == null) {
            fromSalesforceRecord = new FromSalesforceRecord();
            fromSalesforceRecord.setSalesforceObject(salesforceObject);
            fromSalesforceRecord.setSalesforceId(salesforceId);
        } else {
            checkArgument(salesforceObject.equals(fromSalesforceRecord.getSalesforceObject()));
        }

        fromSalesforceRecord.setData(requestBody);
        fromSalesforceRecord.setProcessed(false);
        fromSalesforceRecord.setInsertDate(new Timestamp(System.currentTimeMillis()));
        fromSalesforceRecord.setPriority(priority);

        fromSalesforceRecordDao.save(fromSalesforceRecord);

        return fromSalesforceRecord;
    }

}
