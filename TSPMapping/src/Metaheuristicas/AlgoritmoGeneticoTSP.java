package Metaheuristicas;

import DiccionarioDatos.BruteForce;
import DiccionarioDatos.Coordenada;
import DiccionarioDatos.Especie;
import DiccionarioDatos.SolucionTSP;
import algoritmos.ag.*;
import java.util.*;
import org.jgap.*;

public class AlgoritmoGeneticoTSP {

    private final ArrayList<Coordenada> problema;
    BruteForce br = new BruteForce();
    private ConfiguracionTSP c;
    private Genotype prueba;

    public AlgoritmoGeneticoTSP(ArrayList<Coordenada> problema,BruteForce br) {
        this.problema = problema;
        this.br = br;
        c = new ConfiguracionTSP();
        c.makeConfiguration(problema,br,"3");
    }
    
    public void setConfigurationTSP(int numGen,double cullingPercentage,int mutationRate){
        c = new ConfiguracionTSP();
        c.setNumGen(numGen);
        c.setCullingPercentage(cullingPercentage);
        c.setMutationRate(mutationRate);
        c.makeConfiguration(problema,br,"3");
    }
    
    public IChromosome[] TSPInicializaPoblacion(ConfiguracionTSP c, ArrayList<Coordenada> p) {
        Gene[] samplegenes = c.getConfiguration().getSampleChromosome().getGenes();
        IChromosome[] chromosomes = new IChromosome[c.getPopulationSize()];
        LinkedList<Integer> cityList = new LinkedList();
        for (int j = 1; j < samplegenes.length; j++) { cityList.add(j); }
        try {
            for (int i = 0; i < chromosomes.length; i++) {
                Gene[] genes = new Gene[samplegenes.length];
                Collections.shuffle(cityList);
                genes = StochasticInitialization.operate(genes, samplegenes, p,(LinkedList<Integer>) cityList.clone(),br);
                chromosomes[i] = new Chromosome(c.getConfiguration(), genes);
            }
        } catch (InvalidConfigurationException iex) {
            System.out.println("Error" + iex);
            System.exit(0);
        }
        return chromosomes;
    }    
    
    public SolucionTSP start() {
        
        IChromosome[] chromosomes = TSPInicializaPoblacion(c, problema);
        try {
            prueba = new Genotype(c.getConfiguration(),new Population(c.getConfiguration(), chromosomes));
        } catch (InvalidConfigurationException ex) {
            System.out.println("Error" + ex);
            System.exit(-1);
        }        
//        IChromosome best = prueba.getFittestChromosome();
//        int[] arreglo = new int[best.getGenes().length];
//        Gene[] genes = best.getGenes();
//        for (int j = 0; j < arreglo.length; j++) {
//            arreglo[j] = (Integer) genes[j].getAllele();
//        }  
        
        IChromosome best = null;
        SolucionTSP solucion = new SolucionTSP();
        solucion.setListaOriginal(problema);
        solucion.setBruteForce(br);
        solucion.setDescripcion("Algoritmo Genetico");
        Integer[] arreglo = new Integer[0];
        ArrayList<Coordenada> mejor = new ArrayList<Coordenada>();
        double resultadomejor = br.distanciaTotal(problema);
        
        for (int i = 1; i <= c.getNumGen(); i++) {
            prueba.evolve();
            best = prueba.getFittestChromosome();
            arreglo = new Integer[best.getGenes().length];
            Gene[] genes = best.getGenes();
            for (int j = 0; j < best.getGenes().length; j++) {
                arreglo[j]=(Integer) genes[j].getAllele();
            }
            solucion.getGenesElegidos().add(arreglo);
            ArrayList<Coordenada> nuevo = new ArrayList<Coordenada>();
            Especie especie = new Especie() ;
            for(int l=0; l<arreglo.length;l++){
                nuevo.add(problema.get(arreglo[l]));
            }     
            especie.setResultado(br.distanciaTotal(nuevo));
            if(especie.getResultado()<resultadomejor){
                resultadomejor = especie.getResultado();
                solucion.getEspeciesElegidas().add(especie);           
            }
        }
        for(int l=0; l<arreglo.length;l++){
            mejor.add(problema.get(arreglo[l]));
        }        
        solucion.setMejorGen(arreglo);
        solucion.setMejorEspecie(mejor);
        return solucion;
    }

}
