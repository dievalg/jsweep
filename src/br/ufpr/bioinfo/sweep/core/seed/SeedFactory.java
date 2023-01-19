package br.ufpr.bioinfo.sweep.core.seed;

import br.ufpr.bioinfo.sweep.SweepParameters;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.seed.builder.ContinuesSeedBuilder;
import br.ufpr.bioinfo.sweep.core.seed.builder.GenericSeedBuilder;
import br.ufpr.bioinfo.sweep.core.seed.builder.PairSeedBuilder;

/**
 * SeedFactory creates the instance according to the mask and type of sequence
 * configured
 *
 * @author Dieval Guizelini
 */
public class SeedFactory {
    private static java.util.Map<Long,SeedBuilder> cache = new java.util.HashMap<Long,SeedBuilder>();

    private SeedFactory() {
    }

    public static SeedBuilder getInstance(SweepParameters params) {
        SweepMask mask = params.getMask();
        Long cacheKey = getHashMask(mask.getMask());
        SeedBuilder result = null;
        if( (result = cache.get(cacheKey)) != null ) {
            return result;
        }
        //boolean protein = params.isAminoacidSequence();\
        // @TODO reavaliar, existem elementos repetidos e tratados em SweepMask
        boolean[] m = mask.getMask();
        int numOfWords = 0; 
        int numOfSpaces = 0;
        if (m[0]) {
            numOfWords = 1;
        } else {
            numOfSpaces = 1;
        }
        int i = 1;
        while (i < m.length) {
            if (m[i] != m[i - 1]) {
                if (m[i]) {
                    numOfWords++;
                } else {
                    numOfSpaces++;
                }
            }
            i++;
        }
        Long encodeMask = getHashMask(mask.getMask());
        if( numOfWords == 1 && numOfSpaces == 0 ) {
            return new ContinuesSeedBuilder(mask.getMask());
        } else if( numOfWords == 2 && numOfSpaces == 1 ) { // pair-seed
            return new PairSeedBuilder(mask.getMask());
        } else {
            return new GenericSeedBuilder(mask,BasicSeed.class);
        }
    }
    
    private static Long getHashMask(boolean[] values) {
        long value = 0;
        for(int i=0 ; i<values.length ; i++) {
            if( values[i] ) {
                value = value << 1 | 0x01;
            } else {
                value = value << 1;
            }
        }
        return value;
    }

}
