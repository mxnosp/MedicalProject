package model;

import model.exceptions.InvalidSpirometryValueInput;

/**
 * Spirometry class stores the values measured at a spirometry of a patient during a visit
 */
public class Spirometry {
    private final Double FEV1;
    private final Double FVC;
    private final Double PEF;
    private final Double FEF2575;

    public Spirometry(Double FEV1, Double FVC, Double PEF, Double FEF2575){
        if(FEV1!=null){
            if(FEV1<0){
                throw new InvalidSpirometryValueInput("Το FEV1 πρέπει να είναι θετικό νούμερο!");
            }
        }
        this.FEV1=FEV1;
        if(FVC!=null){
            if(FVC<=0){
                throw new InvalidSpirometryValueInput("Το FVC πρέπει να είναι θετικό νούμερο!");
            }
        }
        this.FVC=FVC;
        if(PEF!=null){
            if(PEF<0){
                throw new InvalidSpirometryValueInput("Το PEF πρέπει να είναι θετικό νούμερο!");
            }
        }
        this.PEF=PEF;
        if(FEF2575!=null){
            if(FEF2575<0){
                throw new InvalidSpirometryValueInput("Το FEF25-75 πρέπει να είναι θετικό νούμερο!");
            }
        }
        this.FEF2575=FEF2575;
    }

    /**
     * @return the FEF2575
     */
    public Double getFEF2575() {
        return FEF2575;
    }
    /**
     * @return the PEF
     */
    public Double getPEF() {
        return PEF;
    }
    /**
     * @return the FVC
     */
    public Double getFVC() {
        return FVC;
    }
    /**
     * @return the FEV1
     */
    public Double getFEV1() {
        return FEV1;
    }
}
