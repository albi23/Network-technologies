package simulation;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GeneratorRandomFile extends DataClass{

    private final Random generator =  new Random();

    public GeneratorRandomFile(){
        generateTestFile();
    }

    private void generateTestFile(){

        try (PrintWriter writer = new PrintWriter(TEST_FILE, String.valueOf(StandardCharsets.UTF_8))){

            for (int i = 1; i <= messageLength ; i++)
                writer.print(generator.nextInt(2));

        }catch (Exception ex) { ex.printStackTrace(); }
    }
}
