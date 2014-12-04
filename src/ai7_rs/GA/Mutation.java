package ai7_rs.GA;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David Armstrong
 */
public class Mutation extends Thread {

    private double mutationRate;
    private Random rand;
    private ArrayList<Integer> mutantsID;
    private MutationWorker[] mutationWorker;
    private RuleSet[] population;
    private int mutantCount;
    private double creepRate;

    public Mutation(double mutationRate, double creepRate) {
        this.mutationRate = mutationRate;
        this.creepRate = creepRate;
        rand = new Random();
    }

    public synchronized RuleSet[] doMutation(RuleSet[] population) {
        mutantsID = new ArrayList<>();
        mutantCount = 0;

        this.population = population;

        getMutantsIDs();
        initMutationWorker();
        runMutationWorker();
        waitForMutationWorkersToFinish();

        return population;
    }

    private void getMutantsIDs() {

        for (int i = 0; i != population.length; i++) {
            if (mutationRate >= rand.nextDouble()) {
                mutantsID.add(i);
            }
        }
    }

    private void initMutationWorker() {
        mutationWorker = new MutationWorker[mutantsID.size()];
        for (int i = 0; i != mutantsID.size(); i++) {
            mutationWorker[i] = new MutationWorker(this);
            mutationWorker[i].setMutantID(mutantsID.get(i));
        }
    }

    private void runMutationWorker() {
        for (int i = 0; i != mutationWorker.length; i++) {
            mutationWorker[i].start();
        }
    }

    private void waitForMutationWorkersToFinish() {
        while (mutantCount != mutantsID.size()) {
            try {
                this.wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }
    }

    public synchronized void setMutant(RuleSet mutant, int mutantID) {
        population[mutantID] = mutant;
        mutantCount++;
        notify();
    }

    private synchronized int getRandomInt(int bound) {
        return rand.nextInt(bound);
    }

    private synchronized double getRandomDouble() {
        return rand.nextDouble();
    }

    private class MutationWorker extends Thread {

        private Thread thread;
        private int mutantID;
        private Mutation mutation;
//        private Random thisRand;

        public MutationWorker(Mutation mutation) {
            this.mutation = mutation;
            thread = new Thread(this);
            //  thisRand = new Random();

        }

        public void setMutantID(int mutantID) {
            this.mutantID = mutantID;
        }

        @Override
        public void run() {
                createMutant();
        }
       
        private void createMutant() {
            int classificationBitSize = 1;
            RuleSet mutantRuleSet = population[mutantID].createClone();

            int mutantIndivID = mutation.getRandomInt(mutantRuleSet.getRules().length);

            Individual mutant = population[mutantID].getRules()[mutantIndivID];

            double[] chromosome = mutant.getChromosomeDouble();

            int classfication = mutantRuleSet.getRules()[mutantIndivID].getClassification();

            int mutantGene = mutation.getRandomInt(mutant.getChromosomeLength() + classificationBitSize);

            if (mutantGene >= mutant.getChromosomeLength()) {
                classfication = changeClassification(classfication);
            } else {
                chromosome = changeGene(chromosome, mutantGene);
            }

            mutant.setClassification(classfication);
            mutant.setChromosomeDouble(chromosome);

            mutantRuleSet.getRules()[mutantIndivID] = mutant;

            mutantRuleSet.setFitnessChanged(true);
            mutation.setMutant(mutantRuleSet, mutantID);

        }

        private int changeClassification(int classfication) {
            int classification = 1;

            if (classfication == 1) {
                classification = 0;
            }

            return classification;
        }

        private double[] changeGene(double[] chromosome, int mutantGeneID) {
            double change = mutation.getRandomDouble();

            double value = chromosome[mutantGeneID];

            if (change >= 0.5) {
                value += creepRate;
                if (value > 1.0) {
                    value = 1.0;
                }
            } else {
                value -= creepRate;
                if (value < 0.0) {
                    value = 0.0;
                }
            }

            chromosome[mutantGeneID] = value;

            return chromosome;
        }
    }

}
