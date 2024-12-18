package csv;

public enum ExportTypes {

    KONTOAUSZUG("Kontoauszug "),
    NACHRICHTEN("Nachrichten "),
    PINNWANDEINTRAEGE("Pinnwandnachrichten ");

    private String name;

    ExportTypes(String name) {
        this.name = name;
    }

    public ExportTypes addInfo(String info) {
        this.name = getName() + info;
        return this;
    }

    public String getName() {
        return name;
    }
}
