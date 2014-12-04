/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import java.text.DecimalFormat;

/**
 *
 * @author david
 */
public class Gene {

    private double lowerBound;
    private double upperBound;

    public Gene() {

    }

    public Gene(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        normaliseBounds();
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;

        normaliseBounds();
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;

        normaliseBounds();
    }

    public boolean between(double value) {
        boolean between = false;
        if ((value > lowerBound) & (value < upperBound)) {
            between = true;
        }
        return between;
    }

    public void lowerUpperBound(double creepRate) {
        this.upperBound -= creepRate;

        normaliseBounds();
    }

    public void lowerLowerBound(double creepRate) {
        this.lowerBound -= creepRate;

        normaliseBounds();
    }

    public void raiseUpperBound(double creepRate) {
        this.upperBound += creepRate;

        normaliseBounds();
    }

    public void raiseLowerBound(double creepRate) {
        this.lowerBound += creepRate;

        normaliseBounds();
    }

    public Gene createClone() {
        Gene cloneGene = new Gene(lowerBound, upperBound);
        return cloneGene;
    }

    private void normaliseBounds() {
        if(lowerBound > upperBound){
            double lowerBound = this.upperBound;
            this.upperBound = this.lowerBound;
            this.lowerBound = lowerBound;
        }
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(lowerBound) + " " + df.format(upperBound);
    }
}
