package br.ufpr.bioinfo.sweep.io;

import br.ufpr.bioinfo.commons.utils.FileUtils;
import br.ufpr.bioinfo.fasta.Fasta;
import br.ufpr.bioinfo.fasta.FastaReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dieval Guizelini
 */
public class SequenceLoader {
    
    private SequenceLoader() {}
    
    public static List<Fasta> load(java.io.File arq) {
        FastaReader in = null;
        List<Fasta> result = null;
        try {
            in = new FastaReader(arq, 32768);
            if( in != null ) {
                result = new java.util.ArrayList<Fasta>();
                Fasta aux=null;
                while( (aux=in.nextRead()) != null ) {
                    result.add(aux);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SequenceLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if( in != null ) {
                try {
                    in.close();
                } catch (Exception ex) {
                    Logger.getLogger(SequenceLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
    
}
