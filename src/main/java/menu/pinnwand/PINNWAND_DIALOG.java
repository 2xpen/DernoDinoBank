package menu.pinnwand;

public enum PINNWAND_DIALOG {
    BESITZER_VON_PINNWAND_EINGEBEN("Bitte gib den Namen von dem Besitzer der Pinnwand ein:"),
    PINNWAND_IST_LEER("Diese Pinnwand ist leer"),
    PINNWAND_NACHRICHT_EINGEBEN("Bitte gib deine Nachricht ein:"),
    PINNWAND_NACHRICHT_ERSTELLT("Pinnwandeintrag wurde erstellt");

    private final String text;
    private final Runnable action;

    PINNWAND_DIALOG(String text){
        this.text = text;
        this.action = () -> System.out.println(text);
    }

    public static void printAll() {
        for (PINNWAND_DIALOG option : PINNWAND_DIALOG.values()) {
            option.action.run();
        }
    }

    public String getText() {
        return text;
    }

    public void print(){
        this.action.run();
    }
}
