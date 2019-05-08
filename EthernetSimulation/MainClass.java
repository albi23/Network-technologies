import java.time.Duration;
import java.util.Random;

public class MainClass {

    private static final int ETHERNET_LENGTH = 80;
    private static final int TRANSMITTING_ANTENNAS = 2;
    private static final int ETHERNET_SPEED = 100;
    private static final int RANGE_OF_SLEEP = 8;
    private static final Random generator = new Random();
    private static volatile String[] ethernetWire = new String[ETHERNET_LENGTH];
    private static int conflictCounter = 0;
    private boolean cleared = true;

    public static void main(String[] args) {

        MainClass main = new MainClass();
        ethernetWire = main.makeClearWire(ethernetWire);
        main.runAntennas();
    }

    private void runAntennas() {

        Thread[] antenna = new Thread[TRANSMITTING_ANTENNAS];

        for (int i = 0; i < TRANSMITTING_ANTENNAS; i++) {
            final int idAntenna = i + 1;
            antenna[i] = new Thread(() -> {
                int positionOfTransmission = generator.nextInt(ETHERNET_LENGTH);
                int leftSource, rightSource, messageLength, sleepTime;
                boolean conflict = false;
                while (true) {
                    try {
                        sleepTime = generator.nextInt(RANGE_OF_SLEEP) * 1000;
                        Thread.sleep(Duration.ofMillis(sleepTime).toMillis()); } catch (InterruptedException ex) { ex.printStackTrace(); }

                    if (ethernetWire[positionOfTransmission].equals("_")) {

                        rightSource = leftSource = positionOfTransmission;
                        messageLength = 0;
                        while (messageLength < ETHERNET_LENGTH) {
                            startTransmit(leftSource--, rightSource++, idAntenna);
                            try { Thread.sleep(Duration.ofMillis(ETHERNET_SPEED).toMillis()); } catch (InterruptedException ex) { ex.printStackTrace(); }
                            messageLength++;
                            printStatusOfConnection();
                            if (!(ethernetWire[positionOfTransmission].equals("" + idAntenna))) {
                                conflict = true;
                                conflictCounter++;
                                break;
                            }

                        }
                        if (messageLength == ETHERNET_LENGTH) {
                            conflict = false;
                            makeClearWire(ethernetWire);
                            conflictCounter = 0;
                        }
                        if (conflict) {
                            for (int j = 0; j < ETHERNET_LENGTH; j++) {
                                startTransmit(leftSource--, rightSource++, idAntenna);
                                try { Thread.sleep(Duration.ofMillis(ETHERNET_SPEED).toMillis()); } catch (InterruptedException ex) { ex.printStackTrace(); }
                                printStatusOfConnection();
                            }
                            resolveConflict(antenna[0], antenna[1]);
                        }
                        if (cleared) makeClearWire(ethernetWire);
                        this.cleared = !cleared;
                    }
                }
            });
            runAntenna(antenna[i]);
        }
    }


    private static void resolveConflict(Thread one, Thread two) {

        int first = 0, second = 0;
        if (conflictCounter < 10) {
            first = generator.nextInt((int) Math.pow(2.0, (double) conflictCounter)) * 1000;
            second = generator.nextInt((int) Math.pow(2.0, (double) conflictCounter)) * 1000;

        } else if (conflictCounter < 17) {

            first = generator.nextInt((int) Math.pow(2.0, (double) 10)) * 1000;
            second = generator.nextInt((int) Math.pow(2.0, (double) 10)) * 1000;

        } else {
            System.err.println("Incorrect settings of network");
            System.exit(1);
        }
        try {
            one.sleep(Duration.ofMillis(first).toMillis());
            two.sleep(Duration.ofMillis(second).toMillis());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    private void runAntenna(Thread antenna) {
        antenna.start();
    }

    private synchronized void startTransmit(int leftPropagation, int rightPropagation, int idAntena) {

        if (leftPropagation >= 0)
            ethernetWire[leftPropagation] = (ethernetWire[leftPropagation].equals("_")) ? Integer.toString(idAntena) : "#";

        if (rightPropagation < ETHERNET_LENGTH)
            ethernetWire[rightPropagation] = (ethernetWire[rightPropagation].equals("_") || ethernetWire[rightPropagation].equals("" + idAntena)) ? Integer.toString(idAntena) : "#";
    }

    private String[] makeClearWire(String[] tab) {

        for (int i = 0; i < tab.length; i++)
            tab[i] = "_";

        return tab;
    }

    private synchronized void printStatusOfConnection() {
        for (int i = 0; i < ETHERNET_LENGTH; i++)
            System.out.print("\u001b[32m"+ethernetWire[i]+"\u001B[0m" + "\u001b[31m"+"|"+"\u001B[0m");

        System.out.println();
    }

}
