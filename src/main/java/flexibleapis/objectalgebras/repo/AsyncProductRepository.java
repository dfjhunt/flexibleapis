
package flexibleapis.objectalgebras.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import flexibleapis.objectalgebras.Product;
import flexibleapis.objectalgebras.ProductRepository;

public class AsyncProductRepository implements ProductRepository<CompletableFuture<Product>> {

    // *************Mock Async Store*************************

    class AsyncStore<K, V> {
        Map<K, V> map = new HashMap<>();

        public CompletableFuture<V> get(K k) {
            return CompletableFuture.completedFuture(map.get(k));
        }

        public CompletableFuture<V> put(K k, V v) {
            map.put(k, v);
            return CompletableFuture.completedFuture(v);
        }
    }
    // ******************************************************

    AsyncStore<String, Product> asyncProductStore = new AsyncStore<>();

    @Override
    public CompletableFuture<Product> findProduct(String id) {
        return asyncProductStore.get(id);
    }

    @Override
    public CompletableFuture<Product> saveProduct(CompletableFuture<Product> product) {
        return product.thenCompose(p -> asyncProductStore.put(p.id, p));
    }

    @Override
    public CompletableFuture<Product> create(Product product) {
        return CompletableFuture.completedFuture(product);
    }

    @Override
    public CompletableFuture<Product> modifyProduct(CompletableFuture<Product> product, Function<Product, Product> f) {
        return product.thenApply(f);
    }
}
