package br.ufpr.bioinfo.fasta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dieval
 */
public class FastaReader {

    // filename
    private java.io.File file;
    // buffer length
    private int defaultCharBufferSize = 32768;
    // number of read reads
    private int numberOfReads = 0;
    // number of read lines
    private int numberOfLines = 0;
    // number of read bytes
    private long bytesRead = 0;
    private java.io.BufferedReader reader = null;
    private String lastBuffer = null;

    public FastaReader(File in) {
        this.file = in;
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(in),
                    defaultCharBufferSize);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FastaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FastaReader(java.io.InputStream is) {
        reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(is), defaultCharBufferSize);
    }

    public FastaReader(File in, int bufferSize) {
        this.file = in;
        try {
            if (bufferSize < 4096) {
                defaultCharBufferSize = 4096;
            } else {
                defaultCharBufferSize = bufferSize;
            }
            reader = new java.io.BufferedReader(new java.io.FileReader(in),
                    defaultCharBufferSize);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FastaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Fasta nextRead() throws IOException {
        Fasta fas = new Fasta();
        StringBuilder sb = new StringBuilder();
        String linha;
        if (lastBuffer != null) {
            linha = lastBuffer;
        } else {
            linha = reader.readLine();
        }
        int EOLlen = System.getProperty("line.separator").length();
        while (linha != null) {
            if (linha.startsWith(">")) {
                if (sb.length() > 0) {
                    fas.setSequence(sb.toString());
                    lastBuffer = linha;
                    numberOfReads++;
                    return fas;
                }
                fas = new Fasta();
                fas.setHeader(linha.substring(1));
                sb = new StringBuilder();
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            } else {
                sb.append(linha);
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            }
            // a leitura da proxima linha pode ser necessario descartar...
            linha = reader.readLine();
        }
        if (sb.length() > 0) {
            fas.setSequence(sb.toString());
            numberOfReads++;
            return fas;
        }
        return null;
    }

    public List<Fasta> nextRead(int count) throws IOException {
        java.util.List<Fasta> result = new java.util.ArrayList<Fasta>();
        Fasta fas = new Fasta();
        StringBuilder sb = new StringBuilder();
        String linha;
        if (lastBuffer != null) {
            linha = lastBuffer;
        } else {
            linha = reader.readLine();
        }
        int EOLlen = System.getProperty("line.separator").length();
        while (linha != null) {
            if (linha.startsWith(">")) {
                if (sb.length() > 0) {
                    fas.setSequence(sb.toString());
                    numberOfReads++;
                    result.add(fas);
                    if (result.size() == count) {
                        sb = new StringBuilder();
                        lastBuffer = linha;
                        break;
                    }
                }
                fas = new Fasta();
                fas.setHeader(linha.substring(1));
                sb = new StringBuilder();
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            } else {
                sb.append(linha);
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            }
        }
        if (sb.length() > 0) {
            fas.setSequence(sb.toString());
            numberOfReads++;
            result.add(fas);
        }
        return result;
    }

    public List<Fasta> nextAll() throws IOException {
        java.util.List<Fasta> result = new java.util.LinkedList<Fasta>();
        Fasta fas = new Fasta();
        StringBuilder sb = new StringBuilder();
        String linha;
        if (lastBuffer != null) {
            linha = lastBuffer;
        } else {
            linha = reader.readLine();
        }
        int EOLlen = System.getProperty("line.separator").length();
        while (linha != null) {
            if (linha.startsWith(">")) {
                if (sb.length() > 0) {
                    fas.setSequence(sb.toString());
                    numberOfReads++;
                    result.add(fas);
                }
                fas = new Fasta();
                fas.setHeader(linha.substring(1));
                sb = new StringBuilder();
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            } else {
                sb.append(linha);
                numberOfLines++;
                bytesRead += linha.length() + EOLlen;
            }
            linha = reader.readLine();
        }
        if (sb.length() > 0) {
            fas.setSequence(sb.toString());
            numberOfReads++;
            result.add(fas);
        }
        return result;
    }

    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * @return the file
     */
    public java.io.File getFile() {
        return file;
    }

    /**
     * @return the numberOfReads
     */
    public int getNumberOfReads() {
        return numberOfReads;
    }

    /**
     * @return the numberOfLines
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * @return the bytesRead
     */
    public long getBytesRead() {
        return bytesRead;
    }

    public long getFileSize() {
        if (this.file == null) {
            return -1;
        }
        return file.length();
    }

}
