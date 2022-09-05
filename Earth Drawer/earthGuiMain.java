package assignment;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;


public class earthGuiMain{

    public static void main(String[] args) throws FileNotFoundException {
        JFrame jf = new JFrame("Map Of Earth");
        jf.getContentPane().setPreferredSize(new Dimension(750,500));
        PlotEarth pe = new PlotEarth("earth.xyz");
        double seaLevel = Double.parseDouble(args[0]); //sea level from args
        pe.shiftSeaLevel(seaLevel);
        jf.add(pe);
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
