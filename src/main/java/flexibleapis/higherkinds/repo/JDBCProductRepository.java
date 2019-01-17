
package flexibleapis.higherkinds.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import flexibleapis.higherkinds.HKT;
import flexibleapis.higherkinds.Product;
import flexibleapis.higherkinds.ProductRepository;
import flexibleapis.higherkinds.extype.Try;
import flexibleapis.higherkinds.extype.Try.t;

public class JDBCProductRepository implements ProductRepository<Try.t> {

    Map<String, Product> productDB = new HashMap<>();

    @Override
    public Try<Product> findProduct(HKT<Try.t, String> hktId) {
        Try<String> id = Try.prj(hktId);

        try {
            if (!id.isException) {
                return Try.success(productDB.get(id.value));
            } else {
                return Try.failure(id.ex);
            }
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    @Override
    public HKT<Try.t, Product> saveProduct(HKT<Try.t, Product> hktProduct) {
        Try<Product> product = Try.prj(hktProduct);

        if (product.isException) {
            return Try.failure(product.ex);
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
    public Try<String> getTopSellerId() {
        try {
            Optional<Product> max = productDB.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
            return Try.success(max.map(p->p.id).orElse(null));
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    @Override
    public HKT<t, Double> getPrice(HKT<t, Product> product) {
        Try<Product> prod = Try.prj(product);

        if (prod.isException) {
            return Try.failure(prod.ex);
        } else {
            return Try.success(prod.value.price);
        }
    }

    @Override
    public HKT<t, Double> sum(HKT<t, Double> price1, HKT<t, Double> price2) {
        Try<Double> p1 = Try.prj(price1);
        Try<Double> p2 = Try.prj(price2);

        if (p1.isException) {
            return Try.failure(p1.ex);
        } else if (p2.isException) {
            return Try.failure(p2.ex);
        } else {
            return Try.success(p1.value + p2.value);
        }
    }
    
    public <A,B,C> Try<C> applyBiFunction(HKT<Try.t, A> tryA, HKT<Try.t, B> tryB, BiFunction<A,B,C> f){
        Try<A> p1 = Try.prj(tryA);
        Try<B> p2 = Try.prj(tryB);

        if (p1.isException) {
            return Try.failure(p1.ex);
        } else if (p2.isException) {
            return Try.failure(p2.ex);
        } else {
            return Try.success(f.apply(p1.value, p2.value));
        }
    }

    @Override
    public Try<Product> create(Product product) {
        return Try.success(product);
    }

    @Override
    public Try<Product> modifyProduct(HKT<Try.t, Product> tryA, Function<Product, Product> f) {
        Try<Product> a = Try.prj(tryA);
        if (a.isException) {
            return Try.failure(a.ex);
        } else {
            return Try.success(f.apply(a.value));
        }
    }

    @Override
    public HKT<t, String> createId(String id) {
        return Try.success(id);
    }
}
