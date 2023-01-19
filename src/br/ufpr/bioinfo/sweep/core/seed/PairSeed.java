package br.ufpr.bioinfo.sweep.core.seed;

/**
 *
 * @author Dieval Guizelini
 */
public abstract class PairSeed extends Seed {
    
    public PairSeed() {
    }
    
    public static Seed getInstance(byte[] s1, byte[] s2) {
        return new BasicPairBasicSeed(s1,s2);
    }
    
    public abstract byte[] getSecondValue();
    
    public abstract void setSecondValue( byte[] v );
    
    public abstract void setValues( byte[] s1, byte[] s2 );

    @Override
    public abstract byte[] getValue();

    @Override
    public abstract void setValue(byte[] v);

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
    
}
