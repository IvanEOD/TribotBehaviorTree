package scripts.framework;


import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.concurrent.atomic.AtomicInteger;

public class Repeater extends LoopDecorator {
    
    public AtomicInteger times;
    
    private int count;
    
    public Repeater () {
        this(null);
    }
    
    public Repeater (Node child) {
        this(new AtomicInteger(-1), child);
    }
    
    public Repeater (AtomicInteger times, Node child) {
        super(child);
        this.times = times;
    }
    
    @Override
    public void start () {
        count = times.get();
    }
    
    @Override
    public boolean condition () {
        return loop && count != 0;
    }
    
    @Override
    public void childSuccess (Node runningNode) {
        if (count > 0) count--;
        if (count == 0) {
            super.childSuccess(runningNode);
            loop = false;
        } else loop = true;
    }
    
    @Override
    public void childFail (Node runningNode) {
        childSuccess(runningNode);
    }
    
}
