
package flexibleapis.objectalgebras;

import java.util.concurrent.CompletableFuture;

import flexibleapis.objectalgebras.repo.AsyncProductRepository;
import flexibleapis.objectalgebras.repo.BasicProductRepository;
import flexibleapis.objectalgebras.repo.JDBCProductRepository;

public class Demo {

    public static void main(String args[]) {
        ProductManager productMgr = new ProductManager();

        Product product = new Product("1234", 50);

        // Basic Product Repository
        BasicProductRepository bpr = new BasicProductRepository();
        bpr.saveProduct(product);
        Product p = productMgr.incrementSales(bpr, "1234", 15);
        System.out.println("Basic Repository");
        System.out.println(p + "\n");

        // Async Product Respository
        AsyncProductRepository apr = new AsyncProductRepository();
        CompletableFuture<Product> cfp = apr.saveProduct(apr.create(product));
        cfp = cfp.thenCompose(x -> productMgr.incrementSales(apr, "1234", 15));
        try {
            System.out.println("Async Repository");
            System.out.println(cfp.get() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // JDBC Product Respository
        JDBCProductRepository jpr = new JDBCProductRepository();
        Try<Product> tp = jpr.saveProduct(jpr.create(product));
        if (!tp.isException) {
            tp = productMgr.incrementSales(jpr, "1234", 15);
            System.out.println("JDBC Repository");
            System.out.println(tp + "\n");
        }

    }
}
