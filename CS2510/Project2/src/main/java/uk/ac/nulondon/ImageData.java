package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageData {
    // ArrayList of pixels to be used to store seams to delete
    ArrayList<Pixel> pixels = new ArrayList<>();

    /**
     * @param file will be the filepath to an image that will be converted into a graph
     *             based on a LinkedList of Lists of Integers. It will throw an exception if
     *             the filepath does not exist.
     */
    public void importImage(String file) throws IOException {

            System.out.println("Importing " + file);
            BufferedImage image = ImageIO.read(new File(file));

            int width = image.getWidth();
            int height = image.getHeight();
            pixels = new ArrayList<>();
            Pixel previousPixel = null;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    Pixel currentPixel = new Pixel(r, g, b);

                    if (x == 0) {
                        pixels.add(currentPixel);
                    } else {
                        previousPixel.setRight(currentPixel);
                        currentPixel.setLeft(previousPixel);
                    }
                    previousPixel = currentPixel;
                }

        }

    }

    /**
     * @param file this is the filepath where the altered image will be stored at.
     *             This method will convert an altered graph to an image and be
     *             displayed to the user.
     */
    public void exportImage(String file) {
        if (pixels == null || pixels.isEmpty()) {
            System.out.println("No pixel data available to export.");
            return;
        }
        try {
            BufferedImage image = new BufferedImage(pixels.getFirst().getSizeLink(), pixels.size(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < pixels.size(); y++) {
                Pixel currentPixel = pixels.get(y);
                for (int x = 0; x < pixels.getFirst().getSizeLink(); x++) {
                    int rgb = (currentPixel.getRed() << 16) | (currentPixel.getGreen() << 8) | currentPixel.getBlue();
                    image.setRGB(x, y, rgb);
                    currentPixel = currentPixel.getRight();
                }
            }
            ImageIO.write(image, "png", new File(file));
        } catch (IOException e) {
            System.out.println("ERROR EXPORTING IMAGE: " + e.getMessage());
        }
    }

    /**
     * This method will go through each pixel in an image and calculate each pixel's energy
     */
    public void iterateEnergy() {
        if(pixels == null)
            return;

        for (Pixel pix : pixels) {
            Pixel up = (pixels.indexOf(pix) > 0) ? pixels.get(pixels.indexOf(pix) - 1) : pix;
            Pixel down = (pixels.indexOf(pix) < pixels.size() - 1) ? pixels.get(pixels.indexOf(pix) + 1) : pix;

            while (pix != null) {
                pix.setEnergy(calcEnergy(up, pix, down));
                up = up.getRight();
                down = down.getRight();
                pix = pix.getRight();
            }
        }
    }

    /**
     * This will calculate the brightness of a pixel by averaging teh RGB values
     * @param pixel given pixel to calculate brightness
     * @return the brightness of that given pixel
     */
    public int calculateBrightness(Pixel pixel, Pixel backup){
        if(pixel == null) {
            int sum = backup.getGreen() + backup.getBlue() +  backup.getRed();
            return sum /3;
        }
        int sum = pixel.getGreen() + pixel.getBlue() +  pixel.getRed();
        return sum /3;
    }

    /**
     * This will calculate energy based on three given pixels
     * @param up this is the pixel above the pixel to be calculated
     * @param middle this is the pixel to be calculated
     * @param down this is the pixel below the pixel to be calculated
     * @return the energy of the middle pixel
     */
    public double calcEnergy(Pixel up, Pixel middle, Pixel down) {
        int horizontal[] = {0, 0};
        int vertical[] = {0, 0};

        if(up == null)
            up = middle;
        if(down == null)
            down = middle;

        horizontal[0] = calculateBrightness(up.getLeft(),middle) + calculateBrightness(up,middle) * 2 + calculateBrightness(up.getRight(),middle);
        horizontal[1] = calculateBrightness(down.getLeft(),middle) + calculateBrightness(down,middle) * 2 + calculateBrightness(down.getRight(),middle);

        vertical[0] = calculateBrightness(up.getLeft(),middle) + calculateBrightness(middle.getLeft(),middle) * 2 + calculateBrightness(down.getLeft(),middle);
        vertical[1] = calculateBrightness(up.getRight(),middle) + calculateBrightness(middle.getRight(),middle) * 2 + calculateBrightness(down.getRight(),middle);

        return Math.sqrt(Math.pow((horizontal[1] - horizontal[0]), 2) + Math.pow((vertical[1] - vertical[0]), 2));
    }

    /**
     * This will add an element to a collection
     * @param element main element to be added
     * @param elements the collection to store the element
     * @return returns the  new collection
     * @param <T> Allows for a collection of any type T
     */
    public static <T> List<T> concat(T element, Collection<? extends T> elements) {
        List<T> result = new ArrayList<>();
        result.add(element);
        result.addAll(elements);
        return result;
    }

    /**
     * This will calculate the energies of each pixel then find the seam based on a parameter
     * @param isBlue this will provide instructions for if the lowest energy seam or bluest seam is found
     * @return returns a list of seams to be deleted
     */
    public List<Pixel> getSeam(boolean isBlue) {
        int width = pixels.getFirst().getSizeLink();
        iterateEnergy();
        if (pixels.isEmpty()) return new ArrayList<>();

        double[] previousValues = new double[width]; // the row above's values
        double[] currentValues = new double[width];  // current row's values
        List<List<Pixel>> previousSeams = new ArrayList<>(); // seam values from last iteration
        List<List<Pixel>> currentSeams = new ArrayList<>(); // seam values with this row's iteration
        int col = 0;
        Pixel p = pixels.getFirst();
        Pixel currentPixel;

        // initializing for first row
        while (col < pixels.getFirst().getSizeLink()) {
            previousValues[col] = isBlue ? p.getBlue() : p.getEnergy();
            previousSeams.add(concat(p, List.of()));
            p = p.getRight();
            col++;
        }

        int anotherWidth = pixels.getFirst().getSizeLink();
        // compute values and paths for each row
        for (int row = 1; row < pixels.size(); row++) {
            currentPixel = pixels.get(row);
            int index = 0;

            while (index < anotherWidth) {
                double bestSoFar = previousValues[index];
                int ref = index;

                if (index > 0 && previousValues[index - 1] > bestSoFar && isBlue) {
                    bestSoFar = previousValues[index - 1];
                    ref = index - 1;
                } else if (index > 0 && previousValues[index - 1] < bestSoFar && !isBlue) {
                    bestSoFar = previousValues[index - 1];
                    ref = index - 1;
                }
                if (index < anotherWidth - 1  && previousValues[index + 1] > bestSoFar && isBlue) {
                    bestSoFar = previousValues[index + 1];
                    ref = index + 1;
                } else if (index < anotherWidth - 1  && previousValues[index + 1] < bestSoFar && !isBlue) {
                    bestSoFar = previousValues[index + 1];
                    ref = index + 1;
                }

                currentValues[index] = bestSoFar + (isBlue ? currentPixel.getBlue() : currentPixel.getEnergy());
                currentSeams.add(concat(currentPixel, previousSeams.get(ref)));
                index++;
                currentPixel = currentPixel.getRight();
            }
            previousValues = currentValues;
            previousSeams = currentSeams;
            currentValues = new double[anotherWidth];
            currentSeams = new ArrayList<>();
        }

        return previousSeams.get(getMaxOrMinIndex(previousValues, isBlue));
    }

    /**
     * This will get the index of the pixels in a seam to delete
     * @param array the seam
     * @param isBlue if it is lowest-energy or greatest-blue
     * @return the index of the lowest/highest value in a row
     */
        public int getMaxOrMinIndex(double[] array, boolean isBlue) {
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (isBlue) {
                if (array[i] > array[maxIndex]) { maxIndex = i; }
            } else {
                if (array[i] < array[maxIndex]) maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * @return this will return an ArrayList of pixels
     */
    public List<Pixel> getPixels(){ return pixels; }
    }

