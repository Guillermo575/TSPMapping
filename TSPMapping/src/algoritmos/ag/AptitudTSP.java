package algoritmos.ag;

import DiccionarioDatos.*;
import java.util.ArrayList;
import org.jgap.*;
import org.jgap.impl.IntegerGene;

public class AptitudTSP extends FitnessFunction {

    private final ArrayList<Coordenada> m_salesman;
    private BruteForce br=new BruteForce();
    
    public AptitudTSP(ArrayList<Coordenada> m_salesman,BruteForce br) {
        this.br = br;
        this.m_salesman = m_salesman;
    }

    @Override
    protected double evaluate(final IChromosome a_subject) {
        double s = 0;
        Gene[] genes = a_subject.getGenes();
        for (int i = 0; i < genes.length - 1; i++) {
            int gena = ((IntegerGene)genes[i]).intValue();
            int genb = ((IntegerGene)genes[i + 1]).intValue();
            s += br.getDistance(m_salesman.get(gena), m_salesman.get(genb));
        }
        int genc = ((IntegerGene)genes[genes.length - 1]).intValue();
        s += br.getDistance(m_salesman.get(genc), m_salesman.get(0));
        return s;
    }
    

    
}
