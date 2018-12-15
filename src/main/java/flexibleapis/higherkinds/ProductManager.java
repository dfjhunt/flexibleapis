
package flexibleapis.higherkinds;

import java.util.function.Function;

public class ProductManager {

    //Get the product by Id, modify it with f and save it again
    public <T> HKT<T, Product> updateProduct(ProductRepository<T> repo, String id, Function<Product, Product> f) {
        HKT<T, Product> product = repo.findProduct(repo.createId(id));
        product = repo.modifyProduct(product, f);
        return repo.saveProduct(product);
    }

    //Get the product by Id and increment the sales by delta
    public <T> HKT<T, Product> incrementSales(ProductRepository<T> repo, String id, int delta) {
        return updateProduct(repo, id, p -> new Product(id, p.price, p.sales + delta));
    }

    // Find a product by id and return the price
    public <T> HKT<T, Double> getPrice(ProductRepository<T> repo, String productId) {
        HKT<T, Product> product = repo.findProduct(repo.createId(productId));
        return repo.getPrice(product);
    }

    // Find two products by their ids and return the sum of their prices
    public <T> HKT<T, Double> getTotalPrice(ProductRepository<T> repo, String productId1, String productId2) {
        HKT<T, Double> price1 = getPrice(repo, productId1);
        HKT<T, Double> price2 = getPrice(repo, productId2);
        return repo.sum(price1, price2);
    }

}
