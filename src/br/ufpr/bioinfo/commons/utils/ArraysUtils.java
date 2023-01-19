package br.ufpr.bioinfo.commons.utils;

/**
 * ArraysUtils vários métodos utilitários para manipular vetores.
 * 
 * @author Dieval Guizelini
 */
public final class ArraysUtils {
    
    private ArraysUtils() {}

    public static boolean contain(String[] search, String[] values) {
        for(String s : search) {
            for(String v : values) {
                if( v.equals(s) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static <T> void show(T[][] twoDim, String delim) {
        final int rows = twoDim.length;
        for(int i=0 ; i<rows ; i++ ) {
            final int cols = twoDim[i].length-1;
            for(int j=0 ; j<cols ; j++ ) {
                System.out.print(twoDim[i][j]);
                System.out.print(delim);
            }
            System.out.println(twoDim[i][cols]);
        }
    }
    
    public static void show(double[][] twoDim, String delim) {
        final int rows = twoDim.length;
        for(int i=0 ; i<rows ; i++ ) {
            final int cols = twoDim[i].length-1;
            for(int j=0 ; j<cols ; j++ ) {
                System.out.printf("%.6f",twoDim[i][j]);
                System.out.print(delim);
            }
            System.out.printf("%.6f",twoDim[i][cols]);
            System.out.println();
        }
    }
    
}
