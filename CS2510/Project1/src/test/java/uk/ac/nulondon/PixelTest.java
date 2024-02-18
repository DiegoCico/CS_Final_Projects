package uk.ac.nulondon;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PixelTest {

    // Tests for the default constructor
    @Test
    void defaultConstructorShouldCreateBlackPixel() {
        Pixel pixel = new Pixel();
        assertThat(pixel.getRed()).isEqualTo(0);
        assertThat(pixel.getGreen()).isEqualTo(0);
        assertThat(pixel.getBlue()).isEqualTo(0);
    }

    @Test
    void defaultConstructorShouldNotCreateNonBlackPixel() {
        Pixel pixel = new Pixel();
        assertThat(pixel.getRed()).isNotEqualTo(255);
        assertThat(pixel.getGreen()).isNotEqualTo(255);
        assertThat(pixel.getBlue()).isNotEqualTo(255);
    }

    // Tests for the parameterized constructor
    @Test
    void parameterizedConstructorShouldSetCorrectValues() {
        Pixel pixel = new Pixel(255, 128, 64);
        assertThat(pixel.getRed()).isEqualTo(255);
        assertThat(pixel.getGreen()).isEqualTo(128);
        assertThat(pixel.getBlue()).isEqualTo(64);
    }

    @Test
    void parameterizedConstructorShouldNotSetIncorrectValues() {
        Pixel pixel = new Pixel(255, 128, 64);
        assertThat(pixel.getRed()).isNotEqualTo(0);
        assertThat(pixel.getGreen()).isNotEqualTo(0);
        assertThat(pixel.getBlue()).isNotEqualTo(0);
    }

    // Tests for the string constructor
    @Test
    void stringConstructorShouldParseCorrectValues() {
        Pixel pixel = new Pixel("102, 153, 204");
        assertThat(pixel.getRed()).isEqualTo(102);
        assertThat(pixel.getGreen()).isEqualTo(153);
        assertThat(pixel.getBlue()).isEqualTo(204);
    }

    @Test
    void stringConstructorShouldHandleInvalidInput() {
        Pixel pixel = new Pixel("invalid,input,string");
        assertThat(pixel.getRed()).isEqualTo(0);
        assertThat(pixel.getGreen()).isEqualTo(0);
        assertThat(pixel.getBlue()).isEqualTo(0);
    }

    // Tests for setPixel method
    @Test
    void setPixelShouldChangeValuesCorrectly() {
        Pixel pixel = new Pixel();
        pixel.setPixel(10, 20, 30);
        assertThat(pixel.getRed()).isEqualTo(10);
        assertThat(pixel.getGreen()).isEqualTo(20);
        assertThat(pixel.getBlue()).isEqualTo(30);
    }

    @Test
    void setPixelShouldNotRetainOldValues() {
        Pixel pixel = new Pixel(100, 100, 100);
        pixel.setPixel(10, 20, 30);
        assertThat(pixel.getRed()).isNotEqualTo(100);
        assertThat(pixel.getGreen()).isNotEqualTo(100);
        assertThat(pixel.getBlue()).isNotEqualTo(100);
    }

    // Tests for toString method
    @Test
    void toStringShouldReturnCorrectFormat() {
        Pixel pixel = new Pixel(70, 80, 90);
        assertThat(pixel.toString()).isEqualTo("70, 80, 90");
    }

    @Test
    void toStringShouldNotReturnIncorrectFormat() {
        Pixel pixel = new Pixel(70, 80, 90);
        assertThat(pixel.toString()).isNotEqualTo("70, 80, 90, 100");
    }
}
