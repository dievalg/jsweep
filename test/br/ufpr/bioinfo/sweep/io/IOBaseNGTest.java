package br.ufpr.bioinfo.sweep.io;

import br.ufpr.bioinfo.commons.utils.ArraysUtils;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import java.io.File;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author User
 */
public class IOBaseNGTest {

    private String outputPrefix = "sweep_base";

    public IOBaseNGTest() {
    }

    private SweepBase createBase() {
        SweepBase base = new SweepBase();
        base.setSpaceSize(3);
        base.setProjectionSize(2);
        double[][] vals = new double[][]{
            {1., 2.}, {3., 4.}, {5., 6.}
        };
        base.setBase(vals);
        //ArraysUtils.show(vals, " ");
        return base;
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of save method, of class IOBase.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        File filename = new File(outputPrefix + ".bswp");
        SweepBase base = createBase();
        IOBase.save(filename, base);

        SweepBase lida = IOBase.load(filename);
        if (lida == null) {
            fail("load fail in save test");
        } else {
            assertEquals(lida.equals(base), true);            
        }
    }

    /**
     * Test of exportText method, of class IOBase.
     */
    @Test
    public void testExportText() {
        System.out.println("exportText");
        File filename = new File(outputPrefix + ".txt");
        SweepBase base = createBase();
        IOBase.exportText(filename, base);
        
        SweepBase lida = IOBase.importText(filename);
        if (lida == null) {
            fail("load fail in save test");
        } else {
            ArraysUtils.show(lida.getBase(), " ");
            assertEquals(lida.equals(base), true);            
        }
    }

    /**
     * Test of exportJSON method, of class IOBase.
     */
    @Test
    public void testExportJSON() {
        System.out.println("exportJSON");
        File filename = new File(outputPrefix + ".json");
        SweepBase base = createBase();
        IOBase.exportJSON(filename, base);
        assertEquals(true, true);
    }

}
