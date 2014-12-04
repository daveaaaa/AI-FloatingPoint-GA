/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai7_rs;

import ai7_rs.GA.GA;
import ai7_rs.utils.DataIndividual;
import ai7_rs.utils.FileReader;
import ai7_rs.utils.FileWriter;
import ai7_rs.utils.HeaderResults;
import ai7_rs.utils.Result;
import java.text.DecimalFormat;

/**
 *
 * @author david
 */
public class AI7_rs {

    public static DataIndividual[] unseenSet;
    public static DataIndividual[] trainingSet;

    private static String dataFile = "data3";
    private static double trainingSetPercent;
    private static int populationSize;
    private static double mutationRate;
    private static int crossOverPoint;
    private static int competitorSize;
    private static int maxGenerations;
    private static int ruleSetSize;
    private static double crossoverPercentage;
    private static int maxRuns;
    private static boolean randomCrossover = false;
    private static double creepRate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path = "";
        String directory = "test";
        String fileName = "test1";

        initVariables();
        FileReader.generateDataSets(0.8, dataFile);
increaseRuleSetSize();
//        initVariables();
//        increaseCreep();
//        initVariables();
//        increaseCompetitorSize();
//        initVariables();
//        increaseCrossover();
//        initVariables();
//        increasePop();
//        initVariables();
//        increaseMutationRate();
//        initVariables();
//        randomCrossover();
    }

    private static void increaseCreep() {
        for (int i = 6; i != 8; i++) {
            doTest("CreepTest", "test" + i);
        }
    }

    private static void increasePop() {
        populationSize = 10;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("IncreasePop", "Test" + i);
            long endtime = System.currentTimeMillis();
            populationSize += 5;
            System.out.println("IncreasePop\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void randomCrossover() {
        randomCrossover = true;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("randomCrossover", "Test" + i);
            long endtime = System.currentTimeMillis();
            System.out.println("randomCrossover\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void increaseCrossover() {
        crossoverPercentage = 0.0;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("increaseCrossover", "Test" + i);
            long endtime = System.currentTimeMillis();
            crossoverPercentage += 0.1;
            System.out.println("Increase Crossover\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void increaseMutationRate() {
        mutationRate = 0.0;
        System.out.println("Increase Mutation Rate");
        for (int i = 0; i != 4; i++) {
            doTest("IncreaseMutationRate", "Test" + i);
            mutationRate += 0.1;
        }
    }

    private static void increaseCompetitorSize() {
        System.out.println("increase Competitor Size");
        for (int i = 0; i != 4; i++) {
            doTest("IncreaseCompetitorSize", "Test" + i);
            competitorSize += 3;
        }
    }
    
    private static void increaseRuleSetSize(){
        ruleSetSize = 2;
        System.out.println("Increase Rule Set Size");
        for(int i = 0; i != 4; i++){
            doTest("IncreaseRuleSet", "Test" + i);
            ruleSetSize += 2;
        }
    }

    private static void initVariables() {
        creepRate = 0.1;
        trainingSetPercent = 0.8;
        populationSize = 200;
        mutationRate = 0.3;
        crossOverPoint = 0;
        randomCrossover = true;
        competitorSize = 2;
        maxGenerations = 3000;
        ruleSetSize = 20;
        crossoverPercentage = 0.8;
        maxRuns = 20;
    }

    private static void doTest(String directory, String fileName) {
        String path;
        DecimalFormat df = new DecimalFormat("0.##");
        if (System.getProperty("os.name").contains("inux")) {
            path = "/home/david/Dropbox/uni/year_4/AI/assignment/AI7_floating/src/results/" + directory + "/";
        } else {
            path = "C:\\Users\\david\\Dropbox\\uni\\year_4\\AI\\assignment\\AI7_floating\\src\\results\\" + directory + "\\";
        }

        HeaderResults hr = new HeaderResults(dataFile, mutationRate, trainingSetPercent, maxGenerations, ruleSetSize, populationSize, competitorSize,creepRate,crossoverPercentage);

        System.out.println("Run: \t Time: \tUnseen Fitness");
        for (int run = 0; run != maxRuns; run++) {
            GA alg = new GA(populationSize, mutationRate, crossOverPoint, randomCrossover, competitorSize, maxGenerations, ruleSetSize, crossoverPercentage, creepRate);

            long starttime = System.currentTimeMillis();
            int intiUnseenFitness = alg.getFitness(unseenSet);
            double initPercent = (double) intiUnseenFitness / ((double) unseenSet.length / 100.0);

            Result[] results = alg.run(maxGenerations);

            int unseenFitness = alg.getFitness(unseenSet);

            double endPercent = (double) unseenFitness / ((double) unseenSet.length / 100.0);
            hr.addRunInformation(run, initPercent, endPercent);

            FileWriter.WriteFile(path, fileName, results, run, unseenFitness);

            long endtime = System.currentTimeMillis();
            System.out.println(run + "\t" + ((endtime - starttime) / 1000) + "\t" + df.format((double) unseenFitness / ((double) unseenSet.length / 100.0)));

            //Force clean up (hopefully... )
            results = null;
            alg = null;
            System.gc();
        }

        if (System.getProperty("os.name").contains("inux")) {
            FileWriter.WriteFile(path, fileName + "Header", hr);
        } else {
            FileWriter.WriteFile(path, fileName + "Header", hr);
        }

    }
}
