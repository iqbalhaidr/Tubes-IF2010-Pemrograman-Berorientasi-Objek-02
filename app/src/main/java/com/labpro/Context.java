package com.labpro;

// TODO: Context dibuat sesaat setelah login (Admin/Kurir)
public class Context {
    private Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    // Somehow ini masih perlu parameter controller?
    public void execute(Object controller) {
        strategy.useController(controller);
    }
}
