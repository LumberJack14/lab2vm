import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatrixManager {

    private static int  size = 2;

    public static boolean isDiagonallyDominant(double[][] matrix) {
        if (matrix == null || size == 0) {
            System.out.println("Matrix is not initialized.");
            return false;
        }

        boolean isDominant = true;

        for (int i = 0; i < size; i++) {
            double diagonalValue = Math.abs(matrix[i][i]);
            double sumOfOffDiagonals = 0.0;

            for (int j = 0; j < size; j++) {
                if (j != i) {
                    sumOfOffDiagonals += Math.abs(matrix[i][j]);
                }
            }

            if (diagonalValue <= sumOfOffDiagonals) {
                isDominant = false;
                break;
            }
        }

        return isDominant;
    }


    public static double[][] makeDiagonallyDominant(double[][] matrix) {
        if (matrix == null || size == 0) {
            System.out.println("Matrix is not initialized.");
            return null;
        }

        if (isDiagonallyDominant(matrix)) {
            System.out.println("Matrix is already diagonally dominant.");
            return matrix;
        }

        System.out.println("Attempting to make the matrix diagonally dominant...");

        double[][] temp = matrix;

        List<Integer> columnIndices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            columnIndices.add(i);
        }

        List<List<Integer>> permutations = new ArrayList<>();
        generatePermutations(columnIndices, 0, permutations);

        for (List<Integer> permutation : permutations) {
            if (tryPermutation(permutation, matrix)) {
                System.out.println("Matrix is now diagonally dominant after rearranging columns.");
                return matrix;
            }
        }


        System.out.println("Unable to make the matrix diagonally dominant by rearranging columns.");
        return matrix;
    }

    private static void generatePermutations(List<Integer> columns, int start, List<List<Integer>> result) {
        if (start == columns.size() - 1) {
            result.add(new ArrayList<>(columns));
        } else {
            for (int i = start; i < columns.size(); i++) {
                Collections.swap(columns, start, i);
                generatePermutations(columns, start + 1, result);
                Collections.swap(columns, start, i);
            }
        }
    }

    private static boolean tryPermutation(List<Integer> permutation, double[][] matrix) {
        double[][] tempMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempMatrix[i][j] = matrix[i][permutation.get(j)];
            }
        }
        matrix = tempMatrix;
        return isDiagonallyDominant(matrix);
    }

    private static void displayMatrix(double [][] matrix, double[] b) {
        int size = 2;

        System.out.println("Current Matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%2s \t", matrix[i][j]);
            }
            System.out.print("|\t" + b[i]);
            System.out.println();
        }
    }
}
