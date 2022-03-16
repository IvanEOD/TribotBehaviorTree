package scripts.framework;



public class RandomSuccess extends Decorator {
    
    public float successRate;
    
    private float p;
    
    public RandomSuccess() {
        this(0.5f);
    }
    
    public RandomSuccess(Node task) {
        this(0.5f, task);
    }
    public RandomSuccess(float successRate) {
        super();
        this.successRate = successRate;
    }
    
    public RandomSuccess(float successRate, Node task) {
        super(task);
        this.successRate = successRate;
    }
    
    @Override
    public String getName() {
        return "RandomSuccess";
    }
    
    @Override
    public void start () {
        p = successRate;
    }
    
    @Override
    public void run () {
        if (child != null) super.run();
        else decide();
    }
    
    @Override
    public void childFail (Node runningNode) {
        decide();
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        decide();
    }
    
    private void decide () {
        if (Math.random() <= p) success();
        else fail();
    }
    
    
}