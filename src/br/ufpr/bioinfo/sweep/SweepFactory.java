package br.ufpr.bioinfo.sweep;

import br.ufpr.bioinfo.fasta.Fasta;
import br.ufpr.bioinfo.sweep.core.SeedEncoder;
import br.ufpr.bioinfo.sweep.core.SeedIndex;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.nucleotide.NucleotideSweepFactory;
import br.ufpr.bioinfo.sweep.core.protein.ProteinSweepFactory;
import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import br.ufpr.bioinfo.sweep.core.seed.SeedFactory;
import br.ufpr.bioinfo.sweep.matrix.SweepSparseMatrix;
import java.util.List;

/**
 * SweepFactory create SWeeP vectors from sequence.
 * 
 * <p>SWeeP main algorithm:<br>
 * <ol><li>Substrings (seed) extracted from the sequence based on the mask.</li>
 * <li>the seed is converted to a single number that represents the coordinate in the vector.</li>
 * <li>the value in the position indicated by the seed may be the frequency of the seed in the sequence or binary (co-occurrence).</li>
 * <li>This first vector is very sparse and the vector has high dimensionality (HDV).</li>
 * <li>The final vector (LDV) is generated from HDV vector projection.</li>
 * </ol>
 * </p> * 
 * @author Dieval Guizelini
 */
public abstract class SweepFactory {
    protected SweepParameters params = null;
    //
    public final static int NUCLEOTIDE = 1;
    public final static int AMINOACID = 2;
    public final static String DEFAULT_PROTEIN_MASK = "11011";  // 2,1,2
    public final static String DEFAULT_NUCLEOTIDE_MASK = "11011101111"; // 4,7,4 8=64k 9=262144
    // o teste antigo foi com 474 = 111 100 000 001 111 = eq a 2,1,2 em AA
    // mas a base em AA é 20^4=160k, muito mais disperso que 4^8=64k
    protected SweepMask mask = null;
    protected int projectionSize = 600;
    //
    protected SeedEncoder seedEncoder = null;
    protected SeedIndex seedMap = null;    // convert seed to pos 

    
    protected SweepFactory() {
    }
    
    protected SweepFactory(SweepParameters params) {
        this.params = params;
    }

    public static SweepFactory getInstance(SweepParameters params) {
        if( !params.isAminoacidSequence() ) {
            return new NucleotideSweepFactory(params);
        }
        return new ProteinSweepFactory(params);
    }
    
    //
    // delegate to subclass... specific implementation for protein or nucleotides.
    protected abstract void reconfigure();

    protected abstract SeedBuilder createAndConfigureSeedBuilder();

    protected abstract int numberOfRawCoords();
    
    public <N> List<SweepVector<N>> create(List<Fasta> multiFasta) {
        if (multiFasta == null || multiFasta.size() < 1) {
            return null;
        }
        List<SweepVector<N>> result = null;
        SeedBuilder seedBuilder = SeedFactory.getInstance(params);
        
        if (seedBuilder == null) {
            seedBuilder = createAndConfigureSeedBuilder();
        }
        /*java.io.File rawMatrixFilename = params.getExportFiles();
        if (this.rawMatrixFilename != null) {
            try {
                outRawVet = new java.io.PrintWriter(rawMatrixFilename);
            } catch (FileNotFoundException ex) {
                outRawVet = null;
            }
        }*/
        result = new java.util.ArrayList(multiFasta.size());
        for (Fasta fas : multiFasta) {
            // step 1
            List<? extends Seed> seeds = seedBuilder.getSeeds(fas.getSequence());
            // step 2
            
            
            
            
            SweepVector<N> vec = create(fas);
            if (vec != null) {
                result.add(vec);
            }
        }
        /*if (outRawVet != null) {
            FileUtils.close(outRawVet);
            outRawVet = null;
        }*/
        return result;
    }
    
    public <N> SweepVector<N> create(Fasta fasta) {
        if (fasta == null || fasta.getSequence() == null || fasta.getSequence().length < 5) {
            return null;
        }
/*        if (seedBuilder == null) {
            createAndConfigureSeedBuilder();
        }
        List<? extends Seed> seeds = seedBuilder.getSeeds(fasta.getSequence());
        SweepVector<N> vec = new SweepVector<N>();
        vec.setName((N) fasta.getHeader());
        SweepSparseMatrix<Integer, Double> mat = createFromSeeds(seeds);
        vec.setSpace(mat);
        if (outRawVet != null) {
            SweepUtils.printVector(vec, seedMap, numberOfRawCoords(), outRawVet);
        }
        if (autoCalculateProjection) {
            calculeProjection(vec);
        }
        return vec;*/
return null;
    }
    
    /**
     * Create high dimension vector. The vector is very sparse.
     * @param <N> Id type
     * @param fasta 
     * @return 
     */
    public <N> SweepVector<N> createHDV(Fasta fasta) {
        return null;
    }
    
    public SweepSparseMatrix<Integer, Integer> createBinHDVFromSeeds(List<? extends Seed> seeds) {
        SweepSparseMatrix<Integer, Integer> mat = new SweepSparseMatrix<Integer, Integer>(seeds.size());
        return null;
    }
    
    public SweepSparseMatrix<Integer, Double> createFromSeeds(List<? extends Seed> seeds) {
        /*SweepSparseMatrix<Integer, Double> mat = new SweepSparseMatrix<Integer, Double>(seeds.size());
        seedMap.validadeIndex(seedBuilder);
        for (Seed s : seeds) {
            int pos = seedMap.getIndex(s);
            Double old = mat.get(pos);
            if (old == null) {
                mat.setValue(pos, 1d);
            } else if (!useBinaryMatrix) {
                mat.setValue(pos, old + 1);
            }
        }
        return mat;*/
        return null;
    }
    
    
}
