package ro.ab.hmm;

import lombok.Data;


/**
 * Created by adrian.bona on 13/01/16.
 */

@Data
public class Emission<S extends State, O extends Observation> {

    private final S state;
    private final O observation;
}
