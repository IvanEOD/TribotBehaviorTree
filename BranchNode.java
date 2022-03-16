package scripts.framework;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchNode extends Node {
    
    protected List<Node> children;
    
    public BranchNode() {
        this(new ArrayList<>());
    }
    public BranchNode(Node... nodes) {
        children = List.of(nodes);
    }
    public BranchNode(List<Node> nodes) {
        children = nodes;
    }
    
    @Override
    public int addChildToNode(Node child) {
        children.add(child);
        return children.size() - 1;
    }
    
    @Override
    public int getChildCount() {
        return children.size();
    }
    
    @Override
    public Node getChild(int i) {
        return children.get(i);
    }
    
}

