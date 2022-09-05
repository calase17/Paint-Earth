package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Earth {
    private double[][] arrayOfEarth;
    public static final int MAX_DIM_X = 360;
    public static final int MAX_DIM_Y = -90;
    public List <Double> coordAbove;
    public List <Double> coordBelow;
    public double preciseLng;
    public double preciseLat;
    private TreeMap<Double, TreeMap<Double,Double >> mapOfEarth;

    public void readDataArray(String Filename) throws FileNotFoundException {
        Scanner readFile = new Scanner(new File(Filename));
        int i = 0;
        while(readFile.hasNextLine()){
            i++;
        } // while loop counts number of lines in file
        readFile.close();
        arrayOfEarth = new double[i][3];
        int lineNo = 0;
        Scanner readFile1 = new Scanner(new File(Filename));
        while (readFile1.hasNextLine() &&lineNo<i){
            String line = readFile1.nextLine();
            String[] splitValues = line.split("\\s+"); // Each line split by whitespace delimiter to get each value
            for(int x=0; x<splitValues.length;x++ ){ //for loop iterating through each element of array
                double value = Double.parseDouble(splitValues[x]);
                arrayOfEarth[lineNo][x] = value; // Value from array being stored in 2D one
            }
            lineNo++;
        }
        readFile1.close();
    }

    public TreeMap<Double, TreeMap<Double, Double>> getMapOfEarth(){
        return mapOfEarth;
    }

    public List<Double> coordinatesAbove(double altitude){
        coordAbove = new ArrayList<>();
        for(int i =0; i<arrayOfEarth.length;i++){
            for (int x=0;x<arrayOfEarth[i].length -1;x++){   //This for loop iterates through longitude and latitude only
                if (altitude< arrayOfEarth[i][2]){  //if statement where altitude from user is compared against every altitude in array
                    coordAbove.add(arrayOfEarth[i][x]); // altitudes larger than user's altitude is added to the list
                }
            }
        }
        return coordAbove;
    }

    public List<Double> coordinatesBelow(double altitude){ //opposite of method above
        coordBelow = new ArrayList<>();
        for(int i =0; i<arrayOfEarth.length;i++){
            for (int x=0;x<arrayOfEarth[i].length -1;x++){
                if (altitude> arrayOfEarth[i][2]){
                    coordBelow.add(arrayOfEarth[i][x]);
                }
            }
        }
        return coordBelow;
    }

    public void percentageAbove(double altitude){
        coordAbove = coordinatesAbove(altitude);
        double x = coordAbove.size()/2; // number of elements in list
        double percentage = (x/arrayOfEarth.length) *100; //percentage formula
        double percentageRound = Math.round(percentage *10.0) / 10.0; //rounds to two decimal places
        System.out.println("Proportion of coordinates above " +altitude +" meters: " + percentageRound +"%.");
    }

    public void percentageBelow(double altitude){
        coordBelow = coordinatesBelow(altitude);
        double x =coordBelow.size()/2;
        double percentage =  (x/arrayOfEarth.length) *100;
        double percentageRound = Math.round(percentage *10.0) /10.0;
        System.out.println("Proportion of coordinates below " +altitude+ " meters: "+ percentageRound+"%.");
    }

    public void readDataMap(String filename) throws FileNotFoundException{
        mapOfEarth = new TreeMap<>();
        Scanner read = new Scanner(new File(filename));
        double longitude, lat, altitude;
        while (read.hasNextLine()) {
            String line = read.nextLine();
            String[] split = line.split("\\s+");
            longitude = Double.parseDouble(split[0]);
            lat = Double.parseDouble(split[1]);
            altitude = Double.parseDouble(split[2]);

            if (mapOfEarth.get(longitude) == null){  //adds key and value for first time appearance
                mapOfEarth.put(longitude, new TreeMap<>());// map initialised as value for each key of outer map
            }
            mapOfEarth.get(longitude).put(lat, altitude);//inner map key and value are added
        }
        read.close();

    }

    public void generateMap(double resolution){
        mapOfEarth = new TreeMap<>();
        Random random = new Random();
        for (double x=0.0; x<=MAX_DIM_X;x+=resolution){ // loop goes through full range of coordinates at specified sample rate provided by resolution parameter
            for (double y=90; y>=MAX_DIM_Y; y-=resolution){
                double randomAltitude = random.nextInt(12999) - 6999;// random altitude generated
                if (mapOfEarth.get(x) == null){
                    mapOfEarth.put(x, new TreeMap<>());
                }
                mapOfEarth.get(x).put(y,randomAltitude);
            }
        }
    }

    public double getAltitude(double longitude, double latitude){
        double altitude = 15000;
        try {
            if(mapOfEarth.get(longitude) != null){
                altitude = mapOfEarth.get(longitude).get(latitude);//gets altitude
            }

            else if (mapOfEarth.floorKey(longitude) != null) {
                preciseLng = mapOfEarth.floorKey(longitude); //floorkey method gets closest key to to the value of the parameter
                preciseLat = mapOfEarth.get(preciseLng).floorKey(latitude);
                altitude = mapOfEarth.get(preciseLng).get(preciseLat);
            }
        }
        catch (NullPointerException e3){
            System.out.println("No values have been found from coordinates received. ");
            return altitude;
        }
        return altitude;

    }
}

