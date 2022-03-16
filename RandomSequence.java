package scripts.framework;

import java.util.List;

public class RandomSequence extends Sequence {
    
    public RandomSequence () {
        super();
    }
    
    public RandomSequence (List<Node> nodes) {
        super(nodes);
    }
    
    public RandomSequence (Node... nodes) {
        super(List.of(nodes));
    }
    
    @Override
    public void start () {
        super.start();
        if (randomChildren == null) randomChildren = createRandomChildren();
    }
}