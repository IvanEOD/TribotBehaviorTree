package scripts.framework;

import java.util.List;

public class RandomSelector extends Selector {
    
    public RandomSelector () {
        super();
    }
    
    public RandomSelector (Node... nodes) {
        super(List.of(nodes));
    }
    
    public RandomSelector (List<Node> nodes) {
        super(nodes);
    }
    
    @Override
    public void start () {
        super.start();
        if (randomChildren == null) randomChildren = createRandomChildren();
    }
}