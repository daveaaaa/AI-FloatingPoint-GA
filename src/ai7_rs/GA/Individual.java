package ai7_rs.GA;

import ai7_rs.utils.DataIndividual;

/**
 *
 * @author David Armstrong
 */
public class Individual {

    private Gene[] chromosome;
    private int classification;

    public Individual() {

    }

    public Individual(Gene[] gene, int classification) {
        this.chromosome = gene;
        this.classification = classification;
    }
    
    public Individual(int chromosomeLength){
        this.chromosome = new Gene[chromosomeLength];
        for(int i =0; i != chromosomeLength; i++){
            chromosome[i] = new Gene(); 
        }
    }

    public boolean isMatch(DataIndividual data) {
        boolean isMatch = true;

        for (int i = 0; i != data.getGene().length; i++) {
            if (this.chromosome[i].between(data.getGene()[i])) {
                //do nothing
            } else {
                isMatch = false;
                break;
            }
        }

        return isMatch;
    }

    public int getClassification() {
        return classification;
    }

    public Gene[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(Gene[] gene) {
        this.chromosome = gene;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Individual createClone() {
        Gene[] cloneGene = new Gene[chromosome.length];
        int cloneClassification = classification;
        for (int i = 0; i != chromosome.length; i++) {
            cloneGene[i] = chromosome[i];
        }

        return new Individual(cloneGene, cloneClassification);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(chromosome.length + 2);
        for (int i = 0; i != chromosome.length; i++) {
            sb.append(chromosome[i].toString()).append(" ");
        }
        sb.append(" ");
        sb.append(classification);

        return sb.toString();
    }

    public int getChromosomeLength() {
        int chromosomeLength = 0;
        for (Gene gene : chromosome) {
            chromosomeLength += 2;
        }

        return chromosomeLength;
    }

    public double[] getChromosomeDouble() {
        double[] chromosomeDouble = new double[this.getChromosomeLength()];

        int i = 0;
        for (Gene gene : chromosome) {
            chromosomeDouble[i] = gene.getLowerBound();
            i++;
            chromosomeDouble[i] = gene.getUpperBound();
            i++;
        }

        return chromosomeDouble;
    }

    void setChromosomeDouble(double[] chromosomeDouble) {

        int i = 0;
        for (Gene gene : chromosome) {
            gene.setLowerBound(chromosomeDouble[i]);
            i++;
            gene.setUpperBound(chromosomeDouble[i]);
            i++;
        }

    }

}
