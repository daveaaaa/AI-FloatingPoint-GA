package ai7_rs.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author David Armstrong
 */
public class HeaderResults {

    private String datafile;
    private double mutationRate;
    private double trainingSetPercent;
    private int maxGenerations;
    private int ruleSetSize;
    private int populationSize;
    private int competitorSize;
    private ArrayList<String> runInformation;
    private double creepRate;
    private double crossoverRate;

    public HeaderResults(String datafile, double mutationRate, double trainingSetPercent, int maxGenerations, int ruleSetSize, int populationSize, int competitorSize, double creepRate, double crossoverRate) {
        this.datafile = datafile;
        this.mutationRate = mutationRate;
        this.trainingSetPercent = trainingSetPercent;
        this.maxGenerations = maxGenerations;
        this.ruleSetSize = ruleSetSize;
        this.competitorSize = competitorSize;
        this.populationSize = populationSize;
        this.creepRate = creepRate;
        this.crossoverRate = crossoverRate;
        runInformation = new ArrayList<>();
    }

    public void addRunInformation(int run, double intiPercentFitness, double finalPercentFitness) {
        runInformation.add(run + "\t" + new DecimalFormat("#.00").format(intiPercentFitness) + "\t" + new DecimalFormat("#.00").format(finalPercentFitness));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DataSet:\t").append(datafile).append("\n");
        sb.append("Mutation Rate:\t").append(mutationRate).append("\n");
        sb.append("Training Set Percent \t").append(100 * trainingSetPercent).append("%" + "\n");
        sb.append("Generation: \t").append(maxGenerations).append("\n");
        sb.append("RuleSet Size:\t").append(ruleSetSize).append("\n");
        sb.append("Population Size:\t").append(populationSize).append("\n");
        sb.append("Competitor Size:\t").append(competitorSize).append("\n");
        sb.append("Creep Rate:\t").append(creepRate).append("\n");
        sb.append("Crossover Rate:\t").append(crossoverRate).append("\n");
        sb.append("\n");

        sb.append("Run:\t Initial Fitness:\t Final Fitness: \t").append("\n");
        for (String runs : runInformation) {
            sb.append(runs).append("\n");
        }

        return sb.toString();
    }

}
