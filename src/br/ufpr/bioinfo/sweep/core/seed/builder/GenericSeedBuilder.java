package br.ufpr.bioinfo.sweep.core.seed.builder;

import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dieval Guizelini
 */
public class GenericSeedBuilder<T extends Seed> extends SeedBuilder {
//    private int seedLength = 2;
//    private int subsetLength = 2;
    private SweepMask mask = null;
    private Class<T> seedClass = null;

    public GenericSeedBuilder() {
    }

    public GenericSeedBuilder(SweepMask value, Class<T> clazz) {
//        this.seedLength = value.getSubsequenceLength();
//        this.subsetLength = value.getSizeOfSubset();
        this.seedClass = clazz;
        this.mask = value;
        //String[] segs = value.getSegments();
        /*for (String s : segs) {
            for (int j = 0; j < s.length(); j++, i++) {
                mask[i] = (byte) s.charAt(j);
            }
        }*/
    }

    @Override
    public List getSeeds(byte[] sequence) {
        final int seedLength = mask.getSubsequenceLength();
        int numSeeds = sequence.length - seedLength;
        if( numSeeds < 1 ) {
            return new java.util.ArrayList<T>();
        }
        final int subsetLength = mask.getSizeOfSubset();
        byte[] seed = new byte[seedLength];
        byte[] subseed = new byte[subsetLength];
        final byte ONE = (byte)'1';
        List<T> result = new java.util.ArrayList<T>(numSeeds);
        final boolean[] mascara = mask.getMask();
        for (int i = 0; i < numSeeds; i++) {
            System.arraycopy(sequence, i, seed, 0, seed.length);
            for (int j = 0, k = 0; j < seedLength; j++) {
                if( mascara[j] ) {
                    subseed[k] = seed[j];
                    k++;
                }
            }
            try {
                T nova = seedClass.newInstance();
                nova.setValue(subseed);
                result.add(nova);
            } catch (InstantiationException ex) {
                Logger.getLogger(GenericSeedBuilder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GenericSeedBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public Seed getSeed(byte[] sequence) {
        final int seedLength = mask.getSubsequenceLength();
        if( sequence.length < seedLength ) {
            throw new RuntimeException("the length of sequence needs more than seed-length");
        }
        final int subsetLength = mask.getSizeOfSubset();
        byte[] subseed = new byte[subsetLength];
        final boolean[] mascara = mask.getMask();
        for (int j = 0, k = 0; j < seedLength; j++) {
            if( mascara[j] ) {
                subseed[k] = sequence[j];
                k++;
            }
        }
        try {
            T nova = seedClass.newInstance();
            nova.setValue(subseed);
            return nova;
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericSeedBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericSeedBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
}
