
package flexibleapis.monads.extype;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import flexibleapis.monads.Monad;

public class FutureM<A> implements Monad<FutureM.t, A> {
    public static class t {
    }

    public CompletableFuture<A> future;

    public FutureM(CompletableFuture<A> future) {
        this.future = future;
    }

    public static <A> FutureM<A> prj(Monad<t, A> monad) {
        return ((FutureM<A>) monad);
    }

    @Override
    public <B> Monad<t, B> map(Function<A, B> f) {
        return new FutureM<>(future.thenApply(f));
    }

    @Override 
    public <B,C> Monad<t,C> map2(Monad<t,B> mb, BiFunction<A,B,C> op){
        return new FutureM<>(future.thenCombine(prj(mb).future, op));
    }
    
    @Override
    public <B> Monad<t, B> flatMap(Function<A, Monad<t, B>> f) {
        Function<A,CompletableFuture<B>> f2 = a->prj(f.apply(a)).future;
        return new FutureM<>(future.thenCompose(f2));
    }
}
