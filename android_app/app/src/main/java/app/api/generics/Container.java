package app.api.generics;

public class Container<T> {

    private T t;

    private final Object lock;

    public Container(){
        t = null;
        lock = new Object();
    }

    public synchronized void set(T newT){
        this.t = newT;

        synchronized (lock){
            lock.notify();
        }
    }

    public synchronized T get(){
        return this.t;
    }

    public synchronized void waitTillValue(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.notify();
            }
        }
    }
}
