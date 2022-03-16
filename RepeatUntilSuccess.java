package scripts.framework;

public class RepeatUntilSuccess extends LoopDecorator {
    
    public RepeatUntilSuccess () {}
    
    public RepeatUntilSuccess (Node node) {
        super(node);
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        success();
        loop = false;
    }
    
    @Override
    public void childFail (Node runningNode) {
        loop = true;
    }
}