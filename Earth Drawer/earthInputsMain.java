package assignment;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class earthInputsMain {

    public static void main(String[] args) throws FileNotFoundException {
        Earth e = new Earth();
        Scanner options = new Scanner(System.in);
        System.out.println("Choose one of the options below:\n ");
        System.out.println("1. Percentage coordinates Above or Below an altitude \n2. Altitude at specific coordinates ");
        int option = options.nextInt();
        options.close();
        if (option ==1){
            String altitude = "";
            String choices = "";
            e.readDataArray("earth.xyz");
            while (true){
            Scanner input = new Scanner(System.in);
            System.out.println("Do you want to display the coordinates above or below(1 for above 2 for below ): ");
            try {
                choices = input.nextLine();
                int choice = Integer.parseInt(choices);
                System.out.println("Enter altitude: ");
                altitude = input.nextLine();
                double alt = Double.parseDouble(altitude);
                if (choice == 1){
                    e.percentageAbove(alt);
                }
                else if (choice == 2){
                    e.percentageBelow(alt);
                }


            }
            catch (NumberFormatException e1) {
                if (altitude.equals("quit") || choices.equals("quit")){
                    System.out.println("Bye");
                    input.close();
                    break;
                }
                else {
                    System.out.println("Invalid Altitude. PLease enter a valid altitude or \"quit\" to exit program ");
                }
            }
        }
        }
        if (option ==2){
            e.readDataMap("earth.xyz");
            while (true){
                double userLong, userLat;
                Scanner input = new Scanner(System.in);
                System.out.println("Enter longitude (0-360) and latitude (-90 - 90)");
                String line = input.nextLine();
                String[] coords = line.split(" ");
                try {
                    userLong = Double.parseDouble(coords[0]);
                    userLat = Double.parseDouble(coords[1]);
                    double alt = e.getAltitude(userLong,userLat);
                    if(alt == 15000){
                        System.out.println("Please enter valid coordinates or \"quit\" to end the program");
                    }
                    else {
                        System.out.println("The altitude at longitude " +userLong+ " latitude "+userLat+ " is "+alt+ " meters");
                    }
                }
                catch (NumberFormatException e1){
                    if (line.equals("quit")){
                        System.out.println("Bye");
                        input.close();
                        break;
                    }
                    else {
                    System.out.println("Please enter valid coordinates or \"quit\" to end the program");
                    }
                }
            }
        }
    }
}

