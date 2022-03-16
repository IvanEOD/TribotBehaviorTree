package scripts.framework;

import org.tribot.script.sdk.Log;

import java.util.function.BooleanSupplier;

public abstract class Node {
    
    public enum Status {
        FRESH,
        RUNNING,
        FAILED,
        SUCCEEDED,
        CANCELLED,
        ABORT_SCRIPT
    }
    
    protected Status status = Status.FRESH;
    protected Node parentNode;
    protected BehaviorTree tree;
    protected Node guardNode;
    
    public final int addChild(Node child) {
        int index = addChildToNode(child);
        if (tree != null && tree.listeners != null) tree.notifyChildAdded(this, index);
        return index;
    }
    
    public abstract String getName();
    protected abstract int addChildToNode(Node child);
    public abstract int getChildCount();
    public abstract Node getChild(int i);
    
    public ScriptContext getContext() {
        if (tree == null) throw new IllegalStateException("This node has never run.");
        return tree.getContext();
    }
    
    public Node getGuardNode() {
        return guardNode;
    }
    
    public void setGuardNode(Node guardNode) {
        this.guardNode = guardNode;
    }
    
    public final Status getStatus() {
        return status;
    }
    public final Node getParentNode() { return this.parentNode; }
    public final void setParentNode(Node parentNode) {
        tree = parentNode.tree;
        this.parentNode = parentNode;
    }
    
    public boolean checkGuardNode(Node parentNode) {
        if (guardNode == null) return true;
        if (!guardNode.checkGuardNode(parentNode)) return false;
        guardNode.setParentNode(parentNode.tree.guardEvaluator);
        guardNode.start();
        guardNode.run();
        switch (guardNode.getStatus()) {
            case SUCCEEDED:
                return true;
            case FAILED:
                return false;
            default:
                throw new IllegalStateException("Illegal guard status " + guardNode.getStatus() + ". Guard Nodes must either succeed or fail in one step.");
        }
    }
    
    public void start() {}
    public void end() {}
    public abstract void run();
    public final void running() {
        Status previousStatus = status;
        if (previousStatus == Status.ABORT_SCRIPT) { tree.abortScript(); return; }
        status = Status.RUNNING;
        if (tree.listeners != null && tree.listeners.size() > 0) tree.notifyStatusUpdated(this, previousStatus);
        if (parentNode != null) parentNode.childRunning(this, this);
    }
    
    public final void success() {
        Status previousStatus = status;
        if (previousStatus == Status.ABORT_SCRIPT) { tree.abortScript(); return; }
        status = Status.SUCCEEDED;
        if (tree.listeners != null && tree.listeners.size() > 0) tree.notifyStatusUpdated(this, previousStatus);
        end();
        if (parentNode != null) parentNode.childSuccess(this);
    }
    public final void aborting() { tree.abortScript(); }
    
    public final Status evaluateSuccess(Boolean condition, Boolean successValue) {
        if (condition == successValue) return Status.SUCCEEDED;
        else return Status.FAILED;
    }
    public final Status evaluateSuccess(BooleanSupplier condition, Boolean successValue) {
        if (condition.getAsBoolean() == successValue) return Status.SUCCEEDED;
        else return Status.FAILED;
    }
    public final Status executeSuccess() { return Status.SUCCEEDED; }
    public final Status executeFail() { return Status.FAILED; }
    public final Status executeFail(String message) { Log.error(message); return Status.FAILED; }
    public final Status abortScript(String message) { Log.error("Aborting script because : " + message); return Status.ABORT_SCRIPT; }
    
    public final void fail() {
        Status previousStatus = status;
        status = Status.FAILED;
        if (tree.listeners != null && tree.listeners.size() > 0) tree.notifyStatusUpdated(this, previousStatus);
        end();
        if (parentNode != null) parentNode.childFail(this);
    }
    
    public abstract void childSuccess(Node node);
    public abstract void childFail(Node node);
    public abstract void childRunning(Node runningNode, Node reporterNode);
    public final void cancel() {
        cancelRunningChildren(0);
        Status previousStatus = status;
        status = Status.CANCELLED;
        if (tree.listeners != null && tree.listeners.size() > 0) tree.notifyStatusUpdated(this, previousStatus);
        end();
    }
    
    protected void cancelRunningChildren(int startIndex) {
        for (int index = startIndex, length = getChildCount(); index < length; index++) {
            Node child = getChild(index);
            if (child.status == Status.RUNNING) child.cancel();
        }
    }
    
    public void reset() {
        if (status == Status.RUNNING) cancel();
        for (int index = 0, length = getChildCount(); index < length; index++) getChild(index).reset();
        status = Status.FRESH;
        tree = null;
        parentNode = null;
    }
}
