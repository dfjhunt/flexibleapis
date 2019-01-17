
package flexibleapis.monads.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import flexibleapis.monads.Monad;
import flexibleapis.monads.Product;
import flexibleapis.monads.ProductRepository;
import flexibleapis.monads.extype.Try;

public class JDBCProductRepository implements ProductRepository<Try.t> {

    Map<String, Product> productDB = new HashMap<>();

    @Override
    public Try<Product> findProduct(String id) {
        return Try.success(productDB.get(id));
    }

    @Override
    public Monad<Try.t, Product> saveProduct(Product product) {
        productDB.put(product.id, product);
        return Try.success(product);
    }

    @Override
    public Try<String> getTopSellerId() {
        try {
            Optional<Product> max = productDB.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
            return Try.success(max.map(p -> p.id).orElse(null));
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    @Override
    public <A> Try<A> pure(A a) {
        return Try.success(a);
    }
}
