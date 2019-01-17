
package flexibleapis.monads;

public interface ProductRepository<T> {

    public Monad<T, Product> findProduct(String id);

    public Monad<T, Product> saveProduct(Product product);

    public Monad<T, String> getTopSellerId();
    
    //*****************************
    
    public <A> Monad<T,A> pure(A a);

}
