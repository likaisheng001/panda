package designmodel.command;

/**
 * Created by Administrator on 2019/8/30.
 */
public class TVOnCommand implements Command{
    TVReceiver tv;

    public TVOnCommand(TVReceiver tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }

    @Override
    public void undo() {
        tv.off();
    }
}
