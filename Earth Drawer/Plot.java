package assignment;

import javax.swing.*;

public class Plot extends JComponent {

    int width = 500, height = 500;

    double xmin=0, xmax=1, ymin=0, ymax=1;

    public int scaleX(double x){
        return (int)  (width * (x - xmin) / (xmax - xmin));
    }

    public int scaleY(double y) {
        return (int) (height * (ymin - y) / (ymax - ymin) + height);
    }

    public double reverseScaleX(double scaledX) {
        return ((scaledX / width) * xmax);
    } // These two methods reverse  the scale methods to get initial coordinate values

    public double reverseScaleY(double scaledY){
        return (-(((scaledY-height) / height) * (ymax-ymin)-ymin));}

    public void setScaleX(double min, double max) {
        xmin = min; xmax = max; }
    public void setScaleY(double min, double max) {
        ymin = min; ymax = max; }
}

