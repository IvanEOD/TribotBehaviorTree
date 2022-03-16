package scripts.framework;

import org.tribot.util.Util;

import java.util.List;

public abstract class SingleRunningChildBranch extends BranchNode {
    
    protected Node runningChild;
    protected int currentChildIndex;
    protected Node[] randomChildren;
    public SingleRunningChildBranch() {
        super();
    }
    public SingleRunningChildBranch(List<Node> nodes) {
        super(nodes);
    }
    
    @Override
    public void childRunning(Node node, Node reporterNode) {
        runningChild = node;
        running();
    }
    
    @Override
    public void childSuccess(Node node) {
        runningChild = null;
    }
    
    @Override
    public void childFail(Node node) {
        runningChild = null;
        
    }
    
    @Override
    public void run() {
        if (runningChild != null) runningChild.run();
        else if (currentChildIndex < children.size()) {
            if (randomChildren != null) {
                int last = children.size() - 1;
                if (currentChildIndex < last) {
                    int otherChildIndex = (int) Math.floor(Math.random() * (last - currentChildIndex + 1) + currentChildIndex);
                    Node temp = randomChildren[currentChildIndex];
                    randomChildren[currentChildIndex] = randomChildren[otherChildIndex];
                    randomChildren[otherChildIndex] = temp;
                }
                runningChild = randomChildren[currentChildIndex];
            } else runningChild = children.get(currentChildIndex);
            runningChild.setParentNode(this);
            runningChild.start();
            if (!runningChild.checkGuardNode(this)) runningChild.fail();
            else run();
        } else {
            
        }
    }
    
    @Override
    public void start() {
        currentChildIndex = 0;
        runningChild = null;
    }
    
    @Override
    protected void cancelRunningChildren(int startIndex) {
        super.cancelRunningChildren(startIndex);
        runningChild = null;
    }
    
    @Override
    public void reset() {
        super.reset();
        currentChildIndex = 0;
        runningChild = null;
        randomChildren = null;
    }
    
    protected Node[] createRandomChildren() {
        Node[] randChildren = new Node[children.size()];
        System.arraycopy(children.toArray(), 0, randChildren, 0, children.size());
        return randChildren;
    }
    
}

