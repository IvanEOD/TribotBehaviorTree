package scripts.framework;


public class LoopDecorator extends Decorator {
    
    protected boolean loop;
    
    public LoopDecorator() { }
    
    public LoopDecorator(Node child) {
        super(child);
    }
    
    @Override
    public String getName() {
        return "Loop Decorator";
    }
    
    @Override
    public void run() {
        loop = true;
        while (condition()) if (child.status == Status.RUNNING) child.run();
        else {
            child.setParentNode(this);
            child.start();
            if (child.checkGuardNode(this)) child.run();
            else child.fail();
        }
    }
    
    public boolean condition() {
        return loop;
    }
    
    @Override
    public void childRunning(Node runningNode, Node reporterNode) {
        super.childRunning(runningNode, reporterNode);
        loop = false;
    }
    
    
}
