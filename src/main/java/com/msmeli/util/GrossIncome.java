package com.msmeli.util;

public enum GrossIncome {
    IIBB(.045);

    public final double iibPercentage;

    GrossIncome(double iibb) {
        this.iibPercentage = iibb;
    }
}
