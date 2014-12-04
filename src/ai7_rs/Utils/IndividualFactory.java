/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.utils;

import ai7_rs.GA.Gene;
import ai7_rs.GA.Individual;

/**
 *
 * @author david
 */
public class IndividualFactory {

    public static Individual GenerateNewIndividual(int geneLength) {
        Gene[] newGene = new Gene[geneLength];

        for (int i = 0; i != newGene.length; i++) {
            newGene[i] = new Gene();
        }

        int value = 0;
        for (int i = 0; i != geneLength; i++) {

            newGene[i].setUpperBound(Math.random());
            newGene[i].setLowerBound(Math.random());
        }

        if (Math.random() > 0.5) {
            value = 1;
        }

        return new Individual(newGene, value);
    }

}
