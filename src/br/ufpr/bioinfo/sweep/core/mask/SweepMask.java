package br.ufpr.bioinfo.sweep.core.mask;

/**
 *
 * @author Dieval Guizelini
 */
public class SweepMask {
    public static final byte ONE = (byte)1;
    public static final byte ZERO = (byte)0;
    /** @value mask used to create seeds */
    private boolean[] mask = null;
    /** @value lengthOfSubsequence length of subsequence need to create one seed. */
    private int lengthOfSubsequence = 0;
    /** @value sizeOfSubset number of bases on seed. */
    private int sizeOfSubset = 0;
    /** @value sizeOfSubset number of bases on seed. */
    private String[] segments = null;
    private byte[] tokenType = null;
    
    public SweepMask() {
    }
    
    public SweepMask(String mask) {
        setMask(mask);
    }
    
    public final void setMask(String mask) {
        char[] cmask = mask.toCharArray();
        int size = mask.trim().length();
        boolean[] newMask = new boolean[ size ];
        int numberOfOnes = 0;
        //
        java.util.List<String> partesList = new java.util.ArrayList<String>();
        byte[] tokenType = new byte[1024];
        char anterior = cmask[0];
        int start = 0, end = 1, idxParte = 0;
        for (int i = 0,k=0 ; i < cmask.length; i++) {
            if( cmask[i] == '1' ) {
                newMask[k]=true;
                numberOfOnes++;
                k++;
            } else if( cmask[i] == '0' ) {
                newMask[k]=false;
                k++;
            }
            if (cmask[i] != anterior) {
                String sub = mask.substring(start, end);
                start = end;
                partesList.add(sub);
                tokenType[idxParte] = (byte) (anterior == '1' ? ONE : ZERO);
                idxParte++;
            }
        }
        if (end - start > 0) {
            String sub = mask.substring(start, end);
            partesList.add(sub);
            tokenType[idxParte] = (byte) (anterior == '1' ? ONE : ZERO);
        }
        synchronized( this ) {
            this.lengthOfSubsequence = size;
            this.mask = newMask;
            this.sizeOfSubset = numberOfOnes;
            //
            this.segments = new String[partesList.size()];
            this.tokenType = new byte[partesList.size()];
            for (int i = 0; i < partesList.size(); i++) {
                this.segments[i] = partesList.get(i);
                this.tokenType[i] = tokenType[i];
            }
        }
    }
    
    public boolean[] getMask() {
        return this.mask;
    }
    
    public int getSubsequenceLength() {
        return this.lengthOfSubsequence;
    }
    
    public int getSizeOfSubset() {
        return this.sizeOfSubset;
    }

    /**
     * @return the numberOfSegments
     */
    public int getNumberOfSegments() {
        return segments == null ? 0 : segments.length;
    }
    
    public String[] getSegments() {
        return this.segments;
    }
    
    public byte[] getSegmentsToken() {
        return this.tokenType;
    }
    
    @Override
    public String toString() {
        if( mask == null ) {
            return "";
        }
        char[] aux = new char[mask.length];
        for(int i=0 ; i<aux.length ; i++ ) {
            aux[i] = mask[i] ? '1' : '0';
        }
        return new String(aux);
    }
    
}