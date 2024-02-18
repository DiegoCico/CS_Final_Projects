    package uk.ac.nulondon;

    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.TestTemplate;
    import static org.assertj.core.api.Assertions.assertThat;

    import java.io.File;
    import java.io.IOException;

    import static org.assertj.core.api.Assertions.assertThat;
    import static org.junit.jupiter.api.Assertions.*;

    public class ImageProcessorTest {
        private ImageProcessor imageProcessor;
        private final String testImagePath = "src/img/input/square.png";

        @BeforeEach
        void setUp() throws IOException {
            File testImage = new File(testImagePath);
            imageProcessor = new ImageProcessor(testImage);
        }

        // Tests for bluestBlueCol method
        @Test
        void testBluestBlueColNormal() {
            int expectedBluestColumn = 1;
            assertEquals(expectedBluestColumn, imageProcessor.bluestBlueCol(), "Bluest column should be the expected one.");
        }

        @Test
        void testBluestBlueColBoundary() {
            int expectedBluestColumn = 1;
            assertEquals(expectedBluestColumn, imageProcessor.bluestBlueCol(), "Bluest column should be at the boundary.");
        }

        // Tests for colorColumnRed method
        @Test
        void testColorColumnRedMiddle() {
            int col = 1;
            imageProcessor.colorColumnRed(col);
        }

        @Test
        void testColorColumnRedBoundary() {
            int col = 0;
            imageProcessor.colorColumnRed(col);
        }

        // Tests for removeSpecificCol method
        @Test
        void testRemoveSpecificColMiddle() {
            int originalSize = imageProcessor.getGrid().size();
            int colToRemove = 1; // Example column index
            imageProcessor.removeSpecificCol(colToRemove);
            assertEquals(originalSize - 1, imageProcessor.getGrid().size(), "Column should be removed.");
        }

        @Test
        void testRemoveSpecificColBoundary() {
            int originalSize = imageProcessor.getGrid().size();
            int colToRemove = 0; // Example boundary column index
            imageProcessor.removeSpecificCol(colToRemove);
            assertEquals(originalSize - 1, imageProcessor.getGrid().size(), "Boundary column should be removed.");
        }

        // Tests for undo method
        @Test
        void testUndoAfterRemoval() {
            int originalSize = imageProcessor.getGrid().size();
            imageProcessor.removeSpecificCol(1); // remove a column
            imageProcessor.undo(); // undo the removal
            assertEquals(originalSize, imageProcessor.getGrid().size(), "Undo should restore the original grid size.");
        }
    }
