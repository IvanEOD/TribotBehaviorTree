package scripts.framework;

@Data
public class ScriptContext {
    
    private final String scriptName;
    private final LocalDateTime startTime;
    private final AtomicBoolean quitting = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, Object> variables;

    public ScriptContext(String scriptName) {
        this.scriptName = scriptName;
        startTime = LocalDateTime.now();
        variables = new ConcurrentHashMap<>();
        Globals.get().setScriptName(scriptName);
        Globals.get().setStartTime(startTime);
    }

    public void setVariable(String key, Object value) { variables.put(key, value); }
    public <VariableType> VariableType getVariable(String key, VariableType defaultValue) { return (VariableType) variables.getOrDefault(key, defaultValue); }
    public <VariableType> VariableType getVariable(String key) { if (variables.containsKey(key)) return (VariableType) variables.get(key); else return null; }
    public boolean variableExists(String key) { return variables.containsKey(key); }

    public void setQuitting(boolean value) { quitting.set(value); }
    public boolean isQuitting() { return quitting.get(); }
    
    
    
    
    
}
