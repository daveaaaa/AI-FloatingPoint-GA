package ai7_rs.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author david
 */
public class DataIndividual {

    private static Random rand;
    private double[] gene;
    private int classification;
    private final static int HASH_SIGN = -1; 

    public DataIndividual(double[] gene) {
        this.gene = gene;

        if (rand == null) {
            rand = new Random();
        }
    }

    public DataIndividual(double[] gene, int classification) {
        this(gene);
        this.classification = classification;
    }

    public double[] getGene() {
        return gene;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getClassification() {
        return classification;
    }

    public DataIndividual[] crossOver(int xOverPoint, DataIndividual indiv) {
        
        DataIndividual[] children = new DataIndividual[2];

        double[] before = new double[gene.length];
        double[] after = new double[gene.length];

        for (int i = 0; i != xOverPoint; i++) {
            before[i] = gene[i];
            after[i] = indiv.getGene()[i];
        }

        for (int i = xOverPoint; i != gene.length; i++) {
            before[i] = indiv.getGene()[i];
            after[i] = gene[i];
        }

        children[0] = new DataIndividual(before,this.classification);
        children[1] = new DataIndividual(after,indiv.classification);
        
        return children;
    }

    public void mutate() {
        int mutantGene = rand.nextInt(gene.length + 1);

        if (mutantGene == gene.length) {
            if (classification == 1) {
                classification = 0;
            } else {
                classification = 1;
            }
        } else {
            ArrayList<Integer> values = new ArrayList<>();

            values.add(HASH_SIGN);
            values.add(0);
            values.add(1);

            int valueToRemove = 0;

            for (int i = 0; i != values.size(); i++) {
                int value = values.get(i);
                if (value == gene[mutantGene]) {
                    valueToRemove = i;
                }
            }

            values.remove(valueToRemove);

            gene[mutantGene] = values.get(rand.nextInt(values.size()));
        }
    }

    public boolean matchesIndividual(DataIndividual indiv) {
        boolean isMatch = true;

        for (int i = 0; i != gene.length; i++) {
            if (gene[i] != HASH_SIGN) {
                if (gene[i] != indiv.getGene()[i]) {
                    isMatch = false;
                }
            }
        }
        
        if(isMatch){
            if(this.classification != indiv.getClassification() ){
                isMatch = false; 
            }
        }

        return isMatch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder((6 * 6) + 2);
      
        for(double g : gene){
            sb.append(g);
            sb.append(" ");
        }
        
        sb.append(classification);

        return sb.toString();
    }
}
