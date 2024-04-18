package uk.ac.nulondon;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * This class contains unit tests for the ImageEdit class.
 * It tests various functionalities such as highlighting columns based on color and undoing changes.
 */
public class ImageEditTest {
    private ImageEdit imageEdit;
    private Pixel[][] grid;

    /**
     * Sets up the test environment before each test method.
     * This includes initializing the ImageEdit instance and a 3x3 grid of white pixels.
     */
    @Before
    public void setUp() {
        imageEdit = new ImageEdit();
        initializeGrid();
    }

    /**
     * Initializes a 3x3 grid of white pixels and sets horizontal links between them.
     * This method populates the imageData's pixel list with a manually created column of pixels.
     */
    private void initializeGrid() {
        grid = new Pixel[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                grid[y][x] = new Pixel(255, 255, 255); // Initialize with white pixels.
                if (x > 0) {
                    grid[y][x-1].setRight(grid[y][x]);
                    grid[y][x].setLeft(grid[y][x-1]);
                }
            }
        }
        imageEdit.imageData.pixels.clear();
        for (int y = 0; y < 3; y++) {
            imageEdit.imageData.pixels.add(grid[y][0]);
        }
    }

    /**
     * Tests the highlightColumn method with the "b" parameter, which should highlight a blue column.
     * Verifies that the highlighted column is indeed blue.
     */
    @Test
    public void testHighlightBlue() {
        List<Pixel> highlighted = imageEdit.highlightColumn("b");
        assertNotNull(highlighted);
        assertFalse(highlighted.isEmpty());
        for (Pixel pixel : highlighted) {
            assertEquals(255, pixel.getBlue());
            assertEquals(0, pixel.getRed());
            assertEquals(0, pixel.getGreen());
        }
    }

    /**
     * Tests the highlightColumn method with a parameter other than "b", defaulting to red.
     * Verifies that the highlighted column is red.
     */
    @Test
    public void testHighlightRed() {
        List<Pixel> highlighted = imageEdit.highlightColumn("anythingElse");
        assertNotNull(highlighted);
        assertFalse(highlighted.isEmpty());
        for (Pixel pixel : highlighted) {
            assertEquals(255, pixel.getRed());
            assertEquals(0, pixel.getGreen());
            assertEquals(0, pixel.getBlue());
        }
    }

    /**
     * Tests the deleteColumn method by deleting a previously highlighted column and checking if the edit count remains the same.
     * The assumption here is that deleteColumn does not change the edit count.
     */
    @Test
    public void testDeleteColumn() {
        List<Pixel> pixels = imageEdit.highlightColumn("b");
        int beforeDelete = imageEdit.editCount();
        imageEdit.deleteColumn(pixels);
        int afterDelete = imageEdit.editCount();
        assertEquals(beforeDelete, afterDelete);
    }

    /**
     * Tests the undo functionality when there are edits to undo.
     * Verifies that the edit count decreases by one.
     */
    @Test
    public void testUndo() {
        imageEdit.highlightColumn("b");
        int beforeUndo = imageEdit.editCount();
        imageEdit.undo();
        int afterUndo = imageEdit.editCount();
        assertEquals(beforeUndo - 1, afterUndo);
    }

    /**
     * Tests the initial state of edit count, which should be zero before any operations.
     */
    @Test
    public void testEditCountInitial() {
        assertEquals(0, imageEdit.editCount());
    }

    /**
     * Tests the edit count after a highlight operation to ensure it is being tracked correctly.
     */
    @Test
    public void testEditCountAfterHighlight() {
        imageEdit.highlightColumn("b");
        assertEquals(1, imageEdit.editCount());
    }
}
