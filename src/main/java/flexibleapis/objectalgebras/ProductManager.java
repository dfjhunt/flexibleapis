
package flexibleapis.objectalgebras;

import java.util.function.Function;

public class ProductManager {

    public <T> T updateProduct(ProductRepository<T> repo, String id, Function<Product, Product> f) {
        T product = repo.findProduct(id);
        product = repo.modifyProduct(product, f);
        return repo.saveProduct(product);
    }

    public <T> T incrementSales(ProductRepository<T> repo, String id, int delta) {
        return updateProduct(repo, id, p -> new Product(id, p.sales + delta));
    }
}
