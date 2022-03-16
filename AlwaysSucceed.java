package scripts.framework;

public class AlwaysSucceed extends Decorator {
    
    public AlwaysSucceed() {}
    
    public AlwaysSucceed(Node node) {
        super(node);
    }
    
    @Override
    public String getName() {
        return "Always Succeed";
    }
    
    @Override
    public void childFail (Node runningNode) {
        childSuccess(runningNode);
    }
    
}

