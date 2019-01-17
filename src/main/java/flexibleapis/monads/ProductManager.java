
package flexibleapis.monads;

import java.util.function.Function;

public class ProductManager {

    // Get the product by Id, modify it with f and save it again
    public <T> Monad<T, Product> updateProduct(ProductRepository<T> repo, String id, Function<Product, Product> f) {
        Monad<T, Product> modifiedProduct = repo.findProduct(id).map(f);
        return modifiedProduct.flatMap(repo::saveProduct);
    }

    // Get the product by Id and increment the sales by delta
    public <T> Monad<T, Product> incrementSales(ProductRepository<T> repo, String id, int delta) {
        return updateProduct(repo, id, p -> new Product(id, p.price, p.sales + delta));
    }

    // Find a product by id and return the price
    public <T> Monad<T, Double> getPrice(ProductRepository<T> repo, String productId) {
        return repo.findProduct(productId).map(p -> p.price);
    }

    // Find two products by their ids and return the sum of their prices
    public <T> Monad<T, Double> getTotalPrice(ProductRepository<T> repo, String productId1, String productId2) {
        return getPrice(repo, productId1).map2(getPrice(repo, productId2), (x, y) -> x + y);
    }

    // Find the total price of the two products. If it is equal to the money, increment the sales and return true.
    // Otherwise, return false.
    public <T> Monad<T, Boolean> handleSale(ProductRepository<T> repo, String productId1, String productId2, double money) {
        Monad<T, Double> totalPrice = getTotalPrice(repo, productId1, productId2);

        return totalPrice.flatMap(tp -> {
            if (tp == money) {
                return incrementSales(repo, productId1, 1).map2(incrementSales(repo, productId2, 1), (x, y) -> true);
            } else {
                return repo.pure(false);
            }
        });
    }
}
