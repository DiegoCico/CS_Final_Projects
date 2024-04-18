package uk.ac.nulondon;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class ImageEdit {
    // This will store the deleted seam
    private Stack<Pair<List<Pixel>, List<PColor>>>history = new Stack<>();
    // Instance of ImageData class
    public ImageData imageData;

    // A class for original colors of pixel seam
    class PColor {
        int red;
        int green;
        int blue;
        public PColor(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    /**
     * This is a constructor for ImageEdit using ImageData
     */
    public ImageEdit() {
        imageData = new ImageData();
    }

    /**
     * This is an abstracted method to highlight a column
     * @param operation will provide a string to determine what seam will be highlighted
     * @return this will return the list of pixels that are highlighted
     */
    public List<Pixel> highlightColumn(String operation){
        switch(operation){
            case "b":
                return highlightBlue();
            default:
                return highlightRed();
        }
    }

    /**
     * This is a helper function to highlight the greatest-blue seam
     * @return this returns the list of pixels to be deleted
     */
    private List<Pixel> highlightBlue() {
        ArrayList<Pixel> pixels = new ArrayList<>();
        ArrayList<Pixel> oldPixels = new ArrayList<>();
        ArrayList<PColor> oldColors = new ArrayList<>();
        for(Pixel p: imageData.getSeam(true)){
            oldPixels.add(p);
            oldColors.add(new PColor(p.getRed(),p.getGreen(),p.getBlue()));
            p.setPixel(0, 0, 255);
            pixels.add(p);
        }
        history.push(Pair.of(oldPixels, oldColors));
        return pixels;
    }

    /**
     * This is a helper function to highlight the lowest-energy seam
     * @return this returns the list of pixels to be deleted
     */
    private List<Pixel> highlightRed() {
        ArrayList<Pixel> pixels = new ArrayList<>();
        ArrayList<Pixel> oldPixels = new ArrayList<>();
        ArrayList<PColor> oldColors = new ArrayList<>();
        for(Pixel p: imageData.getSeam(false)){
            oldPixels.add(p);
            oldColors.add(new PColor(p.getRed(),p.getGreen(),p.getBlue()));
            p.setPixel(255, 0, 0);
            pixels.add(p);
        }
        history.push(Pair.of(oldPixels, oldColors));
        return pixels;
    }

    /**
     * This method will save and delete the list of pixels chosen from highlightColumn
     * It will remove the references between the pixels in seam to be deleted
     * @param pixelsToRemove this is the list of pixels chosen from highlightColumn
     */
    public void deleteColumn(List<Pixel> pixelsToRemove) {
        try {
            int count = pixelsToRemove.size() - 1;
            for (Pixel pixel : pixelsToRemove) {
                if (pixel.getLeft() != null && pixel.getRight() != null) {
                    pixel.getLeft().setRight(pixel.getRight());
                    pixel.getRight().setLeft(pixel.getLeft());
                }
                if (pixel.getLeft() == null) {
                    imageData.getPixels().set(count, pixel.getRight());
                    pixel.getRight().setLeft(null);
                } else if (pixel.getRight() == null) {
                    pixel.getLeft().setRight(null);
                }
                count--;
            }
        }
        catch (Exception e) {
           System.out.println("No more deletes left");
        }
    }

    /**
     * This will add references back with the seam deleted and undo an edit
     */
    public void undo(){
        try {
            if (history.isEmpty()) {
            System.out.println("No more undos available");
            return;
        }

            Pair<List<Pixel>, List<PColor>> lastState = history.pop();

            List<Pixel> pixels = lastState.getLeft();
            List<PColor> colors = lastState.getRight();

            ArrayList<Pixel> add = new ArrayList<>();
            for(int i = 0; i < pixels.size(); i++){
                add.add(pixels.get(i));
                add.get(i).setPixel(colors.get(i).red,colors.get(i).green,colors.get(i).blue);
            }

            int count = add.size() - 1;
            for (Pixel p : add) {
                if (p.getLeft() != null) {
                    p.getLeft().setRight(p);
                } else {
                    imageData.getPixels().set(count, p);
                }

                if (p.getRight() != null) {
                    p.getRight().setLeft(p);
                }
                count--;
            }
        }
        catch (EmptyStackException e) {
            System.out.println("Stack is empty");
        }
    }

    /**
     * This will return how many edits have been made
     * @return the number of edits
     */
    public int editCount(){ return history.size(); }
}
