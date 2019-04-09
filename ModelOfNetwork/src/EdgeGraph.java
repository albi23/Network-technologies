/**
 * Object presents Edge of graph
 */

public class EdgeGraph {

    private int connectedIdTop;
    private  double stableOfConnection;


    public EdgeGraph(int connectedIdTop, double stableOfConnection) {
        this.connectedIdTop = connectedIdTop;
        this.stableOfConnection = stableOfConnection;
    }


    public int getConnectedIdTop() {
        return connectedIdTop;
    }

    public void setConnectedIdTop(int connectedIdTop) {
        this.connectedIdTop = connectedIdTop;
    }

    public double getStableOfConnection() {
        return stableOfConnection;
    }

    public void setStableOfConnection(double stableOfConnection) {
        this.stableOfConnection = stableOfConnection;
    }
}







