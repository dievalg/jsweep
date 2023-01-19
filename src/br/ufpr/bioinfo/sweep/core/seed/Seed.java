package br.ufpr.bioinfo.sweep.core.seed;

/**
 *
 * @author Dieval Guizelini
 */
public abstract class Seed {
    
    public Seed() { 
    }
    
    /*public static Seed getInstance(byte[] value) {
        return new BasicSeed(value);
    }*/
    
    public abstract byte[] getValue();
    
    public abstract void setValue( byte[] v );
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract boolean equals(Object obj);
    
    public java.util.Comparator<Seed> getSeedComparator() {
        return new java.util.Comparator<Seed>(){
            @Override
            public int compare(Seed o1, Seed o2) {
                if( o1 == null && o2 == null ) {
                    return 0;
                }
                if( o1 != null && o2 == null ) {
                    return -1;
                }
                if( o1 == null && o2 != null ) {
                    return 1;
                }
                byte[] seq1 = o1.getValue();
                byte[] seq2 = o2.getValue();
                if( seq1.length != seq2.length ) {
                    return seq1.length - seq2.length;
                }
                for(int i=0 ; i<seq1.length ; i++) {
                    if( seq1[i] != seq2[i] ) {
                        return (int)seq1[i] - (int)seq2[i];
                    }
                }
                return 0;
            }
        };
    }
    
}
