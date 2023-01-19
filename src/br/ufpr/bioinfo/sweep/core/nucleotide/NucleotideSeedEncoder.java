package br.ufpr.bioinfo.sweep.core.nucleotide;

import br.ufpr.bioinfo.sweep.core.SeedEncoder;

/**
 *
 * @author Dieval Guizelini
 */
public class NucleotideSeedEncoder extends SeedEncoder {
    
    private final byte[] code;
    private byte[] symbols;
    
    public NucleotideSeedEncoder() {
        byte[] aux = new byte[128];
        aux[(byte) 'A'] = 0;   // 00
        aux[(byte) 'C'] = 1;   // 01
        aux[(byte) 'G'] = 2;   // 10
        aux[(byte) 'T'] = 3;   // 11
        aux[(byte) 'a'] = 0;   // 00
        aux[(byte) 'c'] = 1;   // 01
        aux[(byte) 'g'] = 2;   // 10
        aux[(byte) 't'] = 3;   // 11
        code = aux;        
        symbols = new byte[]{'A','C','G','T'};
    }

    @Override
    public byte[] decode(int value, byte len) {
        byte[] res = new byte[len];
        //byte[] codes = new byte[]{'A', 'C', 'G', 'T'};
        final byte[] codes = symbols;
        int v = value;
        for (int i = len - 1; i >= 0; i--) {
            res[i] = codes[v & 0x03];
            v = v >> 2;
        }
        return res;
    }

    @Override
    public int encodeInt(byte[] v, byte len) {
        int value = 0;
        for (int i = 0; i < len; i++) {
            value = (value << 2) | code[v[i]];
        }
        return value;
    }

    @Override
    public int getNumberOfSymbols() {
        return 4;  // a,c,g,t    symbols.length
    }

    @Override
    public byte[] getAlphabet() {
        return symbols;
    }

    @Override
    public void setAlphabet(byte[] values) {
        // @TODO: rever os valores do ENCODER
        this.symbols = values;
    }

    @Override
    public boolean isValidSymbols(byte[] v) {
        // @TODO: refazer
        return true;
    }
    
}
