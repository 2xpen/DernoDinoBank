package repository.konto;

public class TransaktionResponseWrapper {
    private final TransaktionExitCode exitCode;
    private final TransaktionSummary transaktionsSummary;

    TransaktionResponseWrapper(TransaktionExitCode exitCode, TransaktionSummary transaktionsSummary) {
        this.exitCode = exitCode;
        this.transaktionsSummary = transaktionsSummary;
    }

    public TransaktionExitCode getExitCode() {
        return exitCode;
    }

    public TransaktionSummary getTransaktionSummary() {
        return transaktionsSummary;
    }
}