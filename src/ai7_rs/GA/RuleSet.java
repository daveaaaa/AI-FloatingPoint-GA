/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import ai7_rs.utils.IndividualFactory;

/**
 *
 * @author David Armstrong
 */
public class RuleSet {

    private Individual[] rules;
    private int fitness;
    private boolean fitnessChanged;

    public RuleSet() {

    }

    public RuleSet(Individual[] rules) {
        this.rules = rules;
        fitness = 0;
        fitnessChanged = true;
    }

    public RuleSet(double[] newRules, int geneLength, int ruleCount) {
        this.rules = new Individual[ruleCount];
        int i = 0;
        int ruleSetCount = 0;
        fitness = 0; 
        fitnessChanged = true;

        while (i != newRules.length) {
            double[] newGene = new double[geneLength * 2];

            for (int j = 0; j != (geneLength * 2); j++) {
                newGene[j] = newRules[i];
                i++;
            }
            Individual indiv = new Individual(geneLength);
            indiv.setChromosomeDouble(newGene);
            indiv.setClassification((int) newRules[i]);
            rules[ruleSetCount] = indiv;
            ruleSetCount++;
            i++;
        }
    }

    public RuleSet(int ruleSetSize, int geneSize) {
        fitnessChanged = true; 
        rules = new Individual[ruleSetSize];

        for (int i = 0; i != ruleSetSize; i++) {
            rules[i] = IndividualFactory.GenerateNewIndividual(geneSize);
        }
        fitness = 0;
    }

    public void setRules(Individual[] rules) {
        this.rules = rules;
        fitnessChanged = true;
    }

    public Individual[] getRules() {
        return rules;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public boolean getFitnessChanged() {
        return fitnessChanged;
    }

    public void setFitnessChanged(boolean fitnessChanged) {
        this.fitnessChanged = fitnessChanged;
    }

    public int getFitness() {
        return this.fitness;
    }

    public RuleSet createClone() {
        RuleSet clonedRuleSet = new RuleSet();
        Individual[] clonedRules = new Individual[rules.length];

        for (int i = 0; i != rules.length; i++) {
            clonedRules[i] = rules[i].createClone();
        }

        clonedRuleSet.setRules(clonedRules);
        clonedRuleSet.setFitness(fitness);
        clonedRuleSet.setFitnessChanged(false);
        return clonedRuleSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(rules.length * rules[0].getChromosome().length);

        for (Individual rule : rules) {
            sb.append(rule.toString());
            sb.append(" ");
        }

        return sb.toString();
    }

    public int getChromosomeLength() {
        int chromosomeLength = 0;
        for (Individual indiv : rules) {
            chromosomeLength += indiv.getChromosomeLength();
        }

        return chromosomeLength;

    }

    public double[] getRulesAsDouble() {
        double[] rulesAsDouble = new double[rules.length + (rules.length * rules[0].getChromosome().length * 2)];

        int i = 0;
        for (Individual rule : rules) {
            for (int j = 0; j != rule.getChromosome().length * 2; j++) {
                rulesAsDouble[i] = rule.getChromosomeDouble()[j];
                i++;
            }
            rulesAsDouble[i] = rule.getClassification();
            i++;
        }

        return rulesAsDouble;
    }

}
