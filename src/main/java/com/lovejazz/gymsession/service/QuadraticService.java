package com.lovejazz.gymsession.service;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuadraticService {
    private static final double EPSILON = 1e-10;

    public List<Double> solve(double a, double b, double c) {
        if (Math.abs(a) < EPSILON) {
            throw new IllegalArgumentException("Коэффициент 'a' не может быть нулевым");
        }

        double discriminant = b * b - 4 * a * c;

        if (Math.abs(discriminant) < EPSILON) {
            double x = -b / (2 * a);
            return List.of(x);
        } else if (discriminant < -EPSILON) {
            throw new IllegalArgumentException("Уравнение не имеет действительных корней");
        } else {
            double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            return List.of(x1, x2);
        }
    }
}