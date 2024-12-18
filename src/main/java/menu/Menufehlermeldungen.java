package menu;

public enum Menufehlermeldungen {
    WAHLNUMMER_NICHT_KORREKT("Wahlnummer inkorrekt"),
    ERNEUT_VERSUCHEN("Erneut versuchen? (y/n) "),
    BETRAG_FORMAT_FALSCH("kein gültiger Betrag, erlaubt sind ausdrücke wie 33.22 oder 0.1");

    private final String name;
    private final Runnable action;

    Menufehlermeldungen(String s) {
        this.name = s;
        this.action = () -> System.out.println(this.name);
    }

    public void print() {
        action.run();
    }
}