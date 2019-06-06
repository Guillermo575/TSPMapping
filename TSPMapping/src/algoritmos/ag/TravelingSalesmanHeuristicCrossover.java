package algoritmos.ag;

import DiccionarioDatos.BruteForce;
import DiccionarioDatos.Coordenada;
import DiccionarioDatos.MarkerDictionary;
import java.util.*;

import org.jgap.*;
import org.jgap.impl.IntegerGene;
import org.jgap.util.*;

public class TravelingSalesmanHeuristicCrossover extends BaseGeneticOperator {

    private int m_startOffset = 1;
    private ArrayList<Coordenada> salesman;
    private BruteForce br=new BruteForce();
    
    boolean ASSERTIONS = true;

    public TravelingSalesmanHeuristicCrossover() throws InvalidConfigurationException {
        super(Genotype.getStaticConfiguration());
    }

    public TravelingSalesmanHeuristicCrossover(Configuration a_configuration,ArrayList<Coordenada> salesman, BruteForce br)
     throws InvalidConfigurationException {

        super(a_configuration);
        this.salesman = salesman;
        this.br = br;
    }

    @Override
    public int compareTo(final Object a_other) {

        if (a_other == null) {
            return 1;
        }
        TravelingSalesmanHeuristicCrossover op = (TravelingSalesmanHeuristicCrossover) a_other;
        if (getStartOffset() < op.getStartOffset()) {
            return 1;
        } else if (getStartOffset() > op.getStartOffset()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getStartOffset() {

        return m_startOffset;
    }

    public void operate(final IChromosome a_firstMate,
            final IChromosome a_secondMate) {

        Gene[] g1 = a_firstMate.getGenes();
        Gene[] g2 = a_secondMate.getGenes();

        Gene[] c1, c2;
        try {
           
            c1 = operate(g1, g2);
            c2 = operate(g2, g1);

            a_firstMate.setGenes(c1);
            a_secondMate.setGenes(c2);
        } catch (InvalidConfigurationException cex) {
            throw new Error("Error occured while operating on:"
                    + a_firstMate + " and "
                    + a_secondMate
                    + ". First " + m_startOffset + " genes were excluded "
                    + "from crossover. Error message: "
                    + cex.getMessage());
        }
    }

    @Override
    public void operate(final Population a_population,
            final List a_candidateChromosomes) {

        int size = Math.min(getConfiguration().getPopulationSize(),
                a_population.size());

        int numCrossovers = size / 2;

        RandomGenerator generator = getConfiguration().getRandomGenerator();

        for (int i = 0; i < numCrossovers; i++) {

            int position1 = generator.nextInt(size);
            IChromosome origChrom1 = a_population.getChromosome(position1);
            IChromosome firstMate = (IChromosome) ((ICloneable) origChrom1).clone();

            int position2 = generator.nextInt(size);
            IChromosome origChrom2 = a_population.getChromosome(position2);
            IChromosome secondMate = (IChromosome) ((ICloneable) origChrom2).clone();

            if (m_monitorActive) {
                firstMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
                firstMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
                secondMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
                secondMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
            }

            operate(firstMate, secondMate);

            a_candidateChromosomes.add(firstMate);
            a_candidateChromosomes.add(secondMate);
        }
    }

    public void setStartOffset(int a_offset) {

        m_startOffset = a_offset;
    }

    protected Gene findNext(final Gene[] a_g, final Gene a_x) {

        for (int i = m_startOffset; i < a_g.length - 1; i++) {
            if (a_g[i].equals(a_x)) {
                return a_g[i + 1];
            }
        }
        return null;
    }

    protected Gene[] operate(final Gene[] a_g1, final Gene[] a_g2) {
        int n = a_g1.length;
        LinkedList out = new LinkedList();
        TreeSet not_picked = new TreeSet();

        int size = getConfiguration().getChromosomeSize();

        RandomGenerator generator = getConfiguration().getRandomGenerator();

        int parent = generator.nextInt(2);

        if (parent == 0) {
            out.add(a_g1[m_startOffset]);
        } else if (parent == 1) {
            out.add(a_g2[m_startOffset]);
        }

        for (int j = m_startOffset + 1; j < n; j++) { 

            if ((parent == 0) && ASSERTIONS && not_picked.contains(a_g1[j])) {
                throw new Error("All genes must be different for "
                        + getClass().getName()
                        + ". The gene " + a_g1[j] + "[" + j
                        + "] occurs more "
                        + "than once in one of the chromosomes. ");
            }

            if ((parent == 1) && ASSERTIONS && not_picked.contains(a_g2[j])) {
                throw new Error("All genes must be different for "
                        + getClass().getName()
                        + ". The gene " + a_g2[j] + "[" + j
                        + "] occurs more "
                        + "than once in one of the chromosomes. ");
            }

            if (parent == 0) {
                not_picked.add(a_g1[j]);
            } else if (parent == 1) {
                not_picked.add(a_g2[j]);
            }

        }

        if (ASSERTIONS) {
            if (a_g1.length != a_g2.length) {
                throw new Error("Chromosome sizes must be equal");
            }
        }

        while (not_picked.size() > 1) {

            Gene last = (Gene) out.getLast();

            Gene n1 = findNext(a_g1, last);
            Gene n2 = findNext(a_g2, last);

            Gene picked, other;
            boolean pick1;
            if (n1 == null) {
                pick1 = false;
            } else if (n2 == null) {
                pick1 = true;
            } else {
                int genlast = ((IntegerGene)last).intValue();
                int genn1 = ((IntegerGene)n1).intValue();
                int genn2 = ((IntegerGene)n2).intValue();
                pick1 = br.getDistance(salesman.get(genlast), salesman.get(genn1)) < br.getDistance(salesman.get(genlast), salesman.get(genn2));
            }
            if (pick1) {
                picked = n1;
                other = n2;
            } else {
                picked = n2;
                other = n1;
            }

            if (out.contains(picked)) {
                picked = other;
            }

            if ((picked == null) || out.contains(picked)) {

                Object[] randomPick = not_picked.toArray();
                picked = (Gene) randomPick[generator.nextInt(randomPick.length)];

            }
            out.add(picked);
            not_picked.remove(picked);
        }

        if (ASSERTIONS && (not_picked.size() != 1)) {
            throw new Error(
                    "Given Gene not correctly created (must have length > 1"
                    + ")");
        }
        out.add(not_picked.last());

        Gene[] g = new Gene[n];
        Iterator gi = out.iterator();
        for (int i = 0; i < m_startOffset; i++) {
            if (parent == 0) {
                g[i] = a_g1[i];
            } else if (parent == 1) {
                g[i] = a_g2[i];
            }

        }

        if (ASSERTIONS) {
            if (out.size() != g.length - m_startOffset) {
                throw new Error("Unexpected internal error. "
                        + "These two must be equal: " + out.size()
                        + " and " + (g.length - m_startOffset) + ", g.length "
                        + g.length + ", start offset " + m_startOffset);
            }
        }
        for (int i = m_startOffset; i < g.length; i++) {
            g[i] = (Gene) gi.next();
        }
        return g;
    }
}
