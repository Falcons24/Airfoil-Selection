
package falconsTools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class airfoilDataReader {
    public static void main(String[] args) {
        //String folderPath = "C:\\Aero\\Airfoil data\\prelim\\" ;
        String folderPath ="C:\\Aero\\New Folder" ;//Specify the folder path"
        File folder = new File(folderPath) ;
        File[] files = folder.listFiles() ;
        System.out.println("Initialising") ;
        String line ;
        String csvSplitBy = "," ;
        String[][] data ;
        double[][] numberData ;
        final String csvfilePath = "C:\\aircraft desgn\\Fuselage Airfoils.csv";
        String [] header = { "Airfoil Name", "Stall angle", "2D Clmax", "Stall AoA +3", "CL after stall", "Stall gentleness", "AoA @Cdmin", "Cdmin",
        		"CL @Cdmin", "AoA1", "CL1", "AoA2", "CL2", "CL slope 1", "AoA3", "CL3", "AoA4", "CL4", "CL slope 2", "AoA5", "CL5", 
        		"AoA6", "CL6", "CL slope3", "Avg Lift slope", "AoA @CL=0 ( from xflr)", "AoA range", "CL/Cdmax", "AoA endurance",
        		"CL^1.5/Cd", "Cm avg", "AoA1", "Cm1", "AoA2", "Cm2", "Cm slope1", "AoA3", "Cm3", "AoA4", "Cm4", "Cm slope2**", "AoA5", "Cm5", "Cmslope3**"
        		, "AoA6", "Cm6", "Cmslope4", "AoA7", "Cm7", "Cmslope4", "AoA8", "Cm8", "Cmslope5", "Avg Cm slope", "Clmax @Cm=0", "AoA @Cm=0"} ;
        appendLineToFile(csvfilePath, header ) ;
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                // Count the number of rows and columns in the CSV file
                int rowCount = 0 ;
                int colCount = 0 ;
                int ZeroMomentCounter = 0 ;
                int numberRowIndex = -1 ; 
                String airfoilName = "" ;
                double Clmax = Double.MIN_VALUE ;
                double alpha_Clmax = 0.0 ;
                double Cdmin = Double.MAX_VALUE ;
                double CL_Cdmin = 0.0 ;
                double alpha_Cdmin = 0.0 ;
                double Cl_0 = 0.0 ;
                double Cl_2 = 0.0 ;
                double Cl_3 = 0.0 ;
                double Cl_7 = 0.0 ;
                double Cl_0_5 = 0.0 ;                
                double Cl_5_5 = 0.0 ;
                double slope_Cl_05_55 = 0.0 ;
                double slope_Cl_3_7 = 0.0 ;
                double slope_Cl_0_2 = 0.0 ;
                double Cl_Cdmax = Double.MIN_VALUE ;
                double alpha_Cl_Cdmax = 0.0 ;
                double Cl_3_2_Cdmax = Double.MIN_VALUE ;
                double alpha_Cl_3_2_Cdmax = 0.0 ;
                double Cm_0_5 = 0.0 ;
                double Cm_5_5 = 0.0 ;
                double slope_Cm = 0.0 ;
                double Cm_avg = 0.0 ; 
                double CL_avg_aroundstall = 0.0 ;
                double CL_3 = 0.0 ;
                double alpha_CL_3 = 0.0 ;
                double CL_avg = 0.0 ;
                double Cm_0 = 0.0 ; 
                double Cm_3 = 0.0 ; 
                double Cm_5 = 0.0 ; 
                double Cm_7 = 0.0 ; 
                double Cm_10 = 0.0 ; 
                double Cm_13 = 0.0 ; 
                double Cm_0_AoA[] = new double[20] ; 
                double Cm_0_AoA_CLmax = -999 ; 
                double Cm_0_CLmax = -999 ;
                double Cm2_0_AoA_CLmax = -999 ;  
                double Cm2_0_CLmax = -999 ;
                double AoA_0_5 = 0 ;
                double AoA_5_5 = 0 ;
                double AoA_3 = 0 ;
                double AoA_7= 0 ;
                double AoA_0 = 0 ;
                double AoA_2= 0 ;
                double AoA_5 = 0 ;
                double AoA_10= 0 ;
                double AoA_13 = 0 ;
                double AoA_Cm_signum ;
                
                
                
                
                while (( line = br.readLine()) != null ) {
                    if (rowCount == 2) {
                        airfoilName = line ;
                    }

                    String[] row = line.split(csvSplitBy);
                    if (numberRowIndex == -1 && row.length > 0 && isNumeric(row[0]) && isInRange(Double.parseDouble(row[0]), -18.1, 25)) {
                        numberRowIndex = rowCount ;
                    }
                    rowCount++;
                    colCount = Math.max( colCount, row.length ) ;
                }

                // Initialize the 2D arrays with the appropriate size
                data = new String[rowCount][colCount];
                if (numberRowIndex != -1) {
                    numberData = new double[rowCount - numberRowIndex][colCount + 2]; // Increase colCount by 2 for the extra columns
                } else {
                    numberData = new double[0][0];
                }

                // Reset the buffered reader
                
                
           
                if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                    try (BufferedReader br2 = new BufferedReader(new FileReader(file))) {


                // Read the CSV file again and populate the 2D arrays
                int rowIndex = 0;
                int numberDataIndex = 0;
                while ((line = br2.readLine()) != null) {
                    String[] row = line.split(csvSplitBy);
                    for (int colIndex = 0; colIndex < row.length; colIndex++) {
                        data[rowIndex][colIndex] = row[colIndex];
                        if (rowIndex >= numberRowIndex && !row[colIndex].isEmpty()) {
                            numberData[numberDataIndex][colIndex] = Double.parseDouble(row[colIndex]);
                        }
                    }

                    // Calculate values for the two extra columns
                    if (rowIndex >= numberRowIndex) {
                        if (!Double.isNaN(numberData[numberDataIndex][1]) && !Double.isNaN(numberData[numberDataIndex][2])) {
                            double value1 = numberData[numberDataIndex][1] / numberData[numberDataIndex][2];
                            double value2 = Math.pow(numberData[numberDataIndex][1], 3) / Math.pow(numberData[numberDataIndex][2], 2);
                            numberData[numberDataIndex][colCount] = value1;
                            numberData[numberDataIndex][colCount + 1] = value2;
                        } else {
                            numberData[numberDataIndex][colCount] = Double.NaN; // Store NaN for missing or non-numeric values
                            numberData[numberDataIndex][colCount + 1] = Double.NaN; // Store NaN for missing or non-numeric values
                        }
                        numberDataIndex++;
                    }
                    rowIndex++;
                }

                // Delete rows with 0 in column index 2 from numberData
                double[][] filteredNumberData = filterRowsWithZero(numberData, 2);
                if (filteredNumberData.length > 0) {
                
                // Declare the variables
                double alpha_CL_0 = 0.0 ;
                double CL_alpha_0 = 0.0 ;

                // Perform numerical calculations on filteredNumberData
                for (int i = 0; i < filteredNumberData.length; i++) {
                    // Clmax
                    Clmax = Math.max(Clmax, filteredNumberData[i][1]);
                    // alpha_Clmax
                    if (filteredNumberData[i][1] == Clmax) {
                        alpha_Clmax = filteredNumberData[i][0];
                    }
                    // Cdmin
                    Cdmin = Math.min(Cdmin, filteredNumberData[i][2]);
                    // CL_Cdmin
                    if (filteredNumberData[i][2] == Cdmin) {
                        CL_Cdmin = filteredNumberData[i][1];
                        alpha_Cdmin = filteredNumberData[i][0];
                    }
                    // Cl(0.5)
                    if (filteredNumberData[i][0] >= 0.5 && Cl_0_5 == 0) {
                        Cl_0_5 = filteredNumberData[i][1];
                        AoA_0_5 = filteredNumberData[i][0] ; 
                    }

                    if (filteredNumberData[i][0] >= 0 && Cl_0 == 0 ) {
                        Cl_0 = filteredNumberData[i][1];  
                        AoA_0 = filteredNumberData[i][0] ;
                        
                    }
                    if (filteredNumberData[i][0] >= 2 && Cl_2 == 0) {
                        Cl_2 = filteredNumberData[i][1];
                        AoA_2 = filteredNumberData[i][0] ;
                        
                    }
                    if (filteredNumberData[i][0] >= 3 &&  Cl_3 == 0) {
                        Cl_3 = filteredNumberData[i][1];
                        AoA_3 = filteredNumberData[i][0] ;
                    }
                    if (filteredNumberData[i][0] >= 7 && Cl_7 == 0) {
                        Cl_7 = filteredNumberData[i][1];
                        AoA_7 = filteredNumberData[i][0] ;
                    }
                    // Cl(5.5)
                    if (filteredNumberData[i][0] >= 5.5 && Cl_5_5 == 0) {
                        Cl_5_5 = filteredNumberData[i][1];
                        AoA_5_5 = filteredNumberData[i][0] ;
                    }
                    // Cl/Cdmax
                    Cl_Cdmax = Math.max(Cl_Cdmax, filteredNumberData[i][colCount]);
                    // alpha_Cl/Cdmax
                    if (filteredNumberData[i][colCount] == Cl_Cdmax) {
                        alpha_Cl_Cdmax = filteredNumberData[i][0];
                    }
                    // Cl^3/2/Cdmax
                    Cl_3_2_Cdmax = Math.max(Cl_3_2_Cdmax, filteredNumberData[i][colCount + 1]);
                    // alpha_Cl^3/2/Cdmax
                    if (filteredNumberData[i][colCount + 1] == Cl_3_2_Cdmax) {
                        alpha_Cl_3_2_Cdmax = filteredNumberData[i][0];
                    }
                    // Cm(0.5)
                    if (filteredNumberData[i][0] >= 0.5 && Cm_0_5 == 0) {
                        Cm_0_5 = filteredNumberData[i][4];
                    }
                    // Cm(5.5)
                    if (filteredNumberData[i][0] >= 5.5 && Cm_5_5 == 0) {
                        Cm_5_5 = filteredNumberData[i][4];
                    }
                    // Cm_avg
                    Cm_avg += filteredNumberData[i][4];
                    // alpha_CL_0                                        
                    if (filteredNumberData[i][1] == 0) {
                        alpha_CL_0 = filteredNumberData[i][0];
                    }
                    // CL_alpha_0
                    if (filteredNumberData[i][0] >= 0 && Cm_0 == 0) {
                        Cm_0 = filteredNumberData[i][4];
                        
                    }
                    if (filteredNumberData[i][0] >= 3 && Cm_3 == 0) {
                        Cm_3 = filteredNumberData[i][4];
                    }
                    if (filteredNumberData[i][0] >= 5 && Cm_5 == 0) {
                        Cm_5 = filteredNumberData[i][4];
                        AoA_5 = filteredNumberData[i][0];
                    }
                    if (filteredNumberData[i][0] >= 7 && Cm_7 == 0) {
                        Cm_7 = filteredNumberData[i][4];
                    }
                    if (filteredNumberData[i][0] >= 10 && Cm_10 == 0) {
                        Cm_10 = filteredNumberData[i][4];
                        AoA_10 = filteredNumberData[i][0];
                    }
                    if (filteredNumberData[i][0] >= 13 && Cm_13 == 0 ) {
                        Cm_13 = filteredNumberData[i][4];
                        AoA_13 = filteredNumberData[i][0];
                    }
                    if ( filteredNumberData[i][4] < 0.02 && filteredNumberData[i][4] > -0.02 && ZeroMomentCounter < 20 ) {
                    	Cm_0_AoA[ZeroMomentCounter++] = filteredNumberData[i][0] ;                        	
                    	if ( filteredNumberData[i][1] > Cm_0_CLmax ) {
                    		Cm2_0_AoA_CLmax = filteredNumberData[i][0] ; 
                    		Cm2_0_CLmax = filteredNumberData[i][1] ;
                    	}
                    }
                    if (i < filteredNumberData.length - 1 && filteredNumberData[i][4]*filteredNumberData[i+1][4] < 0  && ZeroMomentCounter < 20 ) {
                    	Cm_0_AoA[ZeroMomentCounter++] = filteredNumberData[i][0] ;                        	
                    	if ( filteredNumberData[i][1] > Cm_0_CLmax ) {
                    		Cm_0_AoA_CLmax = filteredNumberData[i][0] ; 
                    		Cm_0_CLmax = filteredNumberData[i][1] ;
                    	}
                    }                                                           	                    
                }
                int c = 0;
                for (int i = 0; i < filteredNumberData.length; i++) { 
                	if (filteredNumberData[i][0] < 0 )
                		continue ; 
                	if (filteredNumberData[i][0] > alpha_Clmax - 2 )                	
                		break ;
                	c++ ;
                	CL_avg += filteredNumberData[i][1] ;                	
                	
                }      
                CL_avg /= c ;

                // Calculate slope_Cl
                slope_Cl_05_55 = (Cl_5_5 - Cl_0_5) / 5.0;
                slope_Cl_3_7 = (Cl_7 - Cl_3) / 4.0;
                slope_Cl_0_2 = (Cl_2 - Cl_0) / 2.0 ;
                // Calculate slope_Cm
                slope_Cm = (Cm_5_5 - Cm_0_5) / 5.0;
                // Calculate Cm_avg
                Cm_avg /= filteredNumberData.length;
                
                // Calculate CL_avg_aroundstall
                
             // Find the row index of alpha_CLmax
                int row = -1;
                for (int i = 0; i < filteredNumberData.length; i++) {
                    if (filteredNumberData[i][0] == alpha_Clmax) {
                        row = i;
                        break;
                    }
                }

                if (row != -1) {
                    // Calculate CL_avg_aroundstall
                  
                    int rowOffset = 6;
                    while ((row + rowOffset) >= filteredNumberData.length) {
                        rowOffset--;
                    }
                    row += rowOffset;
                    CL_3 = filteredNumberData[row][1];
                    alpha_CL_3 = filteredNumberData[row][0];
                    CL_avg_aroundstall = (Clmax - CL_3) / (alpha_Clmax - alpha_CL_3);

                    // Print the calculated variables
                    /*System.out.println("Clmax: " + Clmax);
                    System.out.println("alpha_Clmax: " + alpha_Clmax);
                    System.out.println("Cdmin: " + Cdmin);
                    System.out.println("CL_Cdmin: " + CL_Cdmin);
                    System.out.println("alpha_Cdmin: " + alpha_Cdmin);
                    System.out.println("Cl(0.5): " + Cl_0_5);
                    System.out.println("Cl(5.5): " + Cl_5_5);
                    System.out.println("slope_Cl: " + slope_Cl);
                    System.out.println("Cl/Cdmax: " + Cl_Cdmax);
                    System.out.println("alpha_Cl/Cdmax: " + alpha_Cl_Cdmax);
                    System.out.println("Cl^3/2/Cdmax: " + Cl_3_2_Cdmax);
                    System.out.println("alpha_Cl^3/2/Cdmax: " + alpha_Cl_3_2_Cdmax);
                    System.out.println("Cm(0.5): " + Cm_0_5);
                    System.out.println("Cm(5.5): " + Cm_5_5);
                    System.out.println("slope_Cm: " + slope_Cm);
                    System.out.println("Cm_avg: " + Cm_avg);
                    System.out.println("alpha_CL_0: " + alpha_CL_0);
                    System.out.println("CL_alpha_0: " + CL_alpha_0);
                    System.out.println("CL_avg_aroundstall: " + CL_avg_aroundstall);
                    */
                    if ( alpha_CL_0 == 0 ) { 
                    	int pos_min_difference = 0 ;
                    	for ( int i = 0 ; i < filteredNumberData.length ;i++ ) { 
                    		if ( Math.abs(filteredNumberData[i][1]) <  Math.abs(filteredNumberData[ pos_min_difference][1]) ) { 
                    			pos_min_difference = i ;
                    		}
                    	}
                    	alpha_CL_0 = filteredNumberData[pos_min_difference][0] ;
                    }
                    
                    
                    String[] airfoildat = { airfoilName.substring(22)," ", "" + String.valueOf(alpha_Clmax), "" + String.valueOf(Clmax), "" + String.valueOf(alpha_CL_3), 
                    		String.valueOf(CL_3), String.valueOf(CL_avg_aroundstall), "" + String.valueOf(alpha_Cdmin), String.valueOf(Cdmin), String.valueOf(CL_Cdmin), 
                    		String.valueOf(AoA_5_5),String.valueOf(Cl_5_5),String.valueOf(AoA_0_5), String.valueOf(Cl_0_5),"",String.valueOf(AoA_2),String.valueOf(Cl_2),
                    		String.valueOf(AoA_0), String.valueOf(Cl_0),"",String.valueOf(AoA_7), String.valueOf(Cl_7), String.valueOf(AoA_3),String.valueOf(Cl_3),"","", 
                    		String.valueOf(alpha_CL_0), String.valueOf(alpha_Cl_Cdmax),String.valueOf(Cl_Cdmax), "" + String.valueOf(alpha_Cl_3_2_Cdmax),
                    		String.valueOf(Math.sqrt(Cl_3_2_Cdmax)), String.valueOf(Cm_avg), String.valueOf(AoA_5_5),String.valueOf(Cm_5_5),
                            String.valueOf(AoA_0_5) ,String.valueOf(Cm_0_5),"",String.valueOf(AoA_0), String.valueOf(Cm_0),String.valueOf(AoA_3), String.valueOf(Cm_3), "",
                            String.valueOf(AoA_5),String.valueOf(Cm_5),"",String.valueOf(AoA_7), String.valueOf(Cm_7),"", String.valueOf(AoA_10),String.valueOf(Cm_10),"",
                            String.valueOf(AoA_13 ),String.valueOf(Cm_13),"","", String.valueOf(Cm_0_CLmax),String.valueOf(Cm_0_AoA_CLmax ),String.valueOf(Cm2_0_CLmax),String.valueOf(Cm2_0_AoA_CLmax ),""} ;
                    appendLineToFile(csvfilePath, airfoildat ) ;
                    
                    System.out.print(airfoilName.substring(22) + ",") ;
                    System.out.print("CL avg : " + CL_avg + ",") ;
                    System.out.print("Cl/Cdmax: " + Cl_Cdmax+ ",") ;
                    System.out.print("Cl^3/2/Cdmax: " + Cl_3_2_Cdmax+ ",") ;
                    System.out.println() ;
                    
                    
                    System.out.print(airfoilName + ",");
                    System.out.print(alpha_Clmax + ",");
                    System.out.print(Clmax + ",");
                    System.out.print(alpha_CL_3 + ",") ;
                    System.out.print(CL_3 + ",") ;
                    System.out.print(CL_avg_aroundstall + ",");
                    System.out.print(alpha_Cdmin + ",");
                    System.out.print(Cdmin + ",");
                    System.out.print(CL_Cdmin + ",");
                    System.out.print(Cl_5_5 + ",");
                    System.out.print(Cl_0_5 + ",");
                    System.out.print(alpha_CL_0 + ",");
                    System.out.print(CL_alpha_0 + ",");
                    System.out.print(alpha_Cl_Cdmax + ",");
                    System.out.print(Cl_Cdmax + ",");
                    System.out.print(alpha_Cl_3_2_Cdmax + ",");
                    System.out.print(Cl_3_2_Cdmax + ",");
                    System.out.print(Cm_5_5 + ",");
                    System.out.print(Cm_0_5 + ",");
                    System.out.print(slope_Cm + ",");
                    System.out.print(Cm_avg);
                    System.out.print(Cm_0);
                    System.out.print(Cm_3);
                    System.out.print(Cm_5);
                    System.out.print(Cm_7);
                    System.out.print(Cm_10);
                    System.out.print(Cm_13 + " " );
                    System.out.print(Cm_0_CLmax + " " );
                    System.out.print( Cm_0_AoA_CLmax );
                    
                    
                    System.out.println();
                    
                }
                } else System.out.println(airfoilName) ;

            } catch (IOException e) {
                e.printStackTrace(); }
              catch ( Exception e ) { 
            	  String s[] = { airfoilName.substring(22) } ;
            	  
            	  appendLineToFile(csvfilePath,s ) ;
            	  System.out.println("Error encountered for " +  airfoilName.substring(22)) ;
              }
                    finally { System.out.println("Moving to next airfoil");	}
                }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR!!");}

                finally {System.out.println("ERROR!!"); }
            }}
    }     
    public static void deleteAndMoveRows(String inputFilePath, String outputFilePath, int rowToDelete) throws IOException {
        List<String[]> rowsToKeep = new ArrayList<>( );

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                currentRow++;

                // Skip the 9th row
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
            
    private static void appendLineToFile(String filePath, String[] line) {
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
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // Helper method to check if a value is within a range
    private static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    // Helper method to filter rows with a specific value in a column
    
    private static double[][] filterRowsWithZero(double[][] array, int column) {
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
    static String fixString ( String s ) { 
    	String news = "" ;
    	for ( int i = 0 ; i < s.length(); i++) { 
    		news = news + s.charAt(i) ;
    		if ( s.charAt(i) == '\\' ) 
    			news += '\\' ;    				
    	}
    	return news;
    }
 }