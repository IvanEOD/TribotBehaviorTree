package scripts.framework;

import org.tribot.script.sdk.Log;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class BehaviorTree extends Node {
    
    public static Integer behaviorTreeCount = 0;
    protected final ConcurrentLinkedDeque<Listener> listeners;
    protected final AtomicBoolean aborting;
    protected Node rootNode;
    protected ScriptContext context;
    protected GuardEvaluator guardEvaluator;
    private Integer treeId;
    
    private BehaviorTree(Builder builder) {
        rootNode = builder.rootNode;
        context = builder.context;
        listeners = builder.listeners;
        tree = this;
        aborting = new AtomicBoolean(false);
        guardEvaluator = new GuardEvaluator(this);
        
        behaviorTreeCount++;
        treeId = behaviorTreeCount;
    }
    
    @Override
    public ScriptContext getContext () { return context; }
    public BehaviorTree setContext (ScriptContext object) { context = object; return this; }
    public Integer getTreeId() { return treeId; }
    public Node getRootNode() { return rootNode; }
    public ConcurrentLinkedDeque<Listener> getListeners() { return listeners; }
    public Boolean isAborting() { return aborting.get(); }
    
    @Override
    protected int addChildToNode (Node child) {
        if (rootNode != null) throw new IllegalStateException("A behavior tree cannot have more than one root node");
        rootNode = child;
        return 0;
    }
    
    public int addChildToRoot(Node child) { return rootNode.addChildToNode(child); }
    
    @Override
    public Node getChild (int i) {
        if (i == 0 && rootNode != null) return rootNode;
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + getChildCount());
    }
    @Override
    public int getChildCount () { return rootNode == null ? 0 : 1; }
    @Override
    public void childRunning (Node runningTask, Node reporter) { running(); }
    @Override
    public void childFail (Node runningTask) { fail(); }
    @Override
    public void childSuccess (Node runningTask) { success(); }
    @Override
    public String getName() {
        return "Behavior Tree";
    }
    @Override
    public void run () { Log.debug("Behavior Tree Run Called"); }
    @Override
    public void reset () { super.reset(); tree = this; }
    
    public void step () {
        if (status == Status.ABORT_SCRIPT) abortScript();
        if (status == Status.FAILED) {  }
        if (rootNode.status == Status.RUNNING) rootNode.run();
        else {
            rootNode.setParentNode(this);
            rootNode.start();
            if (rootNode.checkGuardNode(this)) rootNode.run();
            else rootNode.fail();
        }
    }
    
    public void addListener (Listener listener) { listeners.add(listener); }
    public void removeListener (Listener listener) { if (listeners != null) listeners.remove(listener); }
    public void removeListeners () { if (listeners != null) listeners.clear(); }
    public void notifyStatusUpdated (Node node, Status previousStatus) { if (listeners != null && listeners.size() > 0) for (Listener listener : listeners) listener.statusUpdated(node, previousStatus); }
    public void notifyChildAdded (Node node, int index) { for (Listener listener : listeners) listener.childAdded(node, index); }
    
    public void abortScript() {
    
    }
    
    private static final class GuardEvaluator extends Node {
        @SuppressWarnings("unused")
        public GuardEvaluator () { }
        public GuardEvaluator (BehaviorTree tree) { this.tree = tree; }
        @Override public String getName() {
            return "Default Guard Evaluator";
        }
        @Override protected int addChildToNode (Node child) { return 0; }
        @Override public int getChildCount () { return 0; }
        @Override public Node getChild (int i) { return null; }
        @Override public void run () {}
        @Override public void childSuccess (Node node) {}
        @Override public void childFail (Node node) {}
        @Override public void childRunning (Node runningTask, Node reporterNode) {}
    }
    
    
    
    public static Builder builder() { return new Builder(); }
    public static Builder sequence() { return new Builder().root(new Sequence()); }
    public static Builder sequence(Node... nodes) { return new Builder().root(new Sequence(nodes)); }
    public static Builder selector() { return new Builder().root(new Selector()); }
    public static Builder selector(Node... nodes) { return new Builder().root(new Selector(nodes)); }
    
    public static class Builder {
        private StatusListener statusListener;
        private ChildAddedListener childAddedListener;
        private AbortScriptListener abortScriptListener;
        
        private final ConcurrentLinkedDeque<Listener> listeners;
        private Node rootNode;
        private ScriptContext context;
        private Builder() { listeners = new ConcurrentLinkedDeque<>(); }
        
        public final Builder nodes(Node... nodes) { for (Node node : nodes) rootNode.addChild(node); return this; }
        public Builder node(Node node) { rootNode.addChild(node); return  this; }
        public Builder root(Node rootNode) { this.rootNode = rootNode; return this; }
        public Builder abortListener(AbortScriptListener abortListener) { abortScriptListener = abortListener; return this; }
        public Builder statusListener(StatusListener statusListener) { this.statusListener = statusListener; return this; }
        public Builder childListener(ChildAddedListener childAddedListener) { this.childAddedListener = childAddedListener; return this; }
        public Builder listener(Listener listener) { listeners.add(listener); return this; }
        public Builder context(ScriptContext context) { this.context = context; return this; }
        
        public BehaviorTree build() {
            listeners.add(new Listener(statusListener, childAddedListener, abortScriptListener));
            return new BehaviorTree(this);
        }
        
        
    }
    
    public static class Listener implements StatusListener, ChildAddedListener, AbortScriptListener {
        private final StatusListener statusListener;
        private final ChildAddedListener childAddedListener;
        private final AbortScriptListener abortScriptListener;
        
        public Listener(StatusListener status, ChildAddedListener childAdded, AbortScriptListener abortScript) {
            statusListener = status;
            childAddedListener = childAdded;
            abortScriptListener = abortScript;
        }
        
        @Override
        public void statusUpdated(Node node, Status previousStatus) {
            if (statusListener != null) statusListener.statusUpdated(node, previousStatus);
        }
        
        @Override
        public void childAdded(Node node, int index) {
            if (childAddedListener != null) childAddedListener.childAdded(node, index);
        }
        
        @Override
        public void scriptAborted(Node node, String abortMessage) {
            if (abortScriptListener != null) abortScriptListener.scriptAborted(node, abortMessage);
        }
    }
    
    public interface StatusListener {
        void statusUpdated (Node node, Status previousStatus);
    }
    
    public interface AbortScriptListener {
        void scriptAborted (Node node, String abortMessage);
    }
    
    public interface ChildAddedListener {
        void childAdded (Node node, int index);
    }
    
}



