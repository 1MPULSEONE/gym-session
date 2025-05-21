package com.lovejazz.gymsession;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.lovejazz.gymsession.service.MatrixService;

class MatrixServiceTest {

    private final MatrixService service = new MatrixService();

    @Test
    void multiplyMatrices_ValidInput() {
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{5, 6}, {7, 8}};
        int[][] expected = {{19, 22}, {43, 50}};
        assertArrayEquals(expected, service.multiply(a, b));
    }

    @Test
    void multiplyMatrices_IncompatibleSizes() {
        int[][] a = {{1, 2}};
        int[][] b = {{1}};
        assertThrows(IllegalArgumentException.class, () -> service.multiply(a, b));
    }
}