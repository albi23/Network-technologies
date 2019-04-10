import org.jgrapht.GraphPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Random;

public abstract class TestClass {

    static final int numberOfTests = 100000;
    static final double T_max = 0.05;
    private static final int maxThroughput = 150000;
    private static final int packages = 1500;
    private static final int pack = 1;
    private static int sumN;
    private final int sizeMatrix = 10;
    private Random generator = new Random();
    private int[][] MatrixN;
    private int[][] MatrixC = new int[sizeMatrix][sizeMatrix];
    private int[][] MatrixA = new int[sizeMatrix][sizeMatrix];


    /**
     * W konstruktorze uruchomiamy wszystkie niezbędne klasy
     */
     TestClass() {

        generateMatrixN(false);
        //printMatrix(MatrixN, "Matrix N");
        GraphType graph = new GraphType(0.95, 10);
        createMatrixA(graph.getGraph());
        //printMatrix(MatrixA, "Matrix A");
        fillMatrixC(false);
        //print(MatrixC, "Matrix C");
        setSumN();
        runMainTest(graph);
    }


    /**
     * Metoda odpowiedzialna za przeprowadzanie testu
     * @param graphType
     */
    private void runMainTest(GraphType graphType) {

        SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
        ArrayList<DefaultWeightedEdge> edges = new ArrayList<>();
        double edgeWeight;

        for (int i = 1; i <= numberOfTests; i++) {
            graph = graphType.getGraph();
            edges.addAll(graph.edgeSet());
            for (DefaultWeightedEdge edge : edges) {
                edgeWeight = graph.getEdgeWeight(edge);
                if (generator.nextDouble() > edgeWeight) graph.removeEdge(edge);
            }
            if (isCohesive(graph)) {
                createMatrixA(graph);
                if (compareMatrix(MatrixC,MatrixA))increaseData(calculateDelay(graph));
            }
        }
        printResult();
    }


    /**
     * Metoda obliczająca opóźnienie na zadanym grafie
     * @param graph
     * @return
     */
    private double calculateDelay(SimpleWeightedGraph graph) {
        double sum = 0, downSum;
        int edgeSource, edgeTarget;
        for (Object e : graph.edgeSet()) {
            edgeSource = (int) graph.getEdgeSource(e);
            edgeTarget = (int) graph.getEdgeTarget(e);
            downSum = MatrixC[edgeSource - 1][edgeTarget - 1] - MatrixA[edgeSource - 1][edgeTarget - 1];
            if (downSum == 0) {
                return 0;
            }
            sum += MatrixA[edgeSource - 1][edgeTarget - 1] / downSum;
        }
        return sum / sumN;
    }

    /**
     * Metoda porownująca macierze - dziala tylko na tych samych kwadratowych macierzach !
     * @param firstMatrix
     * @param secondMatrix
     */
    boolean compareMatrix(int[][] firstMatrix,int[][] secondMatrix){

        if(firstMatrix.length != secondMatrix.length) throw new NumberFormatException("You can compare only the same demensional matrix!");
        for (int i = 0; i <firstMatrix.length ; i++) {
            for (int j = 0; j < secondMatrix.length ; j++) {
                if(firstMatrix[i][j]/pack < secondMatrix[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Metoda wypełniająca macierz A na podstawie najkrótszych ścierzek z macierzy N
     * @param graph
     */
    private void createMatrixA(SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph) {

        MatrixA = getDefaultMatrix(MatrixA);

        GraphPath<Integer, DefaultWeightedEdge> path;
        ArrayList<Integer> child;
        int tempValue;
        for (int i = 1; i <= sizeMatrix; i++) {
            for (int j = 1; j <= sizeMatrix; j++) {
                if (i == j) continue;                                       // przekątna odrzucamy
                path = DijkstraShortestPath.findPathBetween(graph, i, j);   // Szukamy najkrótszej ścieżki
                child = (ArrayList<Integer>) path.getVertexList();          // lista wierzchołków pomiędzy i,j włącznie
                tempValue = MatrixN[i - 1][j - 1];                          // Pobieramy wartość z macierzy N
                for (int n = 0; n < child.size() - 1; n++) {
                    MatrixA[child.get(n) - 1][child.get(n + 1) - 1] += tempValue;
                    MatrixA[child.get(n + 1) - 1][child.get(n) - 1] += tempValue;
                }
            }
        }
    }

    /**
     * Metoda wypełniajaca zadaną macierz zerami
     *
     * @param matrix
     * @return
     */
    int[][] getDefaultMatrix(int[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    /**
     * Metoda sprawdzająca czy graf jest spójny
     *
     * @param graph
     * @return
     */
    boolean isCohesive(SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph) {
        return new ConnectivityInspector<>(graph).isGraphConnected();
    }

    /**
     * Metoda genetująca macierz N - ważene żeby względem przekątnej macierz była taka sama
     * @param randomizeArray - flaga do randomizacji macierzy
     */
    private void generateMatrixN(boolean randomizeArray) {

        MatrixN = new int[sizeMatrix][sizeMatrix];
        int[] temp = {9, 8, 7, 6, 5, 4, 3, 2, 1}; // statyczna tablica do stałych testów


        if (randomizeArray) {
            Random generator = new Random();
            for (int i = 0; i < sizeMatrix - 1; i++)
                temp[i] = generator.nextInt(10);
        }

        int indextmp;
        for (int k = 0; k < sizeMatrix; k++) {
            MatrixN[k][k] = 0; // przekątna
            indextmp = 0;
            for (int i = k + 1; i < sizeMatrix; i++) {
                //symetryczne wypełnianie macierzy
                MatrixN[k][i] = temp[indextmp];
                MatrixN[i][k] = temp[indextmp];
                indextmp++;
            }
        }
    }

    /**
     * Metoda zliczająca wartosc sumyN
     */
    private void setSumN() {
        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) sumN += MatrixN[i][j];
        }
    }

    /**
     * Metoda wypisująca macierz kwadratową
     * @param matrix
     */
    private void printMatrix(int[][] matrix, String name) {

        System.out.println("\n" + name + "\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (!((matrix[i][j] + "").length() == 3)) {
                    if ((matrix[i][j] + "").length() == 2) {
                        System.out.print("0" + matrix[i][j] + "  ");
                    } else {
                        System.out.print("00" + matrix[i][j] + "  ");
                    }
                } else
                    System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    private void print(int[][] matrix, String name) {

        System.out.println("\n" + name + "\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                    System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    /**
     * Metoda wypełniająca macierz C
     */
    private void fillMatrixC(boolean connectedWithA) {

        if (!connectedWithA) {
            for (int i = 0; i < sizeMatrix; i++) {
                for (int j = 0; j < sizeMatrix; j++) {
                    if (i != j) MatrixC[i][j] = maxThroughput;
                }
            }
        }
        else {
            for (int i = 0; i < sizeMatrix; i++) {
                for (int j = 0; j < sizeMatrix; j++) {
                    if (MatrixA[i][j] == 0 && i != j) {
                        MatrixC[i][j] = 100;
                    } else {
                        MatrixC[i][j] = 2 * MatrixA[i][j];
                    }
                }
            }
        }
    }

    public abstract void increaseData(double t);
    public abstract void printResult();
}
