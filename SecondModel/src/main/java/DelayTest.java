public class DelayTest extends TestClass {

    private int counter = 0;
    private double sum = 0;

    public DelayTest(){
        super();
    }

    @Override
    public void increaseData(double t) {

        if (t > 0 && t < T_max) {
            this.counter++;
            this.sum += t;
        }
    }

    @Override
    public void printResult() {

        if (counter != 0) {
            System.out.print("Average delay: " + sum / counter + "   " + sum + "/" + counter);
        }
    }
}
