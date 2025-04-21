package org.example;

public class FIFOReadRet{
    int rdata;
    int empty;
    int full;

    FIFOReadRet(){

    }

    FIFOReadRet(int rdata, int empty, int full){
        this.rdata = rdata;
        this.empty = empty;
        this.full = full;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FIFOReadRet) {
            FIFOReadRet other = (FIFOReadRet) obj;
            return rdata == other.rdata && empty == other.empty && full == other.full;
        }
        return false;
    }
};
