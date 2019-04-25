package simulation;

import java.io.*;

public class FrameDecoder extends DataClass {

    private BufferedWriter bufferedWriter;

    public FrameDecoder() {
        try { bufferedWriter = new BufferedWriter(new FileWriter(new File(DECODED_RESULT_FILE_TXT))); } catch (IOException e) {
            e.printStackTrace();
        }
        readCodedFile();
    }

    /**
     * Function which read from file, find marker, and send pushed message and crc
     */
    private void readCodedFile() {

        StringBuilder receivedBits = new StringBuilder();
        StringBuilder receivedCRC = new StringBuilder();
        int value, counterBitFlag = 0;
        String pushedMessage;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RESULT_FILE_TXT))) {
            while ((value = bufferedReader.read()) != -1) {

                receivedBits.append((char) value);
                if (receivedBits.length() >= 8 && receivedBits.toString().substring(receivedBits.length() - 8).equals(marker)) {
                    counterBitFlag++;        // we find marker
                }
                if (counterBitFlag == 2) {   // we find closing marker
                    pushedMessage = receivedBits.toString().substring(8, receivedBits.length() - 8);  // message without markers
                    for (int i = 1; i <= CRCLength; i++) {
                        receivedCRC.append((char) (bufferedReader.read()));               // get next 32 bits => crc
                    }
                    writeToFile(pushedMessage, receivedCRC.toString());
                    receivedBits.delete(0, receivedBits.length()); // Clear builders
                    receivedCRC.delete(0, receivedCRC.length());
                    counterBitFlag = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Function which save to file encoded message
     * @param message - message with extra bits
     * @param CRC - control crc
     */
    private void writeToFile(String message, String CRC){

        message = messageWithoutExtraBits(message);
        String newCRC = calculateCRC(message);

        try {
            if (!CRC.equals(newCRC)) message = "Please repeat.";
            bufferedWriter.write(message);
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Function which encode pushed message **/
    private String messageWithoutExtraBits(String message){

        String marker = "111110";
        String expected = "11111";

        if (message.contains(marker))
            message = message.replaceAll(marker,expected);

        return message;
    }
}
