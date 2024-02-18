package uk.ac.nulondon;

/**
 * The Pixel class represents a pixel with red, green, and blue color values.
 */
public class Pixel {
    // Class fields
    private int r;
    private int g;
    private int b;

    /**
     * Default constructor that creates a black pixel (all values set to 0).
     */
    public Pixel(){
        r = 255;
        g = 255;
        b = 255;
    }

    /**
     * Constructor that creates a pixel with specified red, green, and blue values.
     *
     * @param red the red component of the pixel
     * @param green the green component of the pixel
     * @param blue the blue component of the pixel
     */
    public Pixel(int red, int green, int blue){
        setPixel(red, green, blue);
    }

    /**
     * Constructor that creates a pixel from a string containing RGB values.
     * The RGB values should be separated by commas.
     *
     * @param RGB the string containing RGB values in "red, green, blue" format
     */
    public Pixel(String RGB) {
        String[] rgbA = RGB.split(", ");
        if(rgbA.length != 3) {
            System.out.println("Unable to create pixel, creating a blank pixel instead.");
            r = 255;
            g = 255;
            b = 255;
        } else {
            r = Integer.parseInt(rgbA[0]);
            g = Integer.parseInt(rgbA[1]);
            b = Integer.parseInt(rgbA[2]);
        }
    }

    /**
     * Sets the pixel's color values.
     *
     * @param red the red component to set
     * @param green the green component to set
     * @param blue the blue component to set
     */
    public void setPixel(int red, int green, int blue){
        r = red;
        g = green;
        b = blue;
    }

    // Individual setters and getters for each color component
    public void setRed(int red){ r = red; }
    public void setGreen(int green) { g = green; }
    public void setBlue(int blue) { b = blue; }
    public int getRed(){ return r; }
    public int getGreen(){ return g; }
    public int getBlue(){ return b; }

    /**
     * Returns a string representation of the pixel's color values.
     *
     * @return a string in the format "red, green, blue"
     */
    @Override
    public String toString(){ return r + ", " + g + ", " + b; }
}
