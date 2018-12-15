
package flexibleapis.higherkinds.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import flexibleapis.higherkinds.HKT;
import flexibleapis.higherkinds.Product;
import flexibleapis.higherkinds.ProductRepository;
import flexibleapis.higherkinds.extype.FutureM;
import flexibleapis.higherkinds.extype.FutureM.t;

public class AsyncProductRepository implements ProductRepository<FutureM.t> {

    // *************Mock Async Store*************************
    class AsyncStore<K, V> {
        Map<K, V> map = new HashMap<>();

        public CompletableFuture<V> get(K k) {
            return CompletableFuture.completedFuture(map.get(k));
        }

        public CompletableFuture<Void> put(K k, V v) {
            map.put(k, v);
            return CompletableFuture.completedFuture(null);
        }
    }

    // *******************************************************

    AsyncStore<String, Product> asyncProductStore = new AsyncStore<>();

    @Override
    public HKT<FutureM.t, Product> findProduct(HKT<FutureM.t, String> id) {
        CompletableFuture<String> idF = FutureM.prj(id).future;
        return new FutureM<>(idF.thenCompose(asyncProductStore::get));
    }

    @Override
    public HKT<FutureM.t, Product> saveProduct(HKT<FutureM.t, Product> product) {
        CompletableFuture<Product> productF = FutureM.prj(product).future;
        return new FutureM<>(productF.thenCompose(p -> asyncProductStore.put(p.id, p).thenApply(x -> p)));
    }

    @Override
    public HKT<t, String> getTopSellerId() {
        //Cheat here to get the product with most sales
        Optional<Product> max = asyncProductStore.map.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
        return new FutureM<>(CompletableFuture.completedFuture(max.map(p -> p.id).orElse(null)));
    }

    @Override
    public HKT<t, Double> getPrice(HKT<t, Product> product) {
        CompletableFuture<Product> productF = FutureM.prj(product).future;
        return new FutureM<>(productF.thenApply(x -> x.price));
    }

    @Override
    public HKT<t, Double> sum(HKT<t, Double> price1, HKT<t, Double> price2) {
        CompletableFuture<Double> price1F = FutureM.prj(price1).future;
        CompletableFuture<Double> price2F = FutureM.prj(price2).future;
        return new FutureM<>(price1F.thenCombine(price2F, (x, y) -> x + y));
    }

    @Override
    public HKT<FutureM.t, Product> create(Product product) {
        return new FutureM<>(CompletableFuture.completedFuture(product));
    }

    @Override
    public HKT<FutureM.t, Product> modifyProduct(HKT<FutureM.t, Product> product, Function<Product, Product> f) {
        return new FutureM<>(FutureM.prj(product).future.thenApply(f));
    }

    @Override
    public HKT<t, String> createId(String id) {
        return new FutureM<>(CompletableFuture.completedFuture(id));
    }

}
