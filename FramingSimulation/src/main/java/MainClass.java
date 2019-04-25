import simulation.FrameCreator;
import simulation.FrameDecoder;
import simulation.GeneratorRandomFile;


public class MainClass {

    public static void main(String[] args){

        GeneratorRandomFile generatorRandomFile = new GeneratorRandomFile();
        try { FrameCreator frameCreator = new FrameCreator(); } catch (Exception e) { e.printStackTrace(); }
        FrameDecoder frameDecoder = new FrameDecoder();
    }
}
