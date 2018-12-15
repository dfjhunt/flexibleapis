
package flexibleapis.objectalgebras;

public class Product {
    public String id;

    public int sales;

    public double price;

    public Product(String id, int sales) {
        this.id = id;
        this.sales = sales;
    }

    public String toString() {
        return "Product(id:" + id + ", sales:" + sales + ")";
    }
}
