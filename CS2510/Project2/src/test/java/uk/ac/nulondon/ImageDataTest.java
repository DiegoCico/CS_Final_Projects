package uk.ac.nulondon;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class tests various functionalities of the ImageData class,
 * focusing on the correctness of pixel manipulations and utility methods.
 */
public class ImageDataTest {

    /**
     * Tests the calculateBrightness method with a valid Pixel.
     * Ensures the method correctly calculates the average RGB values.
     */
    @Test
    public void testCalculateBrightnessWithValidPixel() {
        ImageData imageData = new ImageData();
        Pixel pixel = new Pixel(10, 20, 30);
        int brightness = imageData.calculateBrightness(pixel, null);
        assertThat(brightness).isEqualTo((10 + 20 + 30) / 3);
    }

    /**
     * Tests the calculateBrightness method with a null Pixel.
     * Ensures the method uses the backup Pixel when the main Pixel is null.
     */
    @Test
    public void testCalculateBrightnessWithNullPixel() {
        ImageData imageData = new ImageData();
        Pixel backup = new Pixel(10, 20, 30);
        int brightness = imageData.calculateBrightness(null, backup);
        assertThat(brightness).isEqualTo((10 + 20 + 30) / 3);
    }

    /**
     * Tests the calcEnergy method with typical Pixel values.
     * Verifies that the energy calculation correctly handles RGB differences.
     */
    @Test
    public void testCalcEnergyWithTypicalPixels() {
        ImageData imageData = new ImageData();
        Pixel up = new Pixel(255, 0, 0);
        Pixel middle = new Pixel(0, 255, 0);
        Pixel down = new Pixel(0, 0, 255);
        double energy = imageData.calcEnergy(up, middle, down);
        assertThat(energy).isEqualTo(0);
    }

    /**
     * Tests the concat method with a non-empty collection of Pixels.
     * Verifies that Pixels are added correctly to the collection.
     */
    @Test
    public void testConcatWithNonEmptyCollection() {
        ImageData imageData = new ImageData();
        List<Pixel> additionalPixels = List.of(new Pixel(1, 2, 3), new Pixel(4, 5, 6));
        List<Pixel> result = imageData.concat(new Pixel(7, 8, 9), additionalPixels);
        assertThat(result.get(0)).extracting(Pixel::getRed, Pixel::getGreen, Pixel::getBlue).contains(7, 8, 9);
        assertThat(result.get(1).getRed()).isEqualTo(1);
    }

    /**
     * Tests the concat method with an empty collection of Pixels.
     * Verifies that a single Pixel can be added to an empty collection.
     */
    @Test
    public void testConcatWithEmptyCollection() {
        ImageData imageData = new ImageData();
        List<Pixel> result = imageData.concat(new Pixel(7, 8, 9), new ArrayList<>());
        assertThat(result.get(0)).extracting(Pixel::getRed, Pixel::getGreen, Pixel::getBlue).contains(7, 8, 9);
    }

    /**
     * Tests getMaxOrMinIndex method to find the maximum value index in an array.
     */
    @Test
    public void testGetMaxOrMinIndexForMaxValue() {
        ImageData imageData = new ImageData();
        double[] array = {1.0, 3.0, 2.0, 5.0, 4.0};
        int index = imageData.getMaxOrMinIndex(array, true);
        assertThat(index).isEqualTo(3);
    }

    /**
     * Tests getMaxOrMinIndex method to find the minimum value index in an array.
     */
    @Test
    public void testGetMaxOrMinIndexForMinValue() {
        ImageData imageData = new ImageData();
        double[] array = {1.0, 3.0, 0.5, 5.0, 4.0};
        int index = imageData.getMaxOrMinIndex(array, false);
        assertThat(index).isEqualTo(2);
    }

    /**
     * Tests getMaxOrMinIndex method with identical values in the array.
     * Verifies that the first index is returned for both max and min searches.
     */
    @Test
    public void testGetMaxOrMinIndexWithIdenticalValues() {
        ImageData imageData = new ImageData();
        double[] array = {2.0, 2.0, 2.0, 2.0, 2.0};
        int maxIndex = imageData.getMaxOrMinIndex(array, true);
        int minIndex = imageData.getMaxOrMinIndex(array, false);
        assertThat(maxIndex).isEqualTo(0);
        assertThat(minIndex).isEqualTo(0);
    }
}
