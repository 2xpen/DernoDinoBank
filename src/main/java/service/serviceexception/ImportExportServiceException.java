package service.serviceexception;

import csv.CSVException;
import csv.CSV_Handler;

public class ImportExportServiceException extends ServiceException{

    //poah hier nochmal gucken, wills erstmal fertig bekommen
    public ImportExportServiceException(CSVException.Message message) {
        super(ServiceErrorArt.IMPORT_EXPORT_ERROR, message);
    }




}
