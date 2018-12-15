
package flexibleapis.higherkinds.extype;

import flexibleapis.higherkinds.HKT;

public class Id<A> implements HKT<Id.t, A> {
    public static class t {
    }
    
    A value;
    
    public Id(A value){
        this.value = value;
    }
    
    public A get(){
        return value;
    }
    
    public static <A> Id<A> prj(HKT<Id.t, A> a){
        return (Id<A>)a;
    }
    
    public String toString(){
        return "Id("+value+")";
    }
}
