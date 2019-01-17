
package flexibleapis.monads.extype;

import java.util.function.Function;

import flexibleapis.monads.Monad;

public class Id<A> implements Monad<Id.t, A> {
    public static class t {
    }
    
    A value;
    
    public Id(A value){
        this.value = value;
    }
    
    public A get(){
        return value;
    }
    
    public static <A> Id<A> prj(Monad<Id.t, A> a){
        return (Id<A>)a;
    }

    @Override
    public <B> Monad<t, B> map(Function<A, B> f) {
        return new Id<>(f.apply(value));
    }

    @Override
    public <B> Monad<t, B> flatMap(Function<A, Monad<t, B>> f) {
        return f.apply(value);
    }
    
    public String toString(){
        return "Id("+value+")";
    }
}
