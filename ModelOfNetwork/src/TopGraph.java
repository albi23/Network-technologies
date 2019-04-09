import java.util.ArrayList;

/**
 * Obiect wich presents top of Graph with 2 fields:
 * idTop - number of actual Top
 * idConnectedWith - the number of the vertex with which we are connected
 */

public class TopGraph {

    private int idTop;
    private ArrayList<EdgeGraph> idConnectedWith = new ArrayList<>();

    public TopGraph(int idTop, EdgeGraph edgeGraph) {
        this.idTop = idTop;
        this.idConnectedWith.add(edgeGraph);
    }

    public int getIdTop() {
        return idTop;
    }

    public void setIdTop(int idTop) {
        this.idTop = idTop;
    }

    public void addIdConnectedTop(EdgeGraph edgeGraph) {
        this.idConnectedWith.add(edgeGraph);
    }

    public ArrayList<EdgeGraph> getIdConnectedWith() {
        return idConnectedWith;
    }

    public String printConnectedTop() {

        StringBuilder stringBuilder = new StringBuilder("{");
        for (EdgeGraph edgeGraph : idConnectedWith)
            stringBuilder.append(edgeGraph.getConnectedIdTop() + " " + "[" + edgeGraph.getStableOfConnection() + "],");

        stringBuilder.append("}");
        return stringBuilder.toString();
    }


    public int getIndexOfTop(int toFindTopId) {
        for (int i = 0; i < idConnectedWith.size(); i++)
            if (idConnectedWith.get(i).getConnectedIdTop() == toFindTopId) return i;
        return -1;
    }
}
