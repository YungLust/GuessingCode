package org.example.passwordguessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationLogic {
    public static final int CODE_LENGTH = 6;
    public static final int MAX_DIGIT = 9;
    public static final int TOTAL_COMBINATIONS = 210; // C(10,6) = 210


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
            int min = code[i];
            for(int j = i; j<CODE_LENGTH;j++){
                min = Math.min(code[j],min);
            }
            code[i] = min;
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

}