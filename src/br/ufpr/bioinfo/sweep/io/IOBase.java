package br.ufpr.bioinfo.sweep.io;

import br.ufpr.bioinfo.commons.utils.FileUtils;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IOBase utility class to store and recovery SweebBase data.
 * 
 * @author Dieval Guizelini
 */
public class IOBase {

    /**
     * store SweepBase data in binary file.
     * 
     * @param filename to store data
     * @param base data to be stored.
     */
    public static void save(java.io.File filename, SweepBase base) {
        java.io.DataOutputStream os = null;
        try {
            os = new java.io.DataOutputStream(
                    new java.io.BufferedOutputStream(
                            new java.io.FileOutputStream(filename), 8192));

            BaseRecord rec = new BaseRecord(base);
            byte[] magic = rec.getMagic();
            os.write(magic, 0, magic.length);
            os.writeInt(rec.getVersion());
            os.writeInt(rec.getApp());
            os.writeLong(rec.getData());
            final int rows = rec.getSizeSpace();
            os.writeInt(rows);
            final int cols = rec.getProjectionSize();
            os.writeInt(cols);
            os.flush();
            double[][] mat = rec.getValues();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    os.writeDouble(mat[i][j]);
                }
            }
            os.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileUtils.close(os);
        }

    }

    public static SweepBase load(java.io.File filename) {
        java.io.DataInputStream is = null;
        SweepBase result = null;
        try {
            is = new java.io.DataInputStream(
                    new java.io.BufferedInputStream(
                            new java.io.FileInputStream(filename), 8192));

            SweepBase aux = new SweepBase(31, 13);
            BaseRecord rec = new BaseRecord(aux);
            byte[] magic1 = rec.getMagic();
            byte[] magic = new byte[magic1.length];
            is.read(magic, 0, magic.length);
            rec.setVersion( is.readInt() );
            rec.setApp( is.readInt() );
            rec.setData(is.readLong());
            rec.setSizeSpace( is.readInt() );
            rec.setProjectionSize( is.readInt() );
            final int rows = rec.getSizeSpace();
            final int cols = rec.getProjectionSize();
            double[][] mat = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    mat[i][j] = is.readDouble();
                }
            }
            aux.setBaseFilename(filename);
            aux.setSpaceSize(rows);
            aux.setProjectionSize(cols);
            aux.setBase(mat);
            result = aux;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileUtils.close(is);
        }
        return result;
    }

    public static void exportText(File baseFilename, SweepBase base) {
        java.io.PrintWriter out = null;
        try {
            out = new java.io.PrintWriter(baseFilename);
            final int rows2 = base.getSpaceSize();
            final int cols2 = base.getProjectionSize();
            final double[][] mat = base.getBase();
            if (mat != null) {
                final int rows = mat.length;
                if (rows != rows2 || mat[0].length != cols2) {
                    System.out.printf("WARN: SweepBase inconsistent dimensions rows=(%d,%d) cols=(%d,%d)",
                            rows2, rows, cols2, mat[0].length);
                    out.printf("WARN: SweepBase inconsistent dimensions rows=(%d,%d) cols=(%d,%d)",
                            rows2, rows, cols2, mat[0].length);
                    out.println();
                }
                for (int i = 0; i < rows; i++) {
                    final int cols = mat[i].length - 1;
                    for (int j = 0; j < cols; j++) {
                        out.printf("%.8f\t", mat[i][j]);
                    }
                    out.printf("%.8f", mat[i][cols]);
                    out.println();
                }
            }
            out.flush();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileUtils.close(out);
        }
    }
    
    public static SweepBase importText(File filename) {
        java.io.BufferedReader in = null;
        SweepBase result = null;
        try {
            in = new java.io.BufferedReader(new java.io.FileReader(filename),32768);
            String line = in.readLine();
            if( line != null && line.startsWith("WARN") ) {
                // linha a ser ignorada.
                line = in.readLine();
            }
            java.util.List<String> linesList = new java.util.LinkedList<String>();
            while( line != null ) {
                linesList.add(line);
                line = in.readLine();
            }
            if( !linesList.isEmpty() ) {
                final int rows = linesList.size();
                String[] firstRow = linesList.get(0).split("\t");
                final int cols = firstRow.length;
                final double[][] mat = new double[rows][cols];
                System.out.printf("criando com %d linhas e %d colunas\n", rows,cols);
                for(int i=0 ; i<rows ; i++) {
                    line = linesList.get(i);
                    //String[] vals = line.split("\t");
                    java.util.Scanner scanner = new java.util.Scanner(line);  
                    for(int j=0 ; j<cols ; j++) {
                        try {
                            //double v = Double.valueOf(vals[j]);
                            //double v = scanner.nextDouble();
                            mat[i][j] = scanner.nextDouble();
                        } catch(Exception e) {
                            mat[i][j] = 0d;
                        }
                    }
                }
                SweepBase aux = new SweepBase();
                aux.setBaseFilename(filename);
                aux.setSpaceSize(rows);
                aux.setProjectionSize(cols);
                aux.setBase(mat);
                result = aux;
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileUtils.close(in);
        }
        return result;
    }

    public static void exportJSON(File baseFilename, SweepBase base) {
        java.io.PrintWriter out = null;
        Locale localeConf = adjustLocale(null);
        try {
            out = new java.io.PrintWriter(baseFilename);
            final int rows2 = base.getSpaceSize();
            final int cols2 = base.getProjectionSize();
            final double[][] mat = base.getBase();

            out.printf("{\"rows\":%d,\n", rows2);
            out.printf("  \"cols\":%d,\n", cols2);
            out.println("  \"mat\": [");
            if (mat != null) {
                final int rows = mat.length;
                if (rows != rows2 || mat[0].length != cols2) {
                    System.out.printf("WARN: SweepBase inconsistent dimensions rows=(%d,%d) cols=(%d,%d)",
                            rows2, rows, cols2, mat[0].length);
                    out.printf("; WARN: SweepBase inconsistent dimensions rows=(%d,%d) cols=(%d,%d)",
                            rows2, rows, cols2, mat[0].length);
                    out.println();
                }
                for (int i = 0; i < rows; i++) {
                    out.print("  [");
                    final int cols = mat[i].length - 1;
                    for (int j = 0; j < cols; j++) {
                        out.printf("%.8f,", mat[i][j]);
                    }
                    out.printf("%.8f", mat[i][cols]);
                    if (i < (rows - 1)) {
                        out.println("],");
                    } else {
                        out.println("]");
                    }
                }
            }
            out.println("]}");
            out.flush();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            adjustLocale(localeConf);
            FileUtils.close(out);
        }
    }
    
    public static Locale adjustLocale(Locale old) {
        Locale atual = Locale.getDefault();
        if( old == null ) {
            Locale.setDefault(new Locale("en", "US"));
        } else {
            Locale.setDefault(old);
        }
        return atual;
    }

}