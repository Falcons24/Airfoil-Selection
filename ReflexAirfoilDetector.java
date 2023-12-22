package falconsTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReflexAirfoilDetector {
    public static void main(String[] args) {
        String folderPath = "C:\\Aero\\PADA task\\Airfoils dat\\coord_seligFmt";

        File folder = new File(folderPath);
        File[] datFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));

        if (datFiles != null) {
            for (File datFile : datFiles) {
                processAirfoil(datFile);
            }
        } else {
            System.out.println("No DAT files found in the specified folder.");
        }
    }

    static void processAirfoil(File datFile) {
        try {
            String airfoilName = datFile.getName();
            double[][] airfoilPoints = readSeligAirfoil(datFile.getAbsolutePath(), 10);
            
            double slopes[] = new double[10] ;
            
            for ( int i = 1 ; i < 10 ; i++ ) { 
            	slopes[i - 1] = slope(airfoilPoints[i-1][1], airfoilPoints[i][1], airfoilPoints[i-1][0], airfoilPoints[i][0]) ;
            }
            int concaveUpCount = 0 ;
            int upwardTrailingEdgeCount = 0 ;
            for ( int i = 1 ; i < 9 ; i ++ ) { 
            	if ( slopes[i] > slopes[i+1] )
            		concaveUpCount++ ;
            	if ( slopes[i] > 0 )
            		upwardTrailingEdgeCount++ ; 
            }
            if ( concaveUpCount >= 3 || upwardTrailingEdgeCount >= 3 ) { 
            	System.out.println(airfoilName ) ;
            }
            
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double slope(double y2, double y1, double x2, double x1) {
        double xdiff = (x2 - x1);
        double ydiff = y2 - y1;
        return ydiff / xdiff;
    }

    static double[][] readSeligAirfoil(String filePath, int maxPoints) throws IOException {
        double[][] points = new double[maxPoints][2];
        int currentIndex = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the first line
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null && currentIndex < maxPoints) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    points[currentIndex][0] = x;
                    points[currentIndex][1] = y;
                    currentIndex++;
                }
            }
        }

        return points;
    }
}