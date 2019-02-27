package Tablesaw;

import org.junit.Test;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

public class test {

    @Test
    public void main(){
        double[] numbers = {1, 2, 3, 4};
        DoubleColumn nc = DoubleColumn.create("Test", numbers);
        System.out.println(nc.print());

        DoubleColumn nc2 = nc.multiply(4);
        System.out.println(nc2.print());
        System.out.println(nc.isLessThan(4));


    }
}
