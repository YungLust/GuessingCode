package org.example.passwordguessing;

/**
 * class for saving results of simulation
 * we use it once at the end of simulation to store results
 */
public class SimulationResult {
    private final int successCount;
    private final int failureCount;
    private final double probability;

    public SimulationResult(int successCount, int failureCount, double probability) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.probability = probability;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public int getTotalCount() {
        return successCount + failureCount;
    }

    public double getProbability() {
        return probability;
    }
}
