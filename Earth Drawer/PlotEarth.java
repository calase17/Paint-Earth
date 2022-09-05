package assignment;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.List;

public class PlotEarth extends Plot  {
    int blocksize;
    List<MapCoordinate> coordsList;
    int[] indexArray;
    TreeMap<Double, TreeMap<Double,Double>> mapOfEarth;
    PrintStream coordsFile;
    Earth e;

    public PlotEarth(String filename) throws FileNotFoundException {
        e = new Earth();

        outerLoop:
        while (true){
            Scanner input = new Scanner(System.in);
            System.out.println("Do you want to sample the coordinates (Y/N): ");
            String response = input.nextLine();
            if (response.equalsIgnoreCase("Y")){
                while (true){
                    System.out.println("Enter a resolution: ");
                    String response1 = input.nextLine();
                    try{
                        double resolution = Double.parseDouble(response1);
                        e.generateMap(resolution);
                        blocksize = 2;
                        break outerLoop;
                    }
                    catch (NumberFormatException e3){
                        System.out.println(" Please enter valid resolution");
                    }
                }
            }
            else if (response.equalsIgnoreCase("N")){
                e.readDataMap(filename);
                blocksize = 1;
                break;
            }

        }

        mapOfEarth = e.getMapOfEarth();
        setScaleX(0, 360);
        setScaleY(-90, 90);
        indexArray = new int[2];
        coordsList = new ArrayList<>();
        coordsFile = new PrintStream("CoordsFile.txt");
        addMouseListener(new MouseListener());
    }


    @Override
    public void paintComponent(Graphics g){
        this.width = getWidth();
        this.height = getHeight();

        TreeMap<Double,Integer> colorMapSea = new TreeMap<>();
        TreeMap<Double, Integer> colorMapLand = new TreeMap<>();
        int colorSea = 255;
        int colorLand = 255;

        int count1 = 0;
        for (double i =0.0 ; i >= -6999; i--){
            if(count1 ==27){
                colorSea -= 1;
                colorMapSea.put(i,colorSea);
                count1= 0;
            }
            else {
                colorMapSea.put(i, colorSea);
                count1++;
            }
        }
        int count = 0;
        for (double i=1.0; i <= 6000 ; i++){
            if (count ==24){
                colorLand -=1;
                colorMapLand.put(i,colorLand);
                count =0;
            }
            else{
                colorMapLand.put(i,colorLand);
                count++;
            }
        }
        Graphics2D g2d = (Graphics2D)g;
        for(Map.Entry<Double, TreeMap<Double, Double>> entry: mapOfEarth.entrySet()){
            double x1 = entry.getKey();
            double x = centreMap(x1);
            TreeMap<Double, Double> map;
            map = entry.getValue();
            double altitude = 0;
            double y = 0;
            for (Map.Entry<Double, Double> entry1: map.entrySet()){
                y = entry1.getKey();
                altitude = entry1.getValue();
                if (altitude > 0){
                    if (colorMapLand.get(altitude) != null){
                        int colorLand1 = colorMapLand.get(altitude);
                        g2d.setColor(new Color(0, colorLand1,0));
                    }
                    else{
                        g2d.setColor(Color.white);
                    }
                }
                else {
                    if(colorMapSea.get(altitude) != null){
                        int colorSea1 = colorMapSea.get(altitude);
                        g2d.setColor(new Color(0 ,0, colorSea1));
                    }
                    else {
                        g2d.setColor(Color.BLACK);
                    }

                }

                g.fillRect(scaleX(x), scaleY(y),blocksize,blocksize);
            }
        }
    }

    public void shiftSeaLevel(double newSeaLevel){
        for(Map.Entry<Double, TreeMap<Double, Double>> entry : mapOfEarth.entrySet()){
             double key1 = entry.getKey();
             TreeMap <Double, Double> map = entry.getValue();
             double key2 = 0;
             double alt = 0;
             for(Map.Entry<Double,Double> entry1 : map.entrySet()){
                 alt = entry1.getValue();
                 key2 = entry1.getKey();

                if(alt >0 && alt <= newSeaLevel){
                     mapOfEarth.get(key1).put(key2, 0.0);
                }
            }
        }
    }

    public void coordsToFile(){
        coordsFile.println(coordsList.get(indexArray[1]).toString());
    }


    public double centreMap(double x){
        x +=180;
        if(x >360){
            x -=360;
        }
        return x;
    }

    public double sortCoords (double x){
        x-=180;
        if(x <0){
            x+=360;
        }
        return x;
    }
    private class MouseListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent event) {
            double longitude,latitude,altitude;
            if (event.getButton() == MouseEvent.BUTTON1){
                longitude = reverseScaleX(event.getX());
                latitude = reverseScaleY(event.getY());
                longitude = sortCoords(longitude);
                altitude = e.getAltitude(longitude, latitude);
                if (altitude != 15000) {
                    int listSize = coordsList.size();
                    if (listSize == 0) {
                        coordsList.add(new MapCoordinate(e.preciseLng, e.preciseLat, altitude));
                        indexArray[1] = 0;
                        indexArray[0] = 999;
                        System.out.println("You clicked on " + coordsList.get(listSize).toString());
                        coordsToFile();
                    } else if (listSize >= 1) {
                        addToList(altitude);
                    }
                }

            }
            else if (event.getButton() == MouseEvent.BUTTON3){
                if(!coordsList.isEmpty()) {
                    System.out.println("Previously clicked " + coordsList.get(indexArray[1]).toString() + " has been deleted");
                    coordsList.remove(indexArray[1]);
                }
            }
        }

        public void addToList(double altitude) {
            MapCoordinate comparison = new MapCoordinate(e.preciseLng, e.preciseLat, altitude);
            if (coordsList.get(indexArray[1]).equals(comparison) && coordsList.get(indexArray[1]).compareTo(comparison)==0) {
                System.out.println("You have clicked on the same coordinates ");
            }
            else {
                coordsList.add(comparison);
                Collections.sort(coordsList);
                int index = coordsList.indexOf(comparison);
                indexArray[0] = indexArray[1];
                indexArray[1] = index;
                if (indexArray[1] <= indexArray[0]){
                    indexArray[0]++;
                }
                coordsToFile();
                System.out.println("You clicked on " + coordsList.get(index).toString()+"\n");
                double distance = coordsList.get(indexArray[1]).distanceTo(coordsList.get(indexArray[0]));
                System.out.println("The distance from "+ coordsList.get(indexArray[1]).toString()+ " to "+ coordsList.get(indexArray[0]).toString() + " is "+distance +" KM\n" );
            }
        }
    }
}






