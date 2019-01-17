
package flexibleapis.monads;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Monad<T, A> extends HKT<T, A> {
    public <B> Monad<T, B> map(Function<A, B> f);

    public <B> Monad<T, B> flatMap(Function<A, Monad<T, B>> f);
    
    default public <B, C> Monad<T, C> map2(Monad<T,B> mb, BiFunction<A, B, C> f){
        return flatMap(a->mb.map(b->f.apply(a, b)));
    }
}
