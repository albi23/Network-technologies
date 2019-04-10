
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GraphType   {

    private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
    private  double edgeWeight;
    private  int numberOfTop;

    public GraphType(double edgeWeight, int numberOfTop){

        this.edgeWeight = edgeWeight;
        this.numberOfTop = numberOfTop;
    }

    /**
     * metoda tworzy graf cyrkularny
     */
    private void createGraph() {
        for (int i = 1; i <= numberOfTop; i++) {
            graph.addVertex(i);
            if (i > 1) {
                graph.setEdgeWeight(graph.addEdge(i - 1, i), edgeWeight);
            }
        }
        addMyConfiguration();
    }

    /**
     * Metoda dodaje moją konfigurację wierzchołków
     */
    private void addMyConfiguration(){
        int[][] myConfiguration= {{1,10},{1,6},{2,7},{3,8},{4,9},{5,10}};
        for (int i = 0 ; i < myConfiguration.length; i++){
            graph.setEdgeWeight(graph.addEdge(myConfiguration[i][0], myConfiguration[i][1]), edgeWeight);
        }
    }

    /**
     * metoda Możliwie dodająca nowe połączenia w grafie
     * @param sourceVertex
     * @param targetVertex
     * @param weight
     */
    public void addEdge(int sourceVertex,int targetVertex, double weight){
        graph.setEdgeWeight(graph.addEdge(sourceVertex, targetVertex), weight);

    }

    /**
     * Metoda za każdym razem zwraca nowa instancję grafu
     * @return
     */
    public SimpleWeightedGraph<Integer, DefaultWeightedEdge> getGraph() {
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        createGraph();
        return graph;
    }
}