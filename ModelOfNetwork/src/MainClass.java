import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainClass {

    private static final int numberOfExperiments = 1000000;
    private Random rand = new Random();
    private ArrayList<Integer> randomArray;

    public static void main(String[] args) {

        MainClass main2 = new MainClass();
        main2.firstTest();
        main2.secondTest();
        main2.thirdTest();
        main2.fourthTest();
    }

    private void firstTest() {

        /**
         * First test
         */
        ModelOfGraph modelOfGraph = new ModelOfGraph();
        ArrayList<TopGraph> graph = modelOfGraph.getGraphArray();
        int failsTests = 0;
        for (int i = 1; i <= numberOfExperiments; i++) {
            for (TopGraph myGraph : graph) {
                myGraph.getIdConnectedWith().get(0).setStableOfConnection(new MainClass().rand.nextDouble());
                if (myGraph.getIdConnectedWith().get(0).getStableOfConnection() <= 0.05) {
                    failsTests++;
                    break;
                }
            }
        }
        printResult(1,failsTests);

    }

    private void secondTest() {

        /**
         * Test of graph nr1
         * "Jak zmieni się niezawodność tej sieci po dodaniu krawędzi e(1,20) takiej, że h(e(1,20))=0.95"
         */
        ModelOfGraph modelOfGraf2 = new ModelOfGraph();
        modelOfGraf2.addNewConnection(1, 20, 0.95); //
        ArrayList<TopGraph> graph2 = modelOfGraf2.getGraphArray();
        //printStructuresOfGraph(graph2);

        int failsTests = 0;
        int actualBreak;
        for (int i = 1; i <= numberOfExperiments; i++) {
            actualBreak = 0;
            for (TopGraph myGraph : graph2) {
                for (EdgeGraph edge : myGraph.getIdConnectedWith()) {
                    edge.setStableOfConnection(new MainClass().rand.nextDouble());
                    if (edge.getStableOfConnection() <= 0.05) {
                        actualBreak++;
                        if (actualBreak == 2) {
                            failsTests++;
                            break;
                        }
                    }
                }
            }
        }
        printResult(2,failsTests);
  }

    private void thirdTest() {

        /**
         * A jak zmieni się niezawodność tej sieci gdy dodatkowo dodamy jeszcze krawędzie
         * e(1,10) oraz e(5,15) takie, że: h(e(1,10))=0.8, a h(e(5,15))=0.7.
         */
        MainClass mainClass = new MainClass();
        ModelOfGraph modelOfGraph3 = mainClass.generateModelOfGraph3();
        ModelOfGraph temp ;
        int failsTests = 0;

        for (int k = 1; k <= numberOfExperiments; k++) {
            temp = generateModelOfGraph3();
            if (!calculate(modelOfGraph3,temp)) failsTests++;
        }
        printResult(3,failsTests);

    }


    private void fourthTest() {

        /**
         A jak zmieni się niezawodność tej sieci gdy dodatkowo dodamy jeszcze 4 krawedzie pomiedzy losowymi wierzchołkami o h=0.4.
         */
        this.generateRandomConnectionsArray();
        ModelOfGraph modelOfGraph4 = generateModelOfGraph4(this.getRandNumbers());
        ModelOfGraph temp;
//        printStructuresOfGraph(modelOfGraph4.getGraphArray());
        int failsTests = 0;
        for (int k = 1; k <= numberOfExperiments; k++) {
            temp = generateModelOfGraph4(this.getRandNumbers());
            if (!calculate(modelOfGraph4,temp)) failsTests++;
        }
        printResult(4,failsTests);

    }

    boolean calculate (ModelOfGraph correct, ModelOfGraph toChange){


        int oppositeTop;
        int indexOfTop;
        TopGraph topGraph;
        for (int i = 0; i < 20; i++) {
            topGraph = toChange.getGraphArray().get(i);                         // iteracja po wierzchołkach
            for (int j = 0; j < topGraph.getIdConnectedWith().size(); j++) {     // iteracja po połączeniach
                if (rand.nextDouble() <= (1 - correct.getGraphArray().get(i).getIdConnectedWith().get(j).getStableOfConnection())) { // próvujemy zerwać połączenie
                    if (correct.getGraphArray().get(i).getIdConnectedWith().get(j).getConnectedIdTop() < topGraph.getIdTop())
                        continue;                                                                                                   // nie sprawdzamy poprzedniego połączenia
                    toChange.getGraphArray().get(i).getIdConnectedWith().get(j).setStableOfConnection(Double.MAX_VALUE);            // jeśli a z b jest zerwane to i b z a
                    oppositeTop = correct.getGraphArray().get(i).getIdConnectedWith().get(j).getConnectedIdTop();
                    indexOfTop = toChange.getGraphArray().get(oppositeTop - 1).getIndexOfTop(topGraph.getIdTop());
                    toChange.getGraphArray().get(oppositeTop - 1).getIdConnectedWith().get(indexOfTop).setStableOfConnection(Double.MAX_VALUE);
                }
            }
        }
        return  (isConsistent(toChange));
    }

    private Boolean isConsistent(ModelOfGraph myGraph) {
        Boolean[] visited = new Boolean[20];
        for (int i = 0; i < 20; i++) visited[i] = false;
        int counterVisited = 0;
        Stack<TopGraph> stack = new Stack<>();
        visited[0] = true;
        TopGraph tempGraph;
        stack.push(myGraph.getGraphArray().get(0));
        while (!stack.isEmpty()) {
            tempGraph = stack.pop();
            counterVisited++;
            for (EdgeGraph edgeGraph : tempGraph.getIdConnectedWith()) {
                if (edgeGraph.getStableOfConnection() != Double.MAX_VALUE) {
                    if(edgeGraph.getConnectedIdTop() == 0) continue;
                    if (visited[edgeGraph.getConnectedIdTop() - 1]) continue;
                    visited[edgeGraph.getConnectedIdTop() - 1] = true;
                    stack.add(myGraph.getGraphArray().get(edgeGraph.getConnectedIdTop() - 1));
                }
            }

        }

        return counterVisited == 20;
    }


    private ModelOfGraph generateUnsignedGraph(ModelOfGraph modelOfGraph) {
        for (int i = 2; i <= 20; i++)
            modelOfGraph.addNewConnection(i, i - 1, modelOfGraph.getGraphArray().get(i - 2).getIdConnectedWith().get(0).getStableOfConnection());
        return modelOfGraph;

    }

    private ModelOfGraph generateModelOfGraph3() {
        MainClass mainClass = new MainClass();
        ModelOfGraph modelOfGraph3 = new ModelOfGraph();
        modelOfGraph3.getGraphArray().add(new TopGraph(20, new EdgeGraph(1, 0.95)));
        modelOfGraph3.addNewConnection(1, 20, 0.95); // state of second point
        modelOfGraph3 = mainClass.generateUnsignedGraph(modelOfGraph3);
        modelOfGraph3.addNewConnection(10, 1, 0.8);
        modelOfGraph3.addNewConnection(1, 10, 0.8);
        modelOfGraph3.addNewConnection(5, 15, 0.7);
        modelOfGraph3.addNewConnection(15, 5, 0.7);
        return modelOfGraph3;
    }

    private ModelOfGraph generateModelOfGraph4(ArrayList<Integer> listOfRandomConnection) {

        MainClass mainClass = new MainClass();
        ModelOfGraph modelOfGraph4 = mainClass.generateModelOfGraph3();
        final double stableOfConnection = 0.4;

        for (int i = 0; i < listOfRandomConnection.size(); i = i + 2) {
            modelOfGraph4.addNewConnection(listOfRandomConnection.get(i), listOfRandomConnection.get(i + 1), stableOfConnection);
            modelOfGraph4.addNewConnection(listOfRandomConnection.get(i + 1), listOfRandomConnection.get(i), stableOfConnection);
        }
        return modelOfGraph4;
    }

    private void generateRandomConnectionsArray() {
        ArrayList<Integer> listOfRandomConnection = new ArrayList<>();
        Random r = new Random();
        int randomConnection;
        while (listOfRandomConnection.size() != 8) {
            randomConnection = r.nextInt(20);
            if (listOfRandomConnection.contains(randomConnection)) continue;
            listOfRandomConnection.add(randomConnection);
        }
        this.randomArray = listOfRandomConnection;
    }
    private  ArrayList<Integer> getRandNumbers(){
        return this.randomArray;
    }

    void printStructuresOfGraph(ArrayList<TopGraph> graph) {
        for (TopGraph a : graph) {
            System.out.println("Number top:            " + a.getIdTop());
            System.out.println("Connection with:       " + a.printConnectedTop());
            System.out.println();
        }
    }

    private void printResult(int nrTest, int fail){
        System.out.println("[Test "+nrTest+"]  Number of tests :     " + numberOfExperiments );
        System.out.println("[Test "+nrTest+"]  Number of fail tests: " + fail);
        System.out.println("[Test "+nrTest+"]  Difference :          " + (numberOfExperiments - fail));
        System.out.println("[Test "+nrTest+"]  Ratio                 " + ((double) (numberOfExperiments - fail) / (double) numberOfExperiments));
        System.out.println();

    }
}
