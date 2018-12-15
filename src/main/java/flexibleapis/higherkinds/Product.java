
package flexibleapis.higherkinds;

public class Product {
    public String id;

    public int sales;

    public double price;

    public Product(String id, double price, int sales) {
        this.id = id;
        this.price = price;
        this.sales = sales;
    }

    public String toString() {
        return "Product(id:" + id + ", price:" + price + ", sales:" + sales + ")";
    }
}
