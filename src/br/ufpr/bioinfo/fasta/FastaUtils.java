package br.ufpr.bioinfo.fasta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dieval Guizelini
 */
public class FastaUtils {
    
    public static List<Fasta> read(String arq) {
        return read(new java.io.File(arq) );
    }
    
    public static List<Fasta> read(java.io.File arq) {
        List<Fasta> result = null;
        FastaReader in = null;
        try {
            in = new FastaReader(arq, 32768);
            // nextAll retorna um LinkedList
            List<Fasta> aux = in.nextAll();
            result = new ArrayList<Fasta>(aux.size());
            result.addAll(aux);
        } catch (IOException ex) {
            Logger.getLogger(FastaUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Duvida, incluir ou não no FileUtils, fazendo com que ele dependa 
            // do FastaReader ou transformar o FastaReader em uma especialização
            // Reader
            try {
                if( in != null ) {
                    in.close();
                }
            } catch(Exception e) {}
        }
        return result;
    }
    
    
}
