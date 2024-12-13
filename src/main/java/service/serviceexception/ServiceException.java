package service.serviceexception;

public abstract class ServiceException extends Exception {

    private final ServiceErrorArt art;

    public ServiceException(ServiceErrorArt art, ServiceErrorMessageProvider message) {
        super(message.getServiceErrorMessage());
        this.art = art;
    }

    public ServiceErrorArt getArt() {
        return art;
    }

}
