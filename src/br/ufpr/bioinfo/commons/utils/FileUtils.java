package br.ufpr.bioinfo.commons.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 *
 * @author Dieval Guizelini
 */
public class FileUtils {

    public static void close(Reader in) {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException ex) {
        }
    }

    public static void close(Writer out) {
        if (out == null) {
            return;
        }
        try {
            out.close();
        } catch (IOException ex) {
        }
    }

    public static void close(java.io.OutputStream out) {
        if (out == null) {
            return;
        }
        try {
            out.close();
        } catch (IOException ex) {
        }
    }

    public static void close(java.io.InputStream in) {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException ex) {
        }
    }

    public static void close(java.io.RandomAccessFile out) {
        if (out == null) {
            return;
        }
        try {
            out.close();
        } catch (IOException ex) {
        }
    }

    public static String extractFilename(String fileName) {
        if (fileName == null) {
            return null;
        }
        int pos = -1;
        if ((pos = fileName.lastIndexOf(".")) > -1) {
            return fileName.substring(0, pos);
        }
        return fileName;
    }

    public static String extractExtFilename(String fileName) {
        if (fileName == null) {
            return null;
        }
        int pos = -1;
        if ((pos = fileName.lastIndexOf(".")) > -1) {
            if (pos + 1 < fileName.length()) {
                return fileName.substring(pos + 1);
            }
            return "";
        }
        return "";
    }
    
    public static String[] separateFilenameExtension(String filename) {
        String[] result = new String[2];
        int pos = -1;
        if ((pos = filename.lastIndexOf(".")) > -1) {
            if (pos + 1 < filename.length()) {
                result[0] = filename.substring(0, pos);
                result[1] = filename.substring(pos+1);
            } else {
                result[0] = filename.substring(0, pos);
                result[1] = "";
            }
        } else {
            result[0] = filename;
            result[1] = "";
        }
        return result;
    }
    
    public static String[] separateFilenameExtension(java.io.File filename) {
        return separateFilenameExtension(filename.getAbsolutePath());
    }
    
    
}
