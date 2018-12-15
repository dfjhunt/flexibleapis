
package flexibleapis.higherkinds.extype;

import java.util.concurrent.CompletableFuture;

import flexibleapis.higherkinds.HKT;

public class FutureM<A> implements HKT<FutureM.t, A> {
    public static class t {
    }

    public CompletableFuture<A> future;

    public FutureM(CompletableFuture<A> future) {
        this.future = future;
    }

    public static <A> FutureM<A> prj(HKT<FutureM.t, A> f) {
        return ((FutureM<A>) f);
    }
}
