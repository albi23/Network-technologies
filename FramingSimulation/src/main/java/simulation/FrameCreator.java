package simulation;

import java.io.*;

public class FrameCreator extends DataClass {

    private BufferedWriter bufferedWriter;

    public FrameCreator() throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(new File(RESULT_FILE_TXT)));
        readFromFile();
    }

    private void readFromFile() {

        int markerCounter = 0, value;

        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_FILE))) {

            while ((value = bufferedReader.read()) != -1) {
                builder.append((char) value);
                markerCounter++;
                if (markerCounter == frameLength) { /** division into frames */
                    markerCounter = 0;
                    createFrames(builder.toString());
                    builder.delete(0, builder.length());
                }
            }

            if (builder.length() > 1)  /** Someone wanted send shorter message **/
                createFrames(builder.toString());

            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * function which contact frame with marker and add control crc result
     */
    private void createFrames(String frame) {

        String crc = calculateCRC(frame);
        String pushedMessage = stretch(frame);
        String resultFrame = marker + pushedMessage+ marker + crc;
        try {
            bufferedWriter.write(resultFrame);
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that pushes out bits in a given sequence
     */
    private String stretch(String frameMessage) {

        String flag = "11111";

        if(frameMessage.contains(flag)){
            String expected = "111110";
            return frameMessage.replaceAll(flag,expected);
        }
        return frameMessage;
    }
}
