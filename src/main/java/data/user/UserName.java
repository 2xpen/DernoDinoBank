package data.user;

public class UserName {

    private final String name;


    public UserName(String name) {
        this.name = name;
    }


    //    public UserName(String name) throws InstantiationException {
//        if (!isValid(name)) {
//            throw new InstantiationException("Benutzername muss eine email sein");
//        } else {
//            this.name = name;
//        }
//    }


    @Override
    public String toString() {
        return name;
    }

}
