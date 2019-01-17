
package flexibleapis.monads.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import flexibleapis.monads.Monad;
import flexibleapis.monads.Product;
import flexibleapis.monads.ProductRepository;
import flexibleapis.monads.extype.Id;
import flexibleapis.monads.extype.Id.t;

public class BasicProductRepository implements ProductRepository<Id.t> {

    Map<String, Product> productStore = new HashMap<>();

    @Override
    public Monad<Id.t, Product> findProduct(String id) {
        return new Id<>(productStore.get(id));
    }

    @Override
    public Monad<Id.t, Product> saveProduct(Product product) {
        productStore.put(product.id, product);
        return new Id<>(product);
    }

    @Override
    public Monad<t, String> getTopSellerId() {
        Optional<Product> max = productStore.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
        return new Id<>(max.map(p->p.id).orElse(null));
    }

    @Override
    public <A> Id<A> pure(A a) {
        return new Id<>(a);
    }
}