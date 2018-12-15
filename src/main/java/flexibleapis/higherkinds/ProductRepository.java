
package flexibleapis.higherkinds;

import java.util.function.Function;

public interface ProductRepository<T> {

    public HKT<T, Product> findProduct(HKT<T, String> id);

    public HKT<T, Product> saveProduct(HKT<T, Product> product);

    public HKT<T, String> getTopSellerId();
    
    public HKT<T, Double> getPrice(HKT<T, Product> product);
    
    public HKT<T, Double> sum(HKT<T, Double> price1, HKT<T, Double> price2);
    
    //*****************************
    
    public HKT<T,Product> create(Product product);

    public HKT<T,Product> modifyProduct(HKT<T,Product> product, Function<Product, Product> f);
    
    public HKT<T, String> createId(String id);
}
