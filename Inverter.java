package scripts.framework;

public class Inverter extends Decorator {
    
    public Inverter () {}
    
    public Inverter (Node node) {
        super(node);
    }
    
    @Override
    public String getName() {
        return "Inverter";
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        super.childFail(runningNode);
    }
    
    @Override
    public void childFail (Node runningNode) {
        super.childSuccess(runningNode);
    }
    
}
