package scripts.framework;

import java.util.List;

public class Sequence extends SingleRunningChildBranch {
    
    public Sequence () {
        super();
    }
    
    public Sequence (List<Node> nodes) {
        super(nodes);
    }
    
    public Sequence (Node... nodes) {
        super(List.of(nodes));
    }
    
    @Override
    public String getName() {
        return "Sequence";
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        super.childSuccess(runningNode);
        if (++currentChildIndex < children.size()) run(); // Run next child
        else success(); // All children processed, return success status
    }
    
    @Override
    public void childFail (Node runningNode) {
        super.childFail(runningNode);
        fail(); // Return failure status when a child says it failed
    }
    
}
