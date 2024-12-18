package data.identifier;

import java.util.Objects;
import java.util.UUID;

public abstract class IdentifierBase {
    private final String identifier;
    protected IdentifierBase(String identifier) {
        this.identifier = identifier;
    }

    protected IdentifierBase() {
        this.identifier = UUID.randomUUID().toString().substring(0, 30);
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierBase that = (IdentifierBase) o;
        return Objects.equals(identifier, that.identifier);
    }
}