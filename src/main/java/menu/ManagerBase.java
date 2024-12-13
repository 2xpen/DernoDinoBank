package menu;

import java.sql.Connection;
import java.util.Scanner;

public abstract class ManagerBase {


    /// TODO TODO TODO einheitlich fallbacks wenn ein service nicht funktionert hat,
    ///  sowas wie "wollen sie es nochmla versuchen blabalba
    /// falls nicth etwas anderes als y tippen sowie es in den ganzen managern schon drin ist,
    ///  grade sehr uneihnteilich
    /// , lösung wöre ähblich der printHead oder printFooter mehtode wie hiern in der BaseManager Klasse


    protected static final Scanner scanner = new Scanner(System.in);


    protected void printHead() {

        System.out.println("\n" +
                "***********************************************************"
                + "\n");
    }


    protected void printSpacedHead() {

        System.out.println("\n" +
                "***********************************************************"
                + "\n");
    }

    protected void printFooter() {
        System.out.println(
                "**********************************************************"
                        + "\n");
    }


    protected void printSpacedFooter() {
        System.out.println(
                "**********************************************************"
                        + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");
    }

    protected void printBitteWahlnummerWaehlenFooter() {
        System.out.println(
                "***************BITTE EINE WAHLNUMMER EINGEBEN***************"
                        + "\n");
    }


}
