package data.identifier;

import java.util.UUID;

public abstract class IdentifierBase {
    private final String identifier;


    protected IdentifierBase(String identifier) {
        this.identifier = identifier;
    }

    protected IdentifierBase() {
        /// TODO TODO TODO das hier ist wirklich nur provisorisch, ich will nixht iwie n substring machen müssen
        this.identifier = UUID.randomUUID().toString().substring(0, 30);
    }

    @Override
    public String toString() {
        return identifier;
    }

}
