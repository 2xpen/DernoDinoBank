package csv;

public enum ExportTypes {

    KONTOAUSZUG("Kontoauszug "),
    NACHRICHTEN("Nachrichten mit "),
    PINNWANDEINTRAEGE("Pinnwandnachrichten von ");


    private String name;

    ExportTypes(String name) {
        this.name = name;
    }

    public ExportTypes addInfo(String info) {
        this.name = this.name.formatted(info);
        return this;
    }

    public String getName() {
        return name;
    }
}
