package algoritmos.ag;

import DiccionarioDatos.BruteForce;
import DiccionarioDatos.Coordenada;
import DiccionarioDatos.MarkerDictionary;
import java.util.ArrayList;
import java.util.List;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.util.*;

public class SegmentSwappingMutation
        extends MutationOperator {

    private int m_startOffset = 1;
    private ArrayList<Coordenada> salesman;
    private BruteForce br = new BruteForce();
    
    public SegmentSwappingMutation()
            throws InvalidConfigurationException {

        super();
    }

    public SegmentSwappingMutation(final Configuration a_config,final int a_desiredMutationRate,ArrayList<Coordenada> salesman,BruteForce br)
    throws InvalidConfigurationException {

        super(a_config, a_desiredMutationRate);
        this.salesman = salesman;
        this.br = br;
    }

    /**
     * Constructs a new instance of this operator with a specified mutation rate
     * calculator, which results in dynamic mutation being turned on.
     * 
     * @param a_config
     *            the configuration to use
     * @param a_mutationRateCalculator
     *            calculator for dynamic mutation rate computation
     * @throws InvalidConfigurationException
     * 
     * @author Klaus Meffert
     * @since 3.0 (previously: without a_config)
     */
    public SegmentSwappingMutation(final Configuration a_config,
            final IUniversalRateCalculator a_mutationRateCalculator)
            throws InvalidConfigurationException {

        super(a_config, a_mutationRateCalculator);
    }

    public SegmentSwappingMutation(final Configuration a_config,
            ArrayList<Coordenada> salesman)
            throws InvalidConfigurationException {

        super(a_config);
        this.salesman = salesman;
    }

    public int getStartOffset() {

        return m_startOffset;
    }

    @Override
    public void operate(final Population a_population,
            List a_candidateChromosomes) {

        final IUniversalRateCalculator m_mutationRateCalc = getMutationRateCalc();
        if ((getMutationRate() == 0) && (m_mutationRateCalc == null)) {
            return;
        }
        int currentRate;
        if (m_mutationRateCalc != null) {
            currentRate = m_mutationRateCalc.calculateCurrentRate();
        } else {
            currentRate = getMutationRate();
        }

        RandomGenerator generator = getConfiguration().getRandomGenerator();
        int size = a_population.size();

        for (int i = 0; i < size; i++) {
            IChromosome x = a_population.getChromosome(i);
            IChromosome xm = operate(x, currentRate, generator);
            if (xm != null) {
                a_candidateChromosomes.add(xm);
            }
        }
    }

    public void setStartOffset(final int a_offset) {

        m_startOffset = a_offset;
    }

    protected IChromosome operate(final IChromosome a_chrom, final int a_rate,
            final RandomGenerator a_generator) {

        IChromosome chromosome = null;

        if ((a_generator.nextInt(a_rate) == 0)) {
            if (chromosome == null) {
                chromosome = (IChromosome) ((ICloneable) a_chrom).clone();
                if (m_monitorActive) {
                    chromosome.setUniqueIDTemplate(a_chrom.getUniqueID(), 1);
                }
            }

            for (int i = 0; i < (int) (.2 * (a_chrom.size())); i++) {
                Gene[] genes = chromosome.getGenes();
                Gene[] mutated = operate(a_generator, genes);
                try {
                    chromosome.setGenes(mutated);
                } catch (InvalidConfigurationException cex) {
                    throw new Error(
                            "Gene type not allowed by constraint checker", cex);
                }
            }
        }
        return chromosome;
    }

    protected Gene[] operate(final RandomGenerator a_generator,final Gene[] a_genes) {

        int gene1 = (m_startOffset)
                + (int) (Math.random() * (((getConfiguration().getChromosomeSize() - 2) - (m_startOffset)) + 1));
        int gene2 = gene1 + 1;
        int gene3 = (m_startOffset)
                + (int) (Math.random() * (((getConfiguration().getChromosomeSize() - 2) - (m_startOffset)) + 1));
        int gene4 = gene3 + 1;

        int gen1 = ((IntegerGene)a_genes[gene1]).intValue();
        int gen2 = ((IntegerGene)a_genes[gene2]).intValue();
        int gen3 = ((IntegerGene)a_genes[gene3]).intValue();
        int gen4 = ((IntegerGene)a_genes[gene4]).intValue();
            
        int edge1 = (int) br.getDistance(salesman.get(gen1), salesman.get(gen2));
        int edge2 = (int) br.getDistance(salesman.get(gen3), salesman.get(gen4));

        int edge3 = (int) br.getDistance(salesman.get(gen1), salesman.get(gen4));
        int edge4 = (int) br.getDistance(salesman.get(gen2), salesman.get(gen3));

        if ((gene1 != gene3) && ((edge1 + edge2) > (edge3 + edge4))) {
            Gene savedGene2 = a_genes[gene2];
            Gene savedGene3 = a_genes[gene3];
            Gene savedGene4 = a_genes[gene4];
            a_genes[gene4] = savedGene3;
            a_genes[gene3] = savedGene2;
            a_genes[gene2] = savedGene4;

        }

        return a_genes;
    }

}
