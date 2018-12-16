
package flexibleapis.higherkinds;

import java.util.concurrent.CompletableFuture;

import flexibleapis.higherkinds.extype.FutureM;
import flexibleapis.higherkinds.extype.Id;
import flexibleapis.higherkinds.extype.Try;
import flexibleapis.higherkinds.repo.AsyncProductRepository;
import flexibleapis.higherkinds.repo.BasicProductRepository;
import flexibleapis.higherkinds.repo.JDBCProductRepository;

public class Demo {

    public static void main(String args[]) {
        ProductManager productMgr = new ProductManager();

        Product product = new Product("1234", 10.5, 50);
        Product product2 = new Product("5678", 6.25, 35);

        // ********Basic Product Repository
        BasicProductRepository bpr = new BasicProductRepository();
        bpr.saveProduct(bpr.create(product));
        bpr.saveProduct(bpr.create(product2));
        Id<Product> p = Id.prj(productMgr.incrementSales(bpr, "1234", 15));
        Id<Double> total = Id.prj(productMgr.getTotalPrice(bpr, "1234", "5678"));
        System.out.println("Basic Repository");
        System.out.println(p);
        System.out.println(total + "\n");

        // ********Async Product Repository
        AsyncProductRepository apr = new AsyncProductRepository();
        CompletableFuture<Product> cfp = FutureM.prj(apr.saveProduct(apr.create(product))).future;
        CompletableFuture<Product> cfp2 = FutureM.prj(apr.saveProduct(apr.create(product2))).future;

        //These are fairly complicated because we're composing them asynchronously to make sure the products are
        //saved before we work with them.
        CompletableFuture<Product> futureResult = cfp.thenCompose(x -> FutureM.prj(productMgr.incrementSales(apr, "1234", 15)).future);
        CompletableFuture<Double> futureTotal =
            cfp.thenCompose(p1 -> cfp2.thenCompose(p2 -> FutureM.prj(productMgr.getTotalPrice(apr, "1234", "5678")).future));

        try {
            System.out.println("Async Repository");
            System.out.println(futureResult.get());
            System.out.println(futureTotal.get() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // **********JDBC Product Repository
        JDBCProductRepository jpr = new JDBCProductRepository();
        Try<Product> tp = Try.prj(jpr.saveProduct(jpr.create(product)));
        Try<Product> tp2 = Try.prj(jpr.saveProduct(jpr.create(product2)));
        if ((!tp.isException) && (!tp2.isException)) {
            Try<Product> incrementedProductTry = Try.prj(productMgr.incrementSales(jpr, "1234", 15));
            Try<Double> totalPriceTry = Try.prj(productMgr.getTotalPrice(jpr, "1234", "5678"));
            System.out.println("JDBC Repository");
            System.out.println(incrementedProductTry);
            System.out.println(totalPriceTry);
        }

    }
}
