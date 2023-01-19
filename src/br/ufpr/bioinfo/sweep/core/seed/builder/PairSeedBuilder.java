/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufpr.bioinfo.sweep.core.seed.builder;

import br.ufpr.bioinfo.sweep.core.seed.BasicPairBasicSeed;
import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 */
public class PairSeedBuilder extends SeedBuilder<Seed> {
    private int firstStart=0,firstLen=2;
    private int secondStart=3,secondLen=2;
    
    public PairSeedBuilder(boolean[] mask) {
        int i=0;
        while( i<mask.length && !mask[i] ) {
            i++;
        }
        this.firstStart = i; // start position of first subsequence
        while( i<mask.length && mask[i] ) {
            i++;
        }
        this.firstLen = i-firstStart;
        // pass spaced aread (false mask)
        while( i<mask.length && !mask[i] ) {
            i++;
        }
        // get second part
        this.secondStart = i; // start position of first subsequence
        while( i<mask.length && mask[i] ) {
            i++;
        }
        this.secondLen = i-secondStart;
    }

    @Override
    public List<Seed> getSeeds(byte[] sequence) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Seed getSeed(byte[] sequence) {
        byte[] s1 = new byte[firstLen];
        byte[] s2 = new byte[secondLen];
        System.arraycopy(sequence, firstStart, s1, 0, s1.length);
        System.arraycopy(sequence, secondStart, s2, 0, s2.length);
        return new BasicPairBasicSeed(s1,s2);
    }
    
}
