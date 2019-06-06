/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmos.ag;

import DiccionarioDatos.*;
import java.util.ArrayList;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;

public class ConfiguracionTSP {

    private ArrayList<Coordenada> problema;
    private BruteForce br=new BruteForce();
    private Genotype genotype;
    private Configuration configuration;
    private FitnessFunction aptitud;
    private int CITIES;    

    private int popSize; 
    private int numGen = 300;
    private double cullingPercentage = .75;
    private int mutationRate = 3;
    
    public int getPopSize() {return popSize;}
    public void setPopSize(int popSize) {this.popSize = popSize;}
    
    public int getNumGen() {return numGen;}
    public void setNumGen(int numGen) {this.numGen = numGen;}
    
    public double getCullingPercentage() {return cullingPercentage;}
    public void setCullingPercentage(double cullingPercentage) {this.cullingPercentage = cullingPercentage;}
    
    public int getMutationRate() {return mutationRate;}
    public void setMutationRate(int mutationRate) {this.mutationRate = mutationRate;}
    
     public ConfiguracionTSP(){
     }
    
    public void makeConfiguration(ArrayList<Coordenada> p,BruteForce br, String tipo) {
        this.br = br;
        problema = p;
        CITIES = problema.size();
        configuration = new Configuration();
        configuration.reset();
        popSize = (1 * (int) ((Math.log(1 - Math.pow(.99,
                (1.0 / CITIES)))) / (Math.log(((float) (CITIES - 3)
                / (float) (CITIES - 1))))));
//        popSize =CITIES;
        try {
            BestChromosomesSelector bestChromsSelector =
                    new BestChromosomesSelector(configuration, cullingPercentage);
            bestChromsSelector.setDoubletteChromosomesAllowed(true);
            configuration.addNaturalSelector(bestChromsSelector, true);
            configuration.setRandomGenerator(new StockRandomGenerator());
            configuration.setMinimumPopSizePercent(100);
            configuration.setEventManager(new EventManager());
            // Object that declares a fitness value better if it is lower.
            configuration.setFitnessEvaluator(new OppositeFitnessEvaluator());
            // Used to preserve memory with the chromosome allocations
            configuration.setFitnessFunction(new AptitudTSP(problema,br));
            configuration.setChromosomePool(new ChromosomePool());
            configuration.setPreservFittestIndividual(true);
            if (!tipo.equals("3")) {
                configuration.removeNaturalSelectors(true);
                if (tipo.equals("1")) {
                    configuration.addNaturalSelector(new TournamentSelector(configuration, popSize, 0.1), true);
                } else {
                    configuration.addNaturalSelector(new WeightedRouletteSelector(configuration), true);
                }
            }

            configuration.addGeneticOperator(new TravelingSalesmanHeuristicCrossover(configuration, p,br));
            configuration.addGeneticOperator(new SegmentSwappingMutation(configuration, mutationRate, p,br));

            IChromosome sampleChromosome = createSampleChromosome();
            configuration.setSampleChromosome(sampleChromosome);
            configuration.setPopulationSize(popSize);

        } catch (InvalidConfigurationException ex) {
            System.out.println("Error" + ex);
            System.exit(0);
        }

    }

    public int getPopulationSize() {
        return popSize;
    }

    public ArrayList<Coordenada> getProblema() {
        return problema;
    }

    public void setProblema(ArrayList<Coordenada> problema) {
        this.problema = problema;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public final IChromosome createSampleChromosome() {
        try {
            Gene[] genes = new Gene[CITIES];

            // Create a sample chromosome from consecutive gene numbers
            for (int i = 0; i < genes.length; i++) {
                genes[i] = new IntegerGene(getConfiguration(), 0, CITIES - 1);
                genes[i].setAllele(i);
            }
            IChromosome sample = new Chromosome(getConfiguration(), genes);
            return sample;

        } catch (InvalidConfigurationException iex) {
            return null;
        }

    }
}
