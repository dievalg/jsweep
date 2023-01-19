package br.ufpr.bioinfo.sweep.core.protein;

import br.ufpr.bioinfo.sweep.core.SeedEncoder;

/**
 *
 * @author Dieval Guizelini
 */
public class ProteinSeedEncoder extends SeedEncoder {

    private static ProteinSeedEncoder INSTANCE = new ProteinSeedEncoder();

    private static byte[] ENCODER;
    // https://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi#SG11
    private static String aa = "ACDEFGHIKLMNPQRSTVWY";
    boolean[] valid = new boolean[128];

    public ProteinSeedEncoder() {
        // preserva a ultima instancia
        INSTANCE = this;
        configure();
    }

    public static ProteinSeedEncoder getInstance() {
        if( INSTANCE == null ) {
            synchronized (ProteinSeedEncoder.class) {
                if( INSTANCE == null ) {
                    INSTANCE = new ProteinSeedEncoder();
                }
            }
        }
        return INSTANCE;
    }

    private void configure() {
        byte[] aux = new byte[128];
        // onde X - Unspecified or unknown        
        //String aa = "ABCDEFGHIJKLMNOPQRSTUVWYZ"; 
        //String aa = "ACDEFGHIKLMNPQRSTVWY";
        if(aa == null) {
            aa = "ACDEFGHIKLMNPQRSTVWY";
        }
        final String aaLower = aa.toLowerCase();
        final String aaUpper = aa.toUpperCase();
        valid = new boolean[128];
        for (int i = 0; i < aaUpper.length(); i++) {
            // uppercase
            char ch = aaUpper.charAt(i);
            aux[(byte) ch] = (byte)i;
            valid[(byte) ch] = true;
            // lowercase
            char ch1 = aaLower.charAt(i);
            aux[(byte) ch1] = (byte)i;
            valid[(byte) ch1] = true;
        }
        // stop codon
        valid[(byte)'*'] = true;
        ENCODER = aux;
    }
    
    @Override
    public int getNumberOfSymbols() {
        return aa.length();  // ACDEFGHIKLMNPQRSTVWY
    }

    @Override
    public byte[] getAlphabet() {
        return aa.getBytes();
    }
    
    @Override
    public void setAlphabet(byte[] value) {
        String s = new String(value);
        if( s.trim().length() < 5) {
            throw new RuntimeException("aa encode vazio...");
        }
        aa = new String(value);
        configure();
    }

    @Override
    public byte[] decode(int value, byte len) {
        byte[] res = new byte[len];
        byte[] codes = aa.getBytes();
        if( codes.length > 31 ) {
            throw new RuntimeException("Alphabet contains more than 31 symbols");
        }
        int v = value;
        for (int i = len - 1; i >= 0; i--) {
            res[i] = codes[v & 0x1f];   // 1f = 31 = 1+2+4+8+16 = 31 = 5 bits
            v = v >> 5;
        }
        return res;
    }

    /**
     *
     * @param v
     * @param len
     * @return int - max 6 aminoacids...
     * @TODO: warning with stop-codon symbols (*)
     */
    @Override
    public int encodeInt(byte[] v, byte len) {
        int value = 0;
        if( v == null ) {
            return -1;
        }
        //try {
            for (int i = 0; i < len; i++) {
                value = value << 5 | ENCODER[v[i]];
            }
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
        return value;
    }
    
    @Override
    public boolean isValidSymbols(byte[] values) {
        for( byte v : values) {
            if( !valid[(int)v] ) {
                return false;
            }
        }
        return true;
    }
    

    public static void main(String[] args) {
        // NCBI Genetic codes
        String AAs = "FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG";
        java.util.Set<Character> charSet = new java.util.HashSet<Character>();
        for (int i = 0; i < AAs.length(); i++) {
            Character c = AAs.charAt(i);
            charSet.add(c);
        }
        java.util.List<Character> lista = new java.util.ArrayList<Character>(charSet);
        java.util.Collections.sort(lista);
        System.out.print("Table 11: ");
        for (Character c : lista) {
            System.out.print(c);
        }
        System.out.println("  " + lista.size());
        System.out.println();
        //
        //

        String aa = "ABCDEFGHIJKLMNOPQRSTUVWYZ";
        byte[] aux = aa.getBytes();
        int len = 2;
        ProteinSeedEncoder prot = new ProteinSeedEncoder();
        int erros = 0;
        for (int i = 0; i < aux.length - len; i++) {
            String p = aa.substring(i, i + len);
            byte[] tmp = p.getBytes();
            int cod = prot.encodeInt(tmp, (byte) len);
            byte[] inv = prot.decode(cod, (byte) len);
            String p2 = new String(inv);
            if( !p.equals(p2) ) {
                erros++;
            }
            System.out.printf("%d\t%s\t%d\t%s\n", i, p, cod, p2);
        }
        System.out.printf("Total de errros %d  ",erros);
        System.out.println( prot.isValidSymbols(aa.getBytes()) ? " seq válida" : "seq inválida" );
        //
        // 
        System.out.println("TEste considerando a tabela 11 do NCBI");
        NCBIGeneticCode genCod = new NCBIGeneticCode();
        aa = genCod.getUniqueAminoacids(11);
        aa = aa.replaceAll("\\*", "");
        aux = aa.getBytes();
        erros = 0;
        for (int i = 0; i < aux.length - len; i++) {
            String p = aa.substring(i, i + len);
            byte[] tmp = p.getBytes();
            int cod = prot.encodeInt(tmp, (byte) len);
            byte[] inv = prot.decode(cod, (byte) len);
            String p2 = new String(inv);
            if( !p.equals(p2) ) {
                erros++;
            }
            System.out.printf("%d\t%s\t%d\t%s\n", i, p, cod, p2);
        }
        System.out.printf("Total de errros %d  ",erros);
        System.out.println( prot.isValidSymbols(aa.getBytes()) ? " seq válida" : "seq inválida" );        
        
    }

}