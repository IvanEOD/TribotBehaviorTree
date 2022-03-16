package scripts.framework;

public abstract class Decorator extends Node {
    
    protected Node child;
    
    public Decorator() {}
    public Decorator(Node child) {
        this.child = child;
    }
    
    @Override
    public int addChildToNode(Node child) {
        if (this.child != null) throw new IllegalStateException("A decorator node cannot have more than one child");
        this.child = child;
        return 0;
    }
    
    @Override
    public int getChildCount() {
        return child == null ? 0 : 1;
    }
    
    @Override
    public Node getChild(int i) {
        if (i == 0 && child != null) return child;
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + getChildCount());
    }
    
    @Override
    public void run() {
        if (child.status == Status.RUNNING) child.run();
        else {
            child.setParentNode(this);
            child.start();
            if (child.checkGuardNode(this)) child.run();
            else child.fail();
        }
    }
    
    @Override
    public void childSuccess(Node node) {
        running();
    }
    
    @Override
    public void childFail(Node node) {
        fail();
    }
    
    @Override
    public void childRunning(Node runningNode, Node reporterNode) {
        success();
    }
    
}

