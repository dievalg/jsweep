package br.ufpr.bioinfo.sweep.core.seed;

import java.util.Arrays;

/**
 *
 * @author Dieval Guizelini
 */
public class BasicSeed extends Seed {
    protected byte[] value;
    
    public BasicSeed() { 
        this.value = null;
    }
    
    public BasicSeed(byte[] seed) {
        this.value = seed;
    }
    
    @Override
    public byte[] getValue() {
        return this.value;
    }
    
    @Override
    public void setValue( byte[] v ) {
        this.value = new byte[v.length];
        System.arraycopy(v, 0, this.value, 0, v.length);
    }
    
    @Override
    public String toString() {
        return new String(value);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i=0 ; i<value.length ; i++ ) {
            // 31 para AA e 2 para NT
            hash = hash*31 + (int)value[i];
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicSeed other = (BasicSeed) obj;
        return !Arrays.equals(this.value, other.value);
    }
}
