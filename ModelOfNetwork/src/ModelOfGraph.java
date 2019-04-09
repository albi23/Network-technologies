import java.util.ArrayList;

public class ModelOfGraph {

    private ArrayList<TopGraph> graphArray;

    public ModelOfGraph() {
        generateGraph();
    }

    private void generateGraph() {

        final double connectionStrength = 0.95;

        graphArray = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            TopGraph newGraph = new TopGraph(i, new EdgeGraph(i + 1, connectionStrength));
            graphArray.add(newGraph);
        }
    }

    public ArrayList<TopGraph> getGraphArray() {
        return graphArray;
    }

    public void addNewConnection(int nrTop, int newTopConnection, double connectionStrength) {
        for (TopGraph graph : graphArray) {
            if (graph.getIdTop() == nrTop) {
                graph.addIdConnectedTop(new EdgeGraph(newTopConnection, connectionStrength));
                break;
            }
        }
    }
}
