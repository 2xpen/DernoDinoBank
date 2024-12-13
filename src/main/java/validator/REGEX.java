package validator;

import java.util.regex.Pattern;

public enum REGEX {

    BENUTZERNAME(Pattern.compile("^(?=.{1,100}$)([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$")),
    BETRAG(Pattern.compile("^(0|[1-9]\\d{0,17})(\\.\\d{1,2})?$")),
    BESCHREIBUNG(Pattern.compile("^.{1,200}$")),
    PASSWORT(Pattern.compile("^.{1,30}$"));
    private final Pattern pattern;

    REGEX(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean validate(String input) {
        return this.pattern.matcher(input).matches();
    }

    public Pattern getPattern() {
        return pattern;
    }

}
