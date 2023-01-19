package br.ufpr.bioinfo.sweep.core;

import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;

/**
 * creates a number index for the seeds.
 * 
 * @param <T extends Seed>
 * @author Dieval Guizelini
 */
public interface SeedIndex<T extends Seed> {
    
    int getIndex(T seed);
    
    T getSeed(int index);

    void createIndex(SeedBuilder<T> builder);
    
    void validadeIndex(SeedBuilder<T> builder);
}