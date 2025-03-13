package org.example.passwordguessing;

import java.util.*;
import java.util.function.BiConsumer;

public class SimulationLogic {
    public static final int CODE_LENGTH = 6;
    public static final int MAX_DIGIT = 9;
    public static final int TOTAL_COMBINATIONS = 210; // C(10,6) = 210


    /**
     * it runs multiple simulations and log the result
     *
     * @param attempts         number of attempts
     * @param simulations      number of simulations
     * @param progressCallback we use callback for login the information
     * @return Результат усіх симуляцій
     */
    public static SimulationResult runMultipleSimulations(int attempts, int simulations,
                                                          BiConsumer<Integer, SimulationLog> progressCallback) {
        int successCount = 0;

        for (int sim = 0; sim < simulations; sim++) {
            SimulationLog log = simulateCodeGuessing(attempts);
            if (log.isSuccess()) {
                successCount++;
            }

            // Викликаємо callback функцію для відстеження прогресу, якщо вона існує
            if (progressCallback != null) {
                progressCallback.accept(sim, log);
            }
        }

        double experimentalProbability = (double) successCount / simulations;
        return new SimulationResult(successCount, simulations - successCount, experimentalProbability);
    }

    /**
     * One simulation of guessing
     *
     * @param attemptsNumber number of attempts
     * @return log with the simulation parameters
     */
    public static SimulationLog simulateCodeGuessing(int attemptsNumber) {
        // generate random 6-digit code that we would guess
        int[] actualCode = generateRandomIncreasingCode();

        //we save already attempted codes to avoid repeating them
        Set<int[]> attemptedCodes = new HashSet<>();

        // guessing the code as soon as we have attempts
        for (int i = 0; i < attemptsNumber; i++) {
            int[] attempt = generateRandomIncreasingCode();

            // dont count the attempt if it was attempted previously
            if (attemptedCodes.contains(attempt)) {
                --i;
                continue;
            }
            attemptedCodes.add(attempt);

            // if we guessed we save info to log object, with success = true
            if (Arrays.equals(attempt, actualCode)) {
                return new SimulationLog(codeToString(actualCode),
                        i + 1,
                        true);
            }
        }

        // if we never guessed the actual code we return log with sucсess = false
        return new SimulationLog(codeToString(actualCode),
                attemptsNumber,
                false);
    }

    /**
     * Generates 6-digit increasing code
     *
     * @return 6 int element array with increasing number sequence
     */
    public static int[] generateRandomIncreasingCode() {
        // generating 6 unique numbers from 0 to 9
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= MAX_DIGIT; i++) {
            digits.add(i);
        }

        Collections.shuffle(digits);

        // after shuffling take first 6 numbers and sort them
        int[] code = new int[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = digits.get(i);
        }

        // selection sort (no need to use Arrays built-in quick-sort algorithm)
        for (int i = 0; i < CODE_LENGTH; i++) {
            int minIndx = i;
            for (int j = i + 1; j < CODE_LENGTH; j++) {
                minIndx = code[minIndx] < code[j] ? minIndx : j;
            }
            int temp = code[i];
            code[i] = code[minIndx];
            code[minIndx] = temp;
        }
        return code;
    }

/**
 * Converts int array to String without commas or whitespaces
 *
 * @param code array with the code represented by integers
 * @return string that represents code in characters
 */

    public static String codeToString(int[] code) {
        StringBuilder sb = new StringBuilder();
        for (int digit : code) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public static double calculateTheoretical(int attempts){
        double failChance = (double) (TOTAL_COMBINATIONS - 1) /TOTAL_COMBINATIONS; // the chance of failing the guess with 1 attempt
        double multipleAttemptFailChance = Math.pow(failChance,attempts); // the chance of failing the guess with multiple attempts
        double sucessChance = 1 - multipleAttemptFailChance; // if we subtract the failing chance from 1 we get success chance
        return sucessChance;
    }

}