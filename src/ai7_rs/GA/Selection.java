/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import java.util.Random;

/**
 *
 * @author David Armstrong
 */
public class Selection extends Thread {

    private SelectionWorker[] selectionWorkers = null;
    private RuleSet[] rules;
    private RuleSet[] winners;
    private int tournamentSize;
    private int winnerCount;
    private Random rand;

    public Selection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
        rand = new Random();
    }

    public synchronized RuleSet[] doSelection(RuleSet[] rules) {
        winnerCount = 0;

        this.rules = rules;
        winners = new RuleSet[rules.length];

        initTournament();
        startSelectionWorkers();
        waitForWorkersToFinish();

        return winners;
    }

    private void waitForWorkersToFinish() {
        while (winnerCount != rules.length) {
            try {
                this.wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }
    }

    private void initTournament() {
        selectionWorkers = new SelectionWorker[rules.length];

        for (int i = 0; i != rules.length; i++) {
            selectionWorkers[i] = new SelectionWorker(this);
        }
    }

    private void startSelectionWorkers() {
        for (int i = 0; i != selectionWorkers.length; i++) {
            selectionWorkers[i].start();
        }
    }

    public synchronized void putWinner(int winnerPosition) {
        winners[winnerCount] = rules[winnerPosition].createClone();
        winnerCount++;
        notify();
    }

    public synchronized int getRandomInt(int bound) {
        return rand.nextInt(bound);
    }

    private class SelectionWorker extends Thread {

        private int[] competitors = new int[tournamentSize];
        private int winner;
        private Selection selection;
        private Thread thread;

        public SelectionWorker(Selection selection) {
            this.selection = selection;
            thread = new Thread(this);

        }

        @Override()
        public void run() {
            getCompetitors();
            findWinner();
            selection.putWinner(winner);
        }

        private void getCompetitors() {
            for (int i = 0; i != tournamentSize; i++) {
                competitors[i] = selection.getRandomInt(rules.length);
            }
        }

        private void findWinner() {
            int bestFitness = 0;
            winner = 0;

            for (int i = 0; i != competitors.length; i++) {
                if (rules[competitors[i]].getFitness() >= bestFitness) {
                    bestFitness = rules[competitors[i]].getFitness();
                    winner = competitors[i];
                }
            }
        }
    }
}
