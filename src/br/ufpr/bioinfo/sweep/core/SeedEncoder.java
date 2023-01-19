package br.ufpr.bioinfo.sweep.core;

/**
 *
 * @author Dieval Guizelini
 */
public abstract class SeedEncoder {
    
    public abstract byte[] decode(int value, byte len);
    
    public abstract int encodeInt(byte[] v, byte len);
    
    public abstract boolean isValidSymbols(byte[] v);
    
    public abstract int getNumberOfSymbols();
    
    public abstract byte[] getAlphabet();
    
    public abstract void setAlphabet(byte[] symbols);
    
}
