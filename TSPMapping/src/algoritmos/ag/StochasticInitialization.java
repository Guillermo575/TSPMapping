package algoritmos.ag;

import DiccionarioDatos.BruteForce;
import DiccionarioDatos.Coordenada;
import DiccionarioDatos.MarkerDictionary;
import java.util.*;

import org.jgap.*;
import org.jgap.impl.*;

public class StochasticInitialization {
    public static int evaluate(LinkedList<Integer> cityList,ArrayList<Coordenada> salesman,BruteForce br) {
       
        double s = 0;
        if (cityList.size() == 1) {
            return Integer.MAX_VALUE;
        }
        for (int i = 0; i < cityList.size() - 1; i++) {
            s +=  br.getDistance(salesman.get(cityList.get(i)), salesman.get(cityList.get(i + 1)));
        }
        s += br.getDistance(salesman.get(cityList.get(cityList.size() - 1)), salesman.get(cityList.get(0)));
        return ((int) (s / cityList.size()));
    }

    
    public static Gene[] operate(Gene[] genes, Gene[] sampleGenes,ArrayList<Coordenada> salesman, LinkedList<Integer> cityList,BruteForce br) {

        genes[0] = sampleGenes[0].newGene();
        genes[0].setAllele(sampleGenes[0].getAllele());
        Random generator = new Random();

        for (int i = 1; i < genes.length; i++) {

            int distance, average, location;
            int counter = 0;
            Random random = new Random();

            do {

                location = generator.nextInt(cityList.size());

                average = evaluate(cityList, salesman,br);
                int gena = ((IntegerGene) genes[i - 1]).intValue();
                distance = (int)br.getDistance(salesman.get(gena), salesman.get(cityList.get(location)));

                counter++;
                if (((distance > average) && (counter == (int) ((genes.length * .2))))
                        || (random.nextInt((int) (genes.length * .3) / counter) == 0)) {
                    counter = 0;
                    break;
                }
            } while (distance > average);

            genes[i] = sampleGenes[cityList.get(location)];
            genes[i].setAllele(sampleGenes[cityList.get(location)].getAllele());

            cityList.remove(location);
        }
        return genes;
    }
}
