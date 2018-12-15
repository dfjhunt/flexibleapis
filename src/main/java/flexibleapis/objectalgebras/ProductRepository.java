
package flexibleapis.objectalgebras;

import java.util.function.Function;

public interface ProductRepository<T> {
    public T findProduct(String id);
    public T saveProduct(T product);

    public T create(Product product);
    public T modifyProduct(T product, Function<Product, Product> f);
}
