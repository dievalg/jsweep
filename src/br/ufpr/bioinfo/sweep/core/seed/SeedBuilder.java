package br.ufpr.bioinfo.sweep.core.seed;

import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 * @param <T>
 */
public abstract class SeedBuilder<T extends Seed> {

    protected SweepMask mask = null;

    public SeedBuilder() {
    }

    public SeedBuilder(SweepMask mask) {
        this.mask = mask;
    }
    
    public abstract List<T> getSeeds(byte[] sequence);

    public abstract T getSeed(byte[] sequence);

}
