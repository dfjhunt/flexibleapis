
package flexibleapis.objectalgebras;

public class Try<A> {

    public Exception ex;

    public A value;

    public boolean isException = false;

    private Try(Exception ex, A value, boolean isException) {
        this.ex = ex;
        this.value = value;
        this.isException = isException;
    }

    // Return a Try value that isn't an exception
    public static <A> Try<A> success(A value) {
        return new Try<>(null, value, false);
    }

    // Return a Try value that represents an exception
    public static <A> Try<A> failure(Exception ex) {
        return new Try<>(ex, null, true);
    }

    // add getters, setters, etc. here

    public String toString() {
        if (!isException) {
            return "SUCCESS(" + value + ")";
        } else {
            return "FAILURE(" + ex + ")";
        }
    }
}
