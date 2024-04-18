package uk.ac.nulondon;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the functionality of the Pixel class ensuring that all pixel manipulations
 * are correct and adhere to expected behaviors.
 */
public class PixelTest {

    /**
     * Tests the default constructor of the Pixel class to ensure it creates a pixel
     * initialized to white (255, 255, 255).
     */
    @Test
    void defaultConstructorShouldCreateWhitePixel() {
        Pixel pixel = new Pixel();
        assertThat(pixel.getRed()).isEqualTo(255);
        assertThat(pixel.getGreen()).isEqualTo(255);
        assertThat(pixel.getBlue()).isEqualTo(255);
    }

    /**
     * Tests the parameterized constructor to ensure that the RGB values are set correctly.
     */
    @Test
    void parameterizedConstructorShouldSetCorrectValues() {
        Pixel pixel = new Pixel(255, 128, 64);
        assertThat(pixel.getRed()).isEqualTo(255);
        assertThat(pixel.getGreen()).isEqualTo(128);
        assertThat(pixel.getBlue()).isEqualTo(64);
    }

    /**
     * Tests the setPixel method to ensure that it correctly updates the RGB values of a pixel.
     */
    @Test
    void setPixelShouldChangeValuesCorrectly() {
        Pixel pixel = new Pixel();
        pixel.setPixel(10, 20, 30);
        assertThat(pixel.getRed()).isEqualTo(10);
        assertThat(pixel.getGreen()).isEqualTo(20);
        assertThat(pixel.getBlue()).isEqualTo(30);
    }

    /**
     * Tests the setEnergy and getEnergy methods to verify correct setting and retrieving
     * of the energy value.
     */
    @Test
    void setAndGetEnergy() {
        Pixel pixel = new Pixel();
        pixel.setEnergy(100);
        assertThat(pixel.getEnergy()).isEqualTo(100);
        pixel.setEnergy(200);
        assertThat(pixel.getEnergy()).isEqualTo(200);
    }

    /**
     * Verifies that the setLeft method correctly sets the left neighboring pixel.
     */
    @Test
    void setLeft() {
        Pixel pixel = new Pixel();
        Pixel leftPixel = new Pixel();
        pixel.setLeft(leftPixel);
        assertThat(pixel.getLeft()).isEqualTo(leftPixel);
    }

    /**
     * Verifies that the setRight method correctly sets the right neighboring pixel.
     */
    @Test
    void setRight() {
        Pixel pixel = new Pixel();
        Pixel rightPixel = new Pixel();
        pixel.setRight(rightPixel);
        assertThat(pixel.getRight()).isEqualTo(rightPixel);
    }
}
