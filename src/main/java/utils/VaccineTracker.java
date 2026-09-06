package utils;

import model.Vaccine;

/**
 * holds a vaccine and a boolean that represents the state of this vaccine (if it is true then the vaccine was added,otherwise it was deleted)
 */
public class VaccineTracker {
    private final Vaccine vaccine;
    private VaccineState state;

    public VaccineTracker(Vaccine vaccine , VaccineState change){
        this.vaccine=vaccine;
        this.state=change;
    }

    public VaccineState getVaccineState(){return state;}

    public void setVaccineState(VaccineState  state){this.state=state;}

    public Vaccine getVaccine(){return vaccine;}
}
