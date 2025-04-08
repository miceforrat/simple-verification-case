package org.example;


public class FIFOWriteRet {
    int empty;
    int full;

    FIFOWriteRet() {
    }

    FIFOWriteRet(int empty, int full) {
        this.empty = empty;
        this.full = full;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FIFOWriteRet) {
            FIFOWriteRet other = (FIFOWriteRet) obj;
            return empty == other.empty && full == other.full;
        }
        return false;
    }
}