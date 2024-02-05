package falconsTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReflexAirfoilDetector {
    public static void main(String[] args) {
        // Path to the folder containing airfoil data files
        String folderPath = "C:\\Aero\\PADA task\\Airfoils dat\\coord_seligFmt";

        // Create a File object representing the folder
        File folder = new File(folderPath);
        
        // List .dat files in the folder using a lambda expression
        File[] datFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));

        // Check if any .dat files were found
        if (datFiles != null) {
            // Loop through each .dat file
            for (File datFile : datFiles) {
                // Process each airfoil file
                processAirfoil(datFile);
            }
        } else {
            // Print a message if no .dat files were found
            System.out.println("No DAT files found in the specified folder.");
        }
    }

    // Method to process each airfoil file
    static void processAirfoil(File datFile) {
        try {
            // Get the name of the airfoil file
            String airfoilName = datFile.getName();
            
            // Read airfoil coordinates from the file
            double[][] airfoilPoints = readSeligAirfoil(datFile.getAbsolutePath(), 10);
            
            // Array to store slopes between consecutive points
            double slopes[] = new double[10] ;
            
            // Calculate slopes between consecutive points
            for ( int i = 1 ; i < 10 ; i++ ) { 
                slopes[i - 1] = slope(airfoilPoints[i-1][1], airfoilPoints[i][1], airfoilPoints[i-1][0], airfoilPoints[i][0]) ;
            }
            
            // Variables to count concave up sections and upward trailing edges
            int concaveUpCount = 0 ;
            int upwardTrailingEdgeCount = 0 ;
            
            // Check slopes to determine concave up sections and upward trailing edges
            for ( int i = 1 ; i < 9 ; i ++ ) { 
                if ( slopes[i] > slopes[i+1] )
                    concaveUpCount++ ;
                if ( slopes[i] > 0 )
                    upwardTrailingEdgeCount++ ; 
            }
            
            // Print airfoil name if it meets criteria for being reflex
            if ( concaveUpCount >= 3 || upwardTrailingEdgeCount >= 3 ) { 
                System.out.println(airfoilName ) ;
            }
            
        } catch (IOException e) {
            // Print stack trace if an exception occurs during file processing
            e.printStackTrace();
        }
    }

    // Method to calculate slope between two points
    static double slope(double y2, double y1, double x2, double x1) {
        double xdiff = (x2 - x1);
        double ydiff = y2 - y1;
        return ydiff / xdiff;
    }

    // Method to read airfoil coordinates from a file
    static double[][] readSeligAirfoil(String filePath, int maxPoints) throws IOException {
        // Array to store airfoil coordinates
        double[][] points = new double[maxPoints][2];
        int currentIndex = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the first line (header)
            reader.readLine();
            
            // Read airfoil coordinates line by line
            String line;
            while ((line = reader.readLine()) != null && currentIndex < maxPoints) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    // Parse x and y coordinates from each line
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    // Store coordinates in the points array
                    points[currentIndex][0] = x;
                    points[currentIndex][1] = y;
                    currentIndex++;
                }
            }
        }

        return points;
    }
}
