package br.ufpr.bioinfo.sweep.core;

import br.ufpr.bioinfo.commons.utils.ElapsedTime;
import br.ufpr.bioinfo.sweep.core.math.SingularValueDecomposition;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SweepBaseProjection calculate orthogonal base
 *
 *
 * @author Dieval Guizelini
 */
public class SweepBaseProjection {

    public SweepBaseProjection() {
    }

    public SweepBase createRandomweepBase(int rows, int cols) {
        SweepBase res = new SweepBase();
        res.setSpaceSize(rows);
        res.setProjectionSize(cols);
        double[][] base = createRandomBase(rows, cols);
        double[][] ort = createOrthogonalBase(base);
        res.setBase( ort );
        base = null;
        System.gc();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(SweepBaseProjection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return( res );
    }

    public double[][] createRandomBase(int rows, int cols) {
        ElapsedTime t0 = new ElapsedTime().start();
        double[][] base = new double[rows][cols];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                base[i][j] = random.nextDouble();
            }
        }
        t0.stop();
        System.out.println(t0.toString());
        return base;
    }
    
    public double[][] createOrthogonalBase(double[][] base) {
        SingularValueDecomposition svd = new SingularValueDecomposition();
        svd.calculate(base);
        double[][] baseOrtonormal = svd.orth();
        return baseOrtonormal;
    }
    
    /*public static void main(String[] args) {
        SweepBaseProjection p = new SweepBaseProjection();
        double[][] mat = p.createRandomBase(160000, 100);
        double[][] orth = p.createOrthogonalBase(mat);
    }*/
}
