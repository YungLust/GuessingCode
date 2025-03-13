package org.example.passwordguessing;

/**
 * class that represents simulation log.
 * We use it for every simulation
 */
public class SimulationLog {
    /**
     * randomly chosen code that is supposed to be guessed
     */
    private final String actualCode;
    /**
     * overall number of guesses
     */
    private final int attemptNumber;

    /**
     * is simulation guessed the code.
     */
    private final boolean success;

    public SimulationLog(String actualCode, int attemptNumber, boolean success) {
        this.actualCode = actualCode;
        this.attemptNumber = attemptNumber;
        this.success = success;
    }

    public String getActualCode() {
        return actualCode;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public boolean isSuccess() {
        return success;
    }
}
