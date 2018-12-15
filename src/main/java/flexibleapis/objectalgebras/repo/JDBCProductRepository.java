
package flexibleapis.objectalgebras.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import flexibleapis.objectalgebras.Product;
import flexibleapis.objectalgebras.ProductRepository;
import flexibleapis.objectalgebras.Try;

public class JDBCProductRepository implements ProductRepository<Try<Product>> {

    Map<String, Product> productDB = new HashMap<>();

    @Override
    public Try<Product> create(Product product) {

        return Try.success(product);

    }

    @Override
    public Try<Product> findProduct(String id) {
        try {
            return Try.success(productDB.get(id));
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    @Override
    public Try<Product> saveProduct(Try<Product> product) {
        if (product.isException) {
            return product;
        } else {
            try {
                Product value = product.value;
                productDB.put(value.id, value);
                return Try.success(value);
            } catch (Exception e) {
                return Try.failure(e);
            }
        }
    }

    @Override
    public Try<Product> modifyProduct(Try<Product> product, Function<Product, Product> f) {
        if (product.isException) {
            return product;
        } else {
            Product value = product.value;
            return Try.success(f.apply(value));
        }
    }
}
