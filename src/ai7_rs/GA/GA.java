/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs.GA;

import static ai7_rs.AI7_rs.unseenSet;
import ai7_rs.utils.DataIndividual;
import ai7_rs.utils.Result;

/**
 *
 * @author david
 */
public class GA {

    private Selection selection;
    private Mutation mutation;
    private CrossOver crossOver;
    private RuleSet[] population;

    public GA(int populationSize, double mutationRate, int crossoverPoint, boolean randomCrossover, int competitorSize, int maxGenerations, int ruleSetSize, double crossoverPercentage, double creepRate) {
        selection = new Selection(competitorSize);
        mutation = new Mutation(mutationRate, creepRate);
        crossOver = new CrossOver(crossoverPoint, randomCrossover, crossoverPercentage);

        population = new RuleSet[populationSize];

        for (int i = 0; i != populationSize; i++) {
            population[i] = new RuleSet(ruleSetSize, unseenSet[0].getGene().length);
        }

    }

    public int getFitness(DataIndividual[] data) {
        RuleSet bestRule = getWinner(population);
        return Fitness.getFitness(bestRule, unseenSet);
    }

    public Result[] run(int maxGenerations) {
        updateFitness();

        Result[] results = new Result[(maxGenerations / 10) + 1];

        for (int generation = 0; generation != maxGenerations; generation++) {
            if (generation % 10 == 0) {
                results[generation / 10] = new Result(generation, population);
            }

            population = selection.doSelection(population);
            population = crossOver.doCrossOver(population);
            population = mutation.doMutation(population);
            updateFitness();
        }
        results[(maxGenerations / 10)] = new Result(maxGenerations, population);
        return results;

    }

    private void updateFitness() {
        Fitness fitness = new Fitness();
        fitness.getFitness_trainingData(population);

    }

    private RuleSet getWinner(RuleSet[] competitors) {
        RuleSet bestRuleSet = null;
        int bestFitness = 0;

        for (RuleSet rule : competitors) {
            int fitness = rule.getFitness();
            if ((fitness >= bestFitness) || (bestRuleSet == null)) {
                bestFitness = fitness;
                bestRuleSet = rule;
            }
        }

        return bestRuleSet;
    }
}
