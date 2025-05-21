package com.lovejazz.gymsession.service;

import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    public int[][] multiply(int[][] a, int[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bCols = b[0].length;

        if (aCols != b.length) {
            throw new IllegalArgumentException("Несовместимые размеры матриц");
        }

        int[][] result = new int[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}
