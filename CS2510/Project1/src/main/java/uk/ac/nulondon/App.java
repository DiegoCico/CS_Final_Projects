package uk.ac.nulondon;

import java.io.File;
import java.util.Scanner;

/**
 * The App class serves as the entry point for the image processing application.
 * It interacts with the user through the command line, allowing them to perform
 * various operations on images such as finding and coloring the bluest column,
 * removing random rows, and undoing changes.
 */
public final class App {
    /**
     * The main method starts the application. It handles user input and
     * directs the flow of the image processing operations.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {

        // Get the source file from the user
        Scanner in = new Scanner(System.in);
        System.out.println("What is the source for the file?");
        String src = in.nextLine();
        ImageProcessor ip = new ImageProcessor(new File(src));

        String userInput;
        while (true) {
            // Display options to the user
            System.out.println("What would you like to do?");
            System.out.println("B for bluest blue");
            System.out.println("R for remove random row");
            System.out.println("U for undo");
            System.out.println("Q for quit");
            // Process user input
            userInput = in.nextLine();
            if (userInput.equalsIgnoreCase("q")) {
                break;
            }

            switch (userInput.toLowerCase()) {
                case "b":
                    int col = ip.bluestBlueCol();
                    ip.colorColumnRed(col);
                    ip.exportImage("src/img/process/newImg.png");
                    System.out.println("Would you like to remove it? (d for yes)");
                    if (in.nextLine().toLowerCase().equals("d")) {
                        ip.removeSpecificCol(col);
                        ip.exportImage("src/img/output/newImg.png");
                    }
                    break;
                case "r":
                    int random = ip.removeRandomCol();
                    ip.colorColumnRed(random);
                    ip.exportImage("src/img/process/newImg.png");
                    System.out.println("Would you like to remove it? (d for yes)");
                    if (in.nextLine().toLowerCase().equals("d")) {
                        ip.removeSpecificCol(random);
                        ip.exportImage("src/img/output/newImg.png");
                    }
                    break;
                case "u":
                    ip.undo();
                    ip.exportImage("src/img/output/newImg.png");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        // Close the scanner
        in.close();
    }
}
