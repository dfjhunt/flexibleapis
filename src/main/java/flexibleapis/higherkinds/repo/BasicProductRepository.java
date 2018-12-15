
package flexibleapis.higherkinds.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import flexibleapis.higherkinds.HKT;
import flexibleapis.higherkinds.Product;
import flexibleapis.higherkinds.ProductRepository;
import flexibleapis.higherkinds.extype.Id;
import flexibleapis.higherkinds.extype.Id.t;

public class BasicProductRepository implements ProductRepository<Id.t> {

    Map<String, Product> productStore = new HashMap<>();

    @Override
    public HKT<Id.t, Product> findProduct(HKT<Id.t, String> id) {
        return new Id<>(productStore.get(Id.prj(id).get()));
    }

    @Override
    public HKT<Id.t, Product> saveProduct(HKT<Id.t, Product> idProduct) {
        Product product = Id.prj(idProduct).get();
        productStore.put(product.id, product);
        return new Id<>(product);
    }

    @Override
    public HKT<t, String> getTopSellerId() {
        Optional<Product> max = productStore.values().stream().max((a, b) -> Integer.compare(a.sales, b.sales));
        return new Id<>(max.map(p->p.id).orElse(null));
    }

    @Override
    public HKT<t, Double> getPrice(HKT<t, Product> product) {
        return new Id<>(Id.prj(product).get().price);
    }

    @Override
    public HKT<t, Double> sum(HKT<t, Double> price1, HKT<t, Double> price2) {
        double p1 = Id.prj(price1).get();
        double p2 = Id.prj(price2).get();
        return new Id<>(p1 + p2);
    }

    @Override
    public Id<Product> create(Product product) {
        return new Id<>(product);
    }

    @Override
    public HKT<Id.t, Product> modifyProduct(HKT<Id.t, Product> product, Function<Product, Product> f) {
        return new Id<>(f.apply(Id.prj(product).get()));
    }

    @Override
    public HKT<t, String> createId(String id) {
        return new Id<>(id);
    }
}