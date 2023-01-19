package br.ufpr.bioinfo.sweep.core.seed;

import java.util.Arrays;

/**
 *
 * @author Dieval Guizelini
 */
public class BasicPairBasicSeed extends PairSeed {
    protected byte[] value;
    protected byte[] value2;
    
    public BasicPairBasicSeed() { 
        this.value = null;
        this.value2 = null;
    }
    
    public BasicPairBasicSeed(byte[] seed1, byte[] seed2) {
        this.value = seed1;
        this.value2 = seed2;
    }
    
    @Override
    public byte[] getSecondValue() {
        return value2;
    }
    
    @Override
    public void setSecondValue( byte[] v ) {
        this.value2 = new byte[v.length];
        System.arraycopy(v, 0, this.value2, 0, v.length);
    }
    
    @Override
    public void setValues( byte[] s1, byte[] s2 ) {
        this.value = new byte[s1.length];
        System.arraycopy(s1, 0, this.value, 0, s1.length);
        
        this.value2 = new byte[s2.length];
        System.arraycopy(s2, 0, this.value2, 0, s2.length);        
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
        return new String(value) + new String(value2);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i=0 ; i<value.length ; i++ ) {
            // 31 para AA e 2 para NT
            hash = hash*31 + (int)value[i];
        }
        for(int i=0 ; i<value2.length ; i++ ) {
            // 31 para AA e 2 para NT
            hash = hash*31 + (int)value2[i];
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
        final BasicPairBasicSeed other = (BasicPairBasicSeed) obj;
        boolean b1 = !Arrays.equals(this.value, other.value);
        if( b1 ) {
            boolean b2 = !Arrays.equals(this.value2, other.value2);
            return b2;
        }
        return b1;
    }
}