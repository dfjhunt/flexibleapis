
package flexibleapis.monads.repo;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import flexibleapis.monads.Monad;
import flexibleapis.monads.Product;
import flexibleapis.monads.ProductRepository;
import flexibleapis.monads.extype.FutureM;
import flexibleapis.monads.extype.FutureM.t;

public class AsyncProductRepository implements ProductRepository<FutureM.t> {

    // *************Mock Async Store*************************
    class AsyncStore<K, V> {
        Map<K, V> map = new ConcurrentHashMap<>();

        //Helper method for testing, just simulates a delay to better test asynchronicity
        private <A> CompletableFuture<A> delayedAction(Supplier<A> s) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return s.get();
            });
        }

        public CompletableFuture<V> get(K k) {
            return delayedAction(() -> {
                return map.get(k);
            });
        }

        public CompletableFuture<Void> put(K k, V v) {
            return delayedAction(() -> {
                map.put(k, v);
                return null;
            });
        }
    }

    // *******************************************************

    AsyncStore<String, Product> asyncProductStore = new AsyncStore<>();

    @Override
    public Monad<FutureM.t, Product> findProduct(String id) {
        return new FutureM<>(asyncProductStore.get(id));
    }

    @Override
    public Monad<FutureM.t, Product> saveProduct(Product product) {
        return new FutureM<>(asyncProductStore.put(product.id, product).thenApply(x -> product));
    }

    @Override
    public Monad<t, String> getTopSellerId() {
        // Need to cheat here to get the product with most sales since our store doesn't support it
        Optional<Product> max = asyncProductStore.map.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
        return new FutureM<>(CompletableFuture.completedFuture(max.map(p -> p.id).orElse(null)));
    }

    @Override
    public <A> Monad<t, A> pure(A a) {
        return new FutureM<>(CompletableFuture.completedFuture(a));
    }
}
