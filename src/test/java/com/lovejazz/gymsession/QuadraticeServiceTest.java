package com.lovejazz.gymsession;

import com.lovejazz.gymsession.service.QuadraticService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuadraticeServiceTest {
    private final QuadraticService service = new QuadraticService();

    @Test
    void solveQuadratic_TwoRoots() {
        List<Double> roots = service.solve(1, -5, 6); // x² -5x +6 =0 → x1=3, x2=2
        assertEquals(2, roots.size());
        assertTrue(roots.contains(3.0));
        assertTrue(roots.contains(2.0));
    }

    @Test
    void solveQuadratic_OneRoot() {
        List<Double> roots = service.solve(1, -4, 4); // x² -4x +4 =0 → x=2
        assertEquals(1, roots.size());
        assertEquals(2.0, roots.get(0));
    }

    @Test
    void solveQuadratic_NoRealRoots() {
        assertThrows(IllegalArgumentException.class, () -> service.solve(1, 0, 1)); // x² +1 =0 → нет корней
    }
}
