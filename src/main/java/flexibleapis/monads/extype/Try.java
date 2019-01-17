
package flexibleapis.monads.extype;

import java.util.function.Function;

import flexibleapis.monads.Monad;

public class Try<A> implements Monad<Try.t, A> {
    public static class t {
    }

    public Exception ex;

    public A value;

    public boolean isException = false;

    private Try(Exception ex, A value, boolean isException) {
        this.ex = ex;
        this.value = value;
        this.isException = isException;
    }

    // Return a Try value that isn't an exception
    public static <A> Try<A> success(A value) {
        return new Try<>(null, value, false);
    }

    // Return a Try value that represents an exception
    public static <A> Try<A> failure(Exception ex) {
        return new Try<>(ex, null, true);
    }

    // add getters, setters, etc. here

    public static <A> Try<A> prj(Monad<Try.t, A> hktTry) {
        return (Try<A>) hktTry;
    }

    @Override
    public <B> Monad<t, B> map(Function<A, B> f) {
        if(isException){
            return failure(ex);
        }else{
            return success(f.apply(value));
        }
    }

    @Override
    public <B> Monad<t, B> flatMap(Function<A, Monad<t, B>> f) {
        if(isException){
            return failure(ex);
        }else{
            return f.apply(value);
        }
    }
    
    public String toString() {
        if (!isException) {
            return "SUCCESS(" + value + ")";
        } else {
            return "FAILURE(" + ex + ")";
        }
    }
}
