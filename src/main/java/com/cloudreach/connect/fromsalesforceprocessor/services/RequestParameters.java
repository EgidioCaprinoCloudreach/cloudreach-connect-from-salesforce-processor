package com.cloudreach.connect.fromsalesforceprocessor.services;

import com.cloudreach.connect.api.context.SynchronousContext;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;

@Singleton
public class RequestParameters {

    private final SynchronousContext synchronousContext;

    @Inject
    public RequestParameters(SynchronousContext synchronousContext) {
        this.synchronousContext = synchronousContext;
    }

    public long getRequiredLong(String param) {
        String stringValue = getRequiredString(param);
        return Long.parseLong(stringValue);
    }

    public String getRequiredString(String param) {
        String value = synchronousContext.getRequestParameter(param);
        value = StringUtils.trimToNull(value);
        if (value == null) {
            throw new BadRequestException("Invalid or missing parameter: " + param);
        }
        return value;
    }

}
