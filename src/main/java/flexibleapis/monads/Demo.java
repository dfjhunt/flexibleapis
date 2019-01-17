
package flexibleapis.monads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import flexibleapis.monads.extype.FutureM;
import flexibleapis.monads.repo.AsyncProductRepository;
import flexibleapis.monads.repo.BasicProductRepository;
import flexibleapis.monads.repo.JDBCProductRepository;

public class Demo {

    public static <T> Monad<T, Void> doDemo(ProductRepository<T> repo, String heading) {
        ProductManager productMgr = new ProductManager();

        Product product1 = new Product("productA", 10.5, 50);
        Product product2 = new Product("productB", 6.25, 35);

        Monad<T, Void> setup = repo.saveProduct(product1).map2(repo.saveProduct(product2), (x, y) -> null);

        // I ultimately want to return a T that "completes" after all four of the test calls below.
        // There are multiple ways to do this, I thought this way was interesting to demonstrate
        // building a monad list.
        Monad<T, List<String>> strings = repo.pure(new ArrayList<>());

        strings = add(strings, setup.flatMap(v -> productMgr.incrementSales(repo, "productA", 15)).map(x -> "product: " + x));
        strings = add(strings, setup.flatMap(v -> productMgr.getTotalPrice(repo, "productA", "productB")).map(x -> "total: " + x));
        strings = add(strings, setup.flatMap(v -> productMgr.handleSale(repo, "productA", "productB", 16.75)).map(x -> "good sale: " + x));
        strings = add(strings, setup.flatMap(v -> productMgr.handleSale(repo, "productA", "productB", 15.75)).map(x -> "bad sale: " + x));

        return strings.map((ls) -> {
            System.out.println(heading);
            for (String str : ls) {
                System.out.println(str);
            }
            System.out.println();
            return null;
        });
    }

    public static <T, A> Monad<T, List<A>> add(Monad<T, List<A>> list, Monad<T, A> ma) {
        return list.map2(ma, (l, a) -> {
            l.add(a);
            return l;
        });
    }

    public static void main(String args[]) {
        doDemo(new BasicProductRepository(), "Basic Repository");

        CompletableFuture<Void> fut = FutureM.prj(doDemo(new AsyncProductRepository(), "Async Repository")).future;
        try {
            // force this to complete before we move on
            fut.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        doDemo(new JDBCProductRepository(), "JDBC Repository");
    }
}
