package scripts.framework;


public abstract class LeafNode extends Node {
        
    public LeafNode() {}
    
    public abstract Status execute();
    
    @Override
    public void run() {
        Status result = execute();
        if (result == null) throw new IllegalStateException("Invalid status 'null' returned by the execute method");
        switch(result) {
            case SUCCEEDED:
                success();
                return;
            case FAILED:
                fail();
                return;
            case RUNNING:
                running();
                return;
            case ABORT_SCRIPT:
            
            default:
                throw new IllegalStateException("Invalid status '" + result.name() + "' returned by the execute method");
        }
    }
    
    @Override
    public int addChildToNode(Node child) {
        throw new IllegalStateException("A leaf node cannot have any children");
    }
    
    @Override
    public int getChildCount() {
        return 0;
    }
    
    @Override
    public Node getChild(int i) {
        throw new IndexOutOfBoundsException("A leaf task can not have any child");
    }
    
    @Override
    public void childSuccess(Node node) {}
    
    @Override
    public void childFail(Node node) {}
    
    @Override
    public void childRunning(Node runningNode, Node reporterNode) {}
}
