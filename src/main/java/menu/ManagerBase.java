package menu;

import java.util.Scanner;

public abstract class ManagerBase {
    protected static final Scanner scanner = new Scanner(System.in);

    protected void printHead() {
        System.out.println("\n" +
                "***********************************************************"
                + "\n");
    }

    protected void printFooter() {
        System.out.println(
                "**********************************************************"
                        + "\n");
    }

    protected void printBitteWahlnummerWaehlenFooter() {
        System.out.println(
                "***************BITTE EINE WAHLNUMMER EINGEBEN***************"
                        + "\n");
    }
}