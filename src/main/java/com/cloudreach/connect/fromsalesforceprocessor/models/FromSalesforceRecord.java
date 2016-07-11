package com.cloudreach.connect.fromsalesforceprocessor.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FromSalesforceRecord {

    private Long idFromSalesforceRecord;
    private String salesforceObject;
    private String salesforceId;
    private String data;
    private Boolean processed;
    private Timestamp processedDate;
    private Timestamp insertDate;
    private String lastError;
    private Long priority;

}
