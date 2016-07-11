## Install

- Create a file `from-salesforce-processor.properties` in your classpath root. With the `db.schema`, `plugin.appKey`
properties.

## Usage

The `FromSalesforceBatchProcessor` will trigger an event for each unprocessed record. The event name will be `FromSalesforceRecord<SalesforceObject>`.
For example, for an Account, it will be `FromSalesforceRecordAccount`.

The event data will be an instance of `FromSalesforceRecord` model class. When the listener has processed this record,
it must set the `processed` column to `true`. In case of error, the best practice is to set the `processed` column to
true too, and set the `last_error` column with the error details.