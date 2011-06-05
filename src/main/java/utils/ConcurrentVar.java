package utils;

public class ConcurrentVar<T> {
    private T var;
    public synchronized T getVar() { return var; }
    public synchronized void setVar(T var) { this.var = var; }
    public ConcurrentVar(T var) {
        super();
        this.var = var;
    }
    public ConcurrentVar() {
        super();
    }
    
}
