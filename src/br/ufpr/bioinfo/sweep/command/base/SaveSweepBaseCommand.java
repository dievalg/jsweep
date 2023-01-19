package br.ufpr.bioinfo.sweep.command.base;

import br.ufpr.bioinfo.sweep.command.SweepCommand;
import br.ufpr.bioinfo.sweep.core.SweepBase;

/**
 *
 * @author Dieval Guizelini
 */
public class SaveSweepBaseCommand extends SweepCommand {

    private SweepBase base = null;

    public SaveSweepBaseCommand() {
    }

    public void setBase(SweepBase base) {
        this.base = base;
    }

    @Override
    public boolean prepare() {
        if (this.base == null || base.getBase() == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean execute() {
        return false;
    }

    public static void save(SweepBase base) {
        SaveSweepBaseCommand cmd2 = new SaveSweepBaseCommand();
        cmd2.setBase(base);
        if (cmd2.prepare()) {
            cmd2.execute();
        }
    }
}
