package scripts.framework;

public class AlwaysFail extends Decorator {
    
    public AlwaysFail () {}
    
    public AlwaysFail (Node node) {
        super(node);
    }
    
    @Override
    public String getName() {
        return "Always Fail";
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        childFail(runningNode);
    }
    
}