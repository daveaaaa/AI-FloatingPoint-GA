/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David Armstrong
 */
public class CrossOver extends Thread {

    private RuleSet[] population;
    private RuleSet[] newGeneration;
    private int crossOverPoint;
    private CrossOverWorker[] crossoverWorkers;
    private int childCount;
    private boolean randomCrossover;
    private double crossoverPercentage;
    private int crossoverNumber;
    private Random rand;

    public CrossOver(int crossOverPoint, boolean randomCrossover, double crossoverPercentage) {
        this.crossOverPoint = crossOverPoint;
        this.randomCrossover = randomCrossover;
        this.crossoverPercentage = crossoverPercentage;
        this.rand = new Random();
    }

    public synchronized RuleSet[] doCrossOver(RuleSet[] population) {
        childCount = 0;
        this.population = population;
        newGeneration = new RuleSet[population.length];

        workoutNumberForCrossingOver();
        initCrossoverWorkers(population[0].getRulesAsDouble().length);
        startCrossoverWorkers();
        waitForWorkersToFinish();
        addUncrossedOverFromPreviousGeneration();

        return newGeneration;
    }

    private void workoutNumberForCrossingOver() {
        crossoverNumber = (int) ((double) population.length * crossoverPercentage);
    }

    private void initCrossoverWorkers(int geneSize) {
        crossoverWorkers = new CrossOverWorker[crossoverNumber];

        for (int i = 0; i != crossoverNumber; i++) {
            if (randomCrossover) {
                int randomCrossoverPoint = rand.nextInt(geneSize);
                crossoverWorkers[i] = new CrossOverWorker(this, randomCrossoverPoint);
            } else {
                crossoverWorkers[i] = new CrossOverWorker(this, crossOverPoint);
            }

        }
    }

    private void waitForWorkersToFinish() {
        while (childCount != crossoverNumber) {
            try {
                wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }
    }

    private void startCrossoverWorkers() {
        for (int i = 0; i != crossoverWorkers.length; i++) {
            crossoverWorkers[i].start();
        }
    }

    private void addUncrossedOverFromPreviousGeneration() {

        for (int i = crossoverNumber; i != population.length; i++) {
            newGeneration[i] = population[rand.nextInt(population.length)].createClone();
        }
    }

    public synchronized void addChildren(ArrayList<RuleSet> children) {
        int i = 0;
        while ((i != children.size()) & (childCount < crossoverNumber)) {
            newGeneration[childCount] = children.get(i);
            childCount++;
            i++;
        }
        notify();
    }

    public synchronized int getRandomInt(int bound) {
        return rand.nextInt(bound);
    }

    private class CrossOverWorker extends Thread {

        //
        private Thread thread;
        private CrossOver crossOver;
        private ArrayList<RuleSet> children;
        private int ownCrossoverPoint;

        public CrossOverWorker(CrossOver crossOver, int crossoverPoint) {
            children = new ArrayList<>();
            ownCrossoverPoint = crossoverPoint;
            this.crossOver = crossOver;
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            createChildren();
            crossOver.addChildren(children);
        }

        private void createChildren() {
            RuleSet parent1 = population[crossOver.getRandomInt(population.length)];
            RuleSet parent2 = population[crossOver.getRandomInt(population.length)];

            getChildren(parent1, parent2);
        }

        private void getChildren(RuleSet parent1, RuleSet parent2) {
            double[] parent1Gene = parent1.getRulesAsDouble();
            double[] parent2Gene = parent2.getRulesAsDouble();

            double[] child1Gene = new double[parent1Gene.length];
            double[] child2Gene = new double[parent1Gene.length];

            for (int i = 0; i != ownCrossoverPoint; i++) {
                child1Gene[i] = parent1Gene[i];
                child2Gene[i] = parent2Gene[i];
            }

            for (int i = ownCrossoverPoint; i != parent1Gene.length; i++) {
                child1Gene[i] = parent2Gene[i];
                child2Gene[i] = parent1Gene[i];
            }

            RuleSet child1 = new RuleSet(child1Gene, parent1.getRules()[0].getChromosome().length, parent1.getRules().length);
            RuleSet child2 = new RuleSet(child2Gene, parent1.getRules()[0].getChromosome().length, parent1.getRules().length);
            child1.setFitnessChanged(true);
            child2.setFitnessChanged(true);
            children.add(child1);
            children.add(child2);

        }
    }

}
