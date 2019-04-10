public class ReliabilityTest extends TestClass {


    private int counter = 0;
    private double sum = 0;

    public ReliabilityTest(){
        super();
    }

    @Override
    public void increaseData(double t){

        if (t > 0) {
            this.counter++;
            this.sum += t;
        }
    }

    @Override
    public void printResult(){
        System.out.println("Reliability: " + (double)counter / numberOfTests + "   " + counter + "/" + numberOfTests);
    }
}
