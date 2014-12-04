/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import static ai7_rs.AI7_rs.trainingSet;
import ai7_rs.utils.DataIndividual;

/**
 *
 * @author David Armstrong
 */
public class Fitness extends Thread {

    private FitnessWorker[] fitnessWorker;
    private RuleSet[] population;
    private DataIndividual[] data;
    private int fitnessCounted;

    public static void setFitness(RuleSet rs) {

        int value = getFitness(rs, trainingSet);
        rs.setFitness(value);
        rs.setFitnessChanged(false);
    }

    public static int getFitness(RuleSet rs, DataIndividual[] data) {
        int value = 0;

        for (DataIndividual dataPoint : data) {
            for (Individual rule : rs.getRules()) {
                if (rule.isMatch(dataPoint)) {
                    if (rule.getClassification() == dataPoint.getClassification()) {
                        value++;
                    }
                    break;
                }
            }
        }

        return value;
    }

    public synchronized void getFitness_unseenData(RuleSet[] population, DataIndividual[] data) {
        fitnessCounted = 0;

        this.population = population;
        this.data = data;

        initWorkers(false);
        startWorkers();
        waitForWorkersToFinish(population);
    }

    private void waitForWorkersToFinish(RuleSet[] population) {

        while (fitnessCounted != population.length) {
            try {
                this.wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }
    }

    public synchronized void getFitness_trainingData(RuleSet[] population) {
        fitnessCounted = 0;

        this.population = population;
        this.data = trainingSet;

        initWorkers(true);
        startWorkers();
        waitForWorkersToFinish(  this.population);
     
    }

    private void initWorkers(boolean trainingSet) {
        fitnessWorker = new FitnessWorker[population.length];
        for (int i = 0; i != population.length; i++) {
            fitnessWorker[i] = new FitnessWorker(this);
            fitnessWorker[i].setRuleSet(i);
            fitnessWorker[i].setIsTrainingSet(trainingSet);
        }
    }

    private void startWorkers() {
        for (int i = 0; i != fitnessWorker.length; i++) {
            fitnessWorker[i].start();
        }
    }

    public synchronized void notifyDone() {
        fitnessCounted++;
        notify();
    }

    private class FitnessWorker extends Thread {

        private Thread thread;
        private Fitness fitness;
        private int ruleSetID;
        private boolean isTrainingSet;

        public FitnessWorker(Fitness fitness) {
            this.fitness = fitness;
            this.thread = new Thread(this);
        }

        public void setRuleSet(int ruleSetID) {
            this.ruleSetID = ruleSetID;
        }

        public void setIsTrainingSet(boolean isTrainingSet) {
            this.isTrainingSet = isTrainingSet;
        }

        @Override
        public void run() {
            if (isTrainingSet) {
                calculateFitness_trainingData();
            } else {
                calculateFitness_unseenData();
            }
            fitness.notifyDone();
        }

        private void calculateFitness_trainingData() {
            RuleSet rs = population[ruleSetID];

            if (rs.getFitnessChanged()) {
                int value = 0;
                for (DataIndividual dataPoint : trainingSet) {
                    for (Individual rule : rs.getRules()) {
                        if (rule.isMatch(dataPoint)) {
                            if (rule.getClassification() == dataPoint.getClassification()) {
                                value++;
                            }
                            break;
                        }
                    }

                }
                rs.setFitness(value);
                rs.setFitnessChanged(false);
            }

        }

        private void calculateFitness_unseenData() {
            RuleSet rs = population[ruleSetID];

            int value = 0;
            for (Individual rule : rs.getRules()) {
                for (DataIndividual dataPoint : data) {
                    if (rule.isMatch(dataPoint)) {
                        if (rule.getClassification() == dataPoint.getClassification()) {
                            value++;
                        }
                        break;
                    }
                }

            }

            rs.setFitness(value);
        }
    }
}
