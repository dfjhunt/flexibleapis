
package flexibleapis.objectalgebras.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import flexibleapis.objectalgebras.Product;
import flexibleapis.objectalgebras.ProductRepository;

public class BasicProductRepository implements ProductRepository<Product> {

    Map<String, Product> productStore = new HashMap<>();

    @Override
    public Product create(Product product) {
        return product;
    }

    @Override
    public Product findProduct(String id) {
        return productStore.get(id);
    }

    @Override
    public Product saveProduct(Product product) {
        productStore.put(product.id, product);
        return product;
    }

    @Override
    public Product modifyProduct(Product product, Function<Product, Product> f) {
        return f.apply(product);
    }
}