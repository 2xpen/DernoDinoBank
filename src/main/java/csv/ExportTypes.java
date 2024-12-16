package csv;

public enum ExportTypes {

    KONTOAUSZUG("Kontoauszug "),
    NACHRICHTEN("Nachrichten "),
    PINNWANDEINTRAEGE("PinnwandEintraege vom");


    private String name;

    ExportTypes(String name) {
        this.name = name;
    }

    public ExportTypes addInfo(String info) {
        this.name = this.name.formatted(" mit: "+info+" ");
        return this;
    }

    public String getName() {
        return name;
    }
}
