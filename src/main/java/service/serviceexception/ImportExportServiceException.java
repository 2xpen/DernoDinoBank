package service.serviceexception;

import csv.CSVException;

public class ImportExportServiceException extends ServiceException{
    public ImportExportServiceException(CSVException.Message message) {
        super(ServiceErrorArt.IMPORT_EXPORT_ERROR, message);
    }
}
