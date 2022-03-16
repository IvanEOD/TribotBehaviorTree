# TribotBehaviorTree
Tribot Behavior Tree


# Creating a new Behavior Tree

- A root node is required
```java
private BehaviorTree tree = BehaviorTree.builder()
              .root(walkToGrandExchange)
              .build();

```
- You do not have to create your own context for the tree, but if you want to this is how. Creating your own makes ```getScriptName()``` available in the context.
```java
private ScriptContext context = new ScriptContext("My Script Name");
private BehaviorTree tree = BehaviorTree.builder()
              .root(walkToGrandExchange)
              .build();
// or
private BehaviorTree tree = BehaviorTree.builder()
              .context(new ScriptContext("My Script Name"))
              .root(walkToGrandExchange)
              .build();
```
- You can create a behavior tree with a default Selector or Sequence root node.
```java

private BehaviorTree sequenceTree = BehaviorTree.sequence()
              .context(context)
              .node(walkToGrandExchange)
              .build();
              
private BehaviorTree selectorTree = BehaviorTree.selector()
              .context(context)
              .node(walkToGrandExchange)
              .build();
```
- You can add nodes to Sequence and Selector trees individually or all at once.
```java
private BehaviorTree sequenceTree = BehaviorTree.sequence()
              .context(context)
              .node(walkToGrandExchange)
              .node(walkToGrandExchange)
              .node(walkToGrandExchange)
              .node(walkToGrandExchange)
              .build();
              
private BehaviorTree selectorTree = BehaviorTree.sequence()
              .context(context)
              .nodes(walkToGrandExchange, walkToGrandExchange, walkToGrandExchange, walkToGrandExchange)
              .build();
```



<details><summary>Context</summary>
<p>
  
### Creating a context:
  
```java
private ScriptContext context = new ScriptContext("Script Name");
```

- Setting variables
  - ```setVariable(String key, Object value)```
  
- Getting variables
  - ```getVariable(String key)```
  - ```getVariable(String key, VariableType defaultValue)```
  
- Get all the variables
  - ```getVariables()```
  
- Checking if a variable exists
  - ```variableExists(String key)```
  
- Get script name
  - ```getScriptName()```
  
- Get start time
  - ```getStartTime()```
  
- Set quitting
  - ```setQuitting(boolean value)```
  
- Check quitting
  - ```isQuitting()```
  
</p>
</details>


<details><summary>Leaf Node</summary>
<p>

## Creating a new LeafNode:

##### As a class:
```java
public class WalkToGrandExchange extends LeafNode {
      @Override
    public Status execute() {
        if ( isAtGrandExchange() ) return executeSuccess();
        return evaluateSuccess(this::goToGrandExchange, true);
    }

    @Override
    public String getName() {
        return "Walk to Grand Exchange;
    }
}
```
##### As a local field:  
```java
private LeafNode walkToGrandExchange = new LeafNode() {
    @Override
    public Status execute() {
        if ( isAtGrandExchange() ) return executeSuccess();
        return evaluateSuccess(this::goToGrandExchange, true);
    }

    @Override
    public String getName() {
        return "Walk to Grand Exchange;
    }
};
```
The execute method must return a Status, there are a few different status but most common are SUCCESS and FAILED.
There are some premade returns, but some you will have to create your own or just return the status.


- ```executeSuccess() = Status.SUCCEEDED```

- ```executeFail() = Status.FAILED```
- ```executeFail("message") = Status.FAILED and logs the fail message```

- ```evaluateSuccess(this::checkSuccess, true) = Status.SUCCEEDED or Status.FAILED```
  - The first parameter is a BooleanSupplier and the second is the desired result. 
  - If the BooleanSupplier returns the desired result it will return ```Status.SUCCEEDED```. If not it will return ```Status.FAILED```.

- ```abortScript("message") = Status.ABORT_SCRIPT and logs the abort message```
  
  
</p>
</details>


<details><summary>Sequence Node</summary>
<p>

## Creating a new Sequence or Selector:

### As a class:
```java
public class CustomSequence extends Sequence {
    public CustomSequence() {
        addChild(walkToGrandExchange);
        addChild(walkToGrandExchange);
        addChild(walkToGrandExchange);
    }
}
```
### As a local field:
```java
private Sequence sequence = new Sequence(walkToGrandExchange);
private Sequence sequence2 = new Sequence(walkToGrandExchange, walkToGrandExchange, walkToGrandExchange);
```
The Selector will run the nodes in order until one returns ```Status.SUCCEEDED```.
The Sequence will run the nodes in order until one returns ```Status.FAILED```.
  
</p>
</details>

 
<details><summary>Script Abort and Child Added Listeners</summary>
<p>
  
### Adding an Abort Listener and Child Added Listener:
  
```java
private BehaviorTree tree = BehaviorTree.sequence()
              .context(context)
              .abortListener((abortingNode, abortMessage) -> Log.error(abortingNode.getName() + " aborted script. " + abortMessage))
              .childListener((addNode, index) -> Log.info(addNode.getName() + " added to node: " + addNode.getParentNode().getName() + " at index: " + index))
              .node(walkToGrandExchange)
              .build();
```

</p>
</details>




