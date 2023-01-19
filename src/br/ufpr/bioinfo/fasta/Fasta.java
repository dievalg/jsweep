package br.ufpr.bioinfo.fasta;

import java.util.Arrays;

/**
 *
 * @author Dieval Guizelini
 */
public class Fasta {
    // header sequence - for blast compatibility, warning more 60 bytes
    protected String header;
    // for DNA,RNA ou protein sequence
    protected byte[] sequence;

    public Fasta() {
    }
    
    public Fasta(String header, String seq) {
        this.header = header;
        setSequence(seq);
    }

    /**
     * @param sequencia the sequencia to set
     */
    public void setSequence(String sequencia) {
        int tamanho = 0;
        for(int i=0 ; i<sequencia.length(); i++) {
            char ch = sequencia.charAt(i);
            if( (ch >= 'A' && ch <= 'Z') ||
                (ch >= 'a' && ch <= 'z') ||
                (ch >= '0' && ch <= '9') ||    
                ch == '-' ) {
                tamanho++;
            }
        }
        this.sequence = new byte[tamanho];
        for(int i=0,j=0 ; i<sequencia.length(); i++) {
            char ch = sequencia.charAt(i);
            if( (ch >= 'A' && ch <= 'Z') ||
                (ch >= 'a' && ch <= 'z') ||
                (ch >= '0' && ch <= '9') ||    
                ch == '-' ) {
                this.sequence[j] = (byte)sequencia.charAt(i);
                j++;
            }
        }
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Define new sequence header
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * get sequence
     * @return the sequence
     */
    public byte[] getSequence() {
        return sequence;
    }

    /**
     * Set sequence
     * @param sequence the sequence to set
     */
    public void setSequence(byte[] sequence) {
        this.sequence = sequence;
    }

    /**
     * Search subsequence on sequence
     * @param subsequencia
     * @return -1 while not found or array position.
     */
    public int indexOf(String subsequence) {
        return indexOf(subsequence.toCharArray());
    }

    /**
     * Search subsequence on sequence
     * @param subsequencia
     * @return -1 while not found or array position.
     */
    public int indexOf(char[] subsequence) {
        if (subsequence == null || subsequence.length < 1) {
            return -1;
        }
        byte[] seq = new byte[subsequence.length];
        for (int i = 0; i < subsequence.length; i++) {
            seq[i] = (byte) subsequence[i];
        }
        return indexOf(seq, 0);
    }

    /**
     * Search subsequence on sequence, start search on fromIndex position.
     * @param subseq
     * @param fromIndex
     * @return -1 while not found or array position.
     */
    public int indexOf(byte[] subseq, int fromIndex) {
        return indexOf(this.sequence, 0, this.sequence.length,
                subseq, 0, subseq.length, fromIndex);
    }

    /**
     * 
     * @param source
     * @param sourceOffset
     * @param sourceCount
     * @param target
     * @param targetOffset
     * @param targetCount
     * @param fromIndex
     * @return 
     */
    public int indexOf(byte[] source, int sourceOffset, int sourceCount,
            byte[] target, int targetOffset, int targetCount,
            int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }
        byte first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);
        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++);
                if (j == end) {
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    /**
     * 
     * @param beginIndex
     * @return 
     */
    public byte[] subsequence(int beginIndex) {
        return subsequence(beginIndex, sequence.length);
    }

    /**
     * 
     * @param beginIndex
     * @param endIndex
     * @return 
     */
    public byte[] subsequence(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > sequence.length) {
            endIndex = sequence.length;
        }
        if (beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
        }
        byte[] sub = Arrays.copyOfRange(sequence, beginIndex, endIndex);
        return sub;
    }

    /**
     * 
     * @param beginIndex
     * @return 
     */
    public String substring(int beginIndex) {
        return substring(beginIndex, sequence.length);
    }

    /**
     * 
     * @param beginIndex
     * @param endIndex
     * @return 
     */
    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > sequence.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
        }
        byte[] sub = Arrays.copyOfRange(sequence, beginIndex, endIndex);
        return new String(sub);
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash;
        // cada base usa 2 bits para int de 32, então cabe 16 bases
        int elem = (int)(
                (((double)this.sequence.length)
                / 16d)+0.5                        
                );
        if( elem > 1 ) {
            int[] lista = new int[elem];
            for(int i=0, ind=0 ; i<sequence.length ; i++ ) {
                byte[] ss = Arrays.copyOfRange(sequence, i*16, 
                        Math.min(sequence.length,(i+1)*16));
                int code = 0;
                for(int j=0; j<ss.length ; j++) {
                    switch( ss[j] ) {
                        case 65:    // A
                        case 97:
                            code = code << 2;
                        break;
                        case 67:    // C
                        case 99:
                            code = (code << 2) | 0x01;
                        break;
                        case 71:    // C
                        case 103:
                            code = (code << 2) | 0x02;
                        break;
                        default:   // T
                            code = (code << 2) | 0x03;
                    }
                }
                lista[ind++]=code;
            }
            hash = Arrays.hashCode(lista);
        } else {
                int code = 0;
                for(int j=0; j<sequence.length ; j++) {
                    switch( sequence[j] ) {
                        case 65:    // A
                        case 97:
                            code = code << 2;
                        break;
                        case 67:    // C
                        case 99:
                            code = (code << 2) | 0x01;
                        break;
                        case 71:    // C
                        case 103:
                            code = (code << 2) | 0x02;
                        break;
                        default:   // T
                            code = (code << 2) | 0x03;
                    }
                }
            hash = code;
        }
//        int hash = 7;
//        hash = 41 * hash + (this.header != null ? this.header.hashCode() : 0);
//        hash = 41 * hash + Arrays.hashCode(this.sequence);
        return hash;
    }

    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        //if (getClass() != obj.getClass()) {
        //    return false;
        //}
        if( !(obj instanceof Fasta) ) {
            return false;
        }
        final Fasta other = (Fasta)obj;
        if ((this.header == null) ? (other.header != null) : !this.header.equals(other.header)) {
            return false;
        }
        if (!Arrays.equals(this.sequence, other.sequence)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return 
     */
    public int getSequenceHashCode() {
        // if length 
        int hash = 7;
        hash = 41 * hash + Arrays.hashCode(this.sequence);
        return hash;
    }

    /**
     * This method verify if two objects have same sequence.
     * @param obj other BaseSequence object
     * @return true, if sequences equals.
     */
    public boolean sequenceEquals(Object obj) {
        if (obj == null) {
            return false;
        }
        //if (getClass() != obj.getClass()) {
        //    return false;
        //}
        if( !(obj instanceof Fasta) ) {
            return false;
        }
        final Fasta other = (Fasta)obj;
        if (!Arrays.equals(this.sequence, other.sequence)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format(">%s\n%s",header, new String(sequence));
    }
    
    
    // seqLengthCmp implements Comparator for sort sequence list by length - ascendent order
    public static java.util.Comparator<Fasta> seqLengthCmp = new java.util.Comparator<Fasta>() {

        @Override
        public int compare(Fasta o1, Fasta o2) {
            return o1.getSequence().length - o2.getSequence().length;
        }
    };
    
    
    // seqLengthCmp implements Comparator for sort sequence list by length - decrescent order
    public static java.util.Comparator<Fasta> seqLengthDescCmp = new java.util.Comparator<Fasta>() {

        @Override
        public int compare(Fasta o1, Fasta o2) {
            return o2.getSequence().length - o1.getSequence().length;
        }
    };
    
    
}