package scripts.framework;

import java.util.List;

public class Selector extends SingleRunningChildBranch {
    
    public Selector () {
        super();
    }
    
    public Selector (Node... nodes) {
        super(List.of(nodes));
    }
    
    public Selector (List<Node> nodes) {
        super(nodes);
    }
    
    @Override
    public void childFail (Node runningNode) {
        super.childFail(runningNode);
        if (++currentChildIndex < children.size()) run(); // Run next child
        else fail(); // All children processed, return failure status
    }
    
    @Override
    public String getName() {
        return "Selector";
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        super.childSuccess(runningNode);
        success(); // Return success status when a child says it succeeded
    }
    
}