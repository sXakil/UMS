package com.ums.pau;

public class MarkToCGPA {
    private double cgpa = 0;
    private double[] subjects;

    MarkToCGPA(double[] subjects) {
        this.subjects = subjects;
    }

    private double calculate() {
        for (double subject : subjects) {
            cgpa += subject;
        }
        return cgpa / subjects.length;
    }
}
