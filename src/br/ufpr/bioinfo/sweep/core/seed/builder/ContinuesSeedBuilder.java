package br.ufpr.bioinfo.sweep.core.seed.builder;

import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.seed.BasicSeed;
import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 */
public class ContinuesSeedBuilder extends SeedBuilder<Seed> {
    private int len=2;
    
    public ContinuesSeedBuilder(int len) {
        this.len = len;
    }    
    
    public ContinuesSeedBuilder(boolean[] value) {
        configure(value);
    }
    
    public ContinuesSeedBuilder(SweepMask mask) {
        configure(mask.getMask());
    }
    
    private void configure(boolean[] value) {
        int conta = 0;
        for(boolean b : value) {
            if( b ) {
                conta++;
            }
        }
        this.len = conta;
    }

    @Override
    public List<Seed> getSeeds(byte[] sequence) {
        if( sequence == null ) {
            return null;
        }
        int numSeeds = sequence.length-len;
        List<Seed> result = new ArrayList<Seed>(numSeeds);
        for(int i=0 ; i<numSeeds ; i++ ) {
            byte[] novo = new byte[len];
            System.arraycopy(sequence, i, novo, 0, novo.length);
            Seed s = new BasicSeed(novo);
            result.add(s);
        }
        return result;
    }

    @Override
    public Seed getSeed(byte[] sequence) {
        byte[] novo = new byte[len];
        System.arraycopy(sequence, 0, novo, 0, novo.length);
        return new BasicSeed(novo);
    }
    
}