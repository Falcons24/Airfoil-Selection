
package falconsTools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class airfoilDataReader {
    public static void main(String[] args) {
        // Specify the folder path containing CSV files
        String folderPath = "C:\\Aero\\New Folder";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        System.out.println("Initializing");

        // Loop through each file in the folder
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    // Count the number of rows and columns in the CSV file
                    int rowCount = 0;
                    int colCount = 0;
                    int ZeroMomentCounter = 0;
                    int numberRowIndex = -1;
                    String airfoilName = "";
                    double Clmax = Double.MIN_VALUE;
                    double alpha_Clmax = 0.0;
                    double Cdmin = Double.MAX_VALUE;
                    double CL_Cdmin = 0.0;
                    double alpha_Cdmin = 0.0;
                    double Cl_0 = 0.0;
                    double Cl_2 = 0.0;
                    // Remaining variable declarations...

                    // Read the CSV file to determine number of rows and columns
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (rowCount == 2) {
                            airfoilName = line;
                        }
                        String[] row = line.split(",");
                        if (numberRowIndex == -1 && row.length > 0 && isNumeric(row[0]) && isInRange(Double.parseDouble(row[0]), -18.1, 25)) {
                            numberRowIndex = rowCount;
                        }
                        rowCount++;
                        colCount = Math.max(colCount, row.length);
                    }

                    // Initialize 2D arrays based on row and column count
                    String[][] data = new String[rowCount][colCount];
                    double[][] numberData;
                    if (numberRowIndex != -1) {
                        numberData = new double[rowCount - numberRowIndex][colCount + 2];
                    } else {
                        numberData = new double[0][0];
                    }

                    // Read the CSV file again and populate the 2D arrays
                    int rowIndex = 0;
                    int numberDataIndex = 0;
                    br.close();
                    br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        String[] row = line.split(",");
                        for (int colIndex = 0; colIndex < row.length; colIndex++) {
                            data[rowIndex][colIndex] = row[colIndex];
                            if (rowIndex >= numberRowIndex && !row[colIndex].isEmpty()) {
                                numberData[numberDataIndex][colIndex] = Double.parseDouble(row[colIndex]);
                            }
                        }
                        if (rowIndex >= numberRowIndex) {
                            if (!Double.isNaN(numberData[numberDataIndex][1]) && !Double.isNaN(numberData[numberDataIndex][2])) {
                                // Calculate additional values for numberData
                                double value1 = numberData[numberDataIndex][1] / numberData[numberDataIndex][2];
                                double value2 = Math.pow(numberData[numberDataIndex][1], 3) / Math.pow(numberData[numberDataIndex][2], 2);
                                numberData[numberDataIndex][colCount] = value1;
                                numberData[numberDataIndex][colCount + 1] = value2;
                            }
                            numberDataIndex++;
                        }
                        rowIndex++;
                    }

                    // Delete rows with 0 in column index 2 from numberData
                    double[][] filteredNumberData = filterRowsWithZero(numberData, 2);
                    if (filteredNumberData.length > 0) {
                        // Perform calculations on filteredNumberData
                        // Calculate additional variables...
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to delete and move rows in a CSV file
    public static void deleteAndMoveRows(String inputFilePath, String outputFilePath, int rowToDelete) throws IOException {
        // Implementation
        List<String[]> rowsToKeep = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                currentRow++;

                // Skip the specified row
                if (currentRow == rowToDelete) {
                    continue;
                }

                String[] rowValues = line.split(",");
                rowsToKeep.add(rowValues);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String[] row : rowsToKeep) {
                StringBuilder newRow = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    newRow.append(row[i]);
                    if (i < row.length - 1) {
                        newRow.append(",");
                    }
                }
                writer.write(newRow.toString());
                writer.newLine();
            }
        }
    }

    // Method to append a line to a CSV file
    private static void appendLineToFile(String filePath, String[] line) {
        // Implementation
        try {
            FileWriter writer = new FileWriter(filePath, true); // Append mode

            writer.append(String.join(",", line));
            writer.append("\n");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if a string is numeric
    private static boolean isNumeric(String str) {
        // Implementation
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // Helper method to check if a value is within a range
    private static boolean isInRange(double value, double min, double max) {
        // Implementation
        return value >= min && value <= max;
    }

    // Helper method to filter rows with a specific value in a column
    private static double[][] filterRowsWithZero(double[][] array, int column) {
        // Implementation
        int count = 0;
        for (double[] row : array) {
            if (row[column] != 0) {
                count++;
            }
        }

        double[][] filteredArray = new double[count][array[0].length];
        int index = 0;
        for (double[] row : array) {
            if (row[column] != 0) {
                filteredArray[index] = row;
                index++;
            }
        }

        return filteredArray;
    }

    // Method to fix backslashes in a string
    static String fixString(String s) {
        // Implementation
        String news = "";
        for (int i = 0; i < s.length(); i++) {
            news = news + s.charAt(i);
            if (s.charAt(i) == '\\')
                news += '\\';
        }
        return news;
    }
}
