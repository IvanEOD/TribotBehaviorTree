package scripts.framework;

public class RepeatUntilFail extends LoopDecorator {
    
    public RepeatUntilFail () {}
    
    public RepeatUntilFail (Node node) {
        super(node);
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        loop = true;
    }
    
    @Override
    public void childFail (Node runningNode) {
        success();
        loop = false;
    }
}