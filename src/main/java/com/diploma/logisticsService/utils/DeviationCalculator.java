package com.diploma.logisticsService.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviationCalculator {
    /**
     * returns average value of of elements in list of double
     *
     * @return double average value
     */
    public double getAverage(List<Double> numbers) throws IllegalArgumentException {
        if (numbers == null || numbers.size() == 0)
            throw new IllegalArgumentException("List is empty. Can't Divide by zero");
        double sum = numbers.stream().reduce(0.0, Double::sum);
        return sum / numbers.size();
    }

    /**
     * Returns standart deviation of elements in list of double
     *
     * @return double standart deviation value
     */
    public double getStandartDeviation(List<Double> numbers) throws IllegalArgumentException {
        int size = numbers.size();
        if (size == 0) throw new IllegalArgumentException("List is empty. Can't Divide by zero");

        double average = getAverage(numbers);

        double sum = numbers
                .stream()
                .map(number -> Math.pow(number - average, 2))
                .reduce(0.0, Double::sum); //sum of (x - middle x)^2
        return Math.sqrt(sum / size); //sqrt(sum of (x - middle x)^2/size)
    }
}
