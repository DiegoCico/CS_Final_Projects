package uk.ac.nulondon;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ImageMain {
    // This is an instance of ImageEdit to handle image methods
    private static ImageEdit imageHandler;
    // This will keep track of how many edits have been made
    private static int editCount = 0;
    // This is the string to determine what operation on the seam occurs
    private static String choice = "";
    // Initializes the scanner
    private static Scanner in;

    /**
     * Print the UI menu options to the user
     */
    private static void printMenu() {
        System.out.println("Please enter a command");
        System.out.println("b - Remove the bluest column");
        System.out.println("r - Remove a random column");
        System.out.println("u - Undo previous edit");
        System.out.println("q - Quit");
    }

    /**
     * Perform an operation based on what the user selected
     */
    private static void handleChoice() {
        String option;
        switch (choice.toLowerCase()) {
            case "b":
                if(imageHandler.imageData.getPixels().getFirst().getSizeLink() == 0) {
                System.out.println("Image too small");
                break;
            }
                // highlight and export intermediate image
                List<Pixel> blueCol = imageHandler.highlightColumn(choice.toLowerCase());
                imageHandler.imageData.exportImage("tempIMG_0" + editCount + ".png");
                editCount++;

                // ask for confirmation and try to execute
                System.out.println("Remove the bluest column. Continue? (Y/N)");
                option = getUserInput();
                if (option.toUpperCase().equals("Y")) {

                    imageHandler.deleteColumn(blueCol);
                }
                else imageHandler.undo();
                break;
            case "r":
                if(imageHandler.imageData.getPixels().getFirst().getSizeLink() == 0) {
                System.out.println("Image too small");
                break;
            }
                // highlight and export intermediate image
                List<Pixel> redCol = imageHandler.highlightColumn("");
                imageHandler.imageData.exportImage("tempIMG_0" + editCount + ".png");
                editCount++;

                // ask for confirmation and try to execute
                System.out.println("Remove a random column. Continue? (Y/N)");
                option = getUserInput();
                if (option.toUpperCase().equals("Y")) {

                    imageHandler.deleteColumn(redCol);
                }
                else imageHandler.undo();
                break;
            case "u":
                System.out.println("Undo. Continue? (Y/N)");
                option = getUserInput();
                if (option.toUpperCase().equals("Y")) {
                    imageHandler.undo();
                    imageHandler.imageData.exportImage("tempIMG_0" + editCount + ".png");
                    editCount++;
                }
                break;
            case "q":
                System.out.println("Thanks for playing.");
                break;
            default:
                System.out.println("That is not a valid option.");
                break;
        }
    }

    /**
     * Get the user's input. Either a menu selection or confirmation value.
     * @return the user's input
     */
    private static String getUserInput() {
        String keyValue = "";

        // get the user's input
        try {
            keyValue = in.next().toLowerCase();
        } catch (InputMismatchException e) {
            // if user enters anything except a menu option
            System.out.println("Input should be one of the menu options");
        }

        return keyValue;
    }

    /**
     * This method will use recursion until a valid file path is found
     * @param filePath this is the user input
     */
    private static void fileFound(String filePath){
        try {
            imageHandler.imageData.importImage(filePath);
        } catch (IOException e) {
            System.out.println("File not found. Please try again.");
            String neFile = getUserInput();
            fileFound(neFile);
        }
    }

    //Main method that serves as a UI
    public static void main(String[] args) {
        boolean shouldQuit = false;

        in = new Scanner(System.in);

        System.out.println("Welcome! Enter file path");
        String filePath = getUserInput();
        imageHandler = new ImageEdit();

        fileFound(filePath);

        // display the menu after every edit
        while (!shouldQuit) {
            printMenu();

            // get and handle user input
            choice = getUserInput();
            handleChoice();

            if (choice.equals("q")) {
                shouldQuit = true;
            }
        }
        // After the user exits, export the final image
        imageHandler.imageData.exportImage("newImg.png");
        in.close();
    }
}

