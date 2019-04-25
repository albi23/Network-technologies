package simulation;

import java.util.zip.CRC32;

abstract class DataClass {

    static final String TEST_FILE = "testFile.txt";
    static final String RESULT_FILE_TXT = "resultFile.txt";
    static final String DECODED_RESULT_FILE_TXT = "decodedResultFile.txt";
    static final String marker = "01111110";
    static final int messageLength = 64;
    static final int frameLength = 32;
    static final int CRCLength = 32;


    /**
     * Simple calculation of CRC
     */
    String calculateCRC(String message) {
        CRC32 crc = new CRC32();
        crc.update(message.getBytes(), 0, message.getBytes().length);
        return fillWithByte(Long.toBinaryString(crc.getValue()));
    }

    /**
     * Fill in the beginning by 0 to easy recognize where is beginning of CRC
     */
    String fillWithByte(String crc) {
        StringBuilder stringBuilder = new StringBuilder(crc);
        while (stringBuilder.length() < CRCLength) {
            stringBuilder.insert(0, '0');
        }
        return stringBuilder.toString();
    }


}
