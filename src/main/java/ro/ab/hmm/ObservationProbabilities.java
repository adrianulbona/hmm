package ro.ab.hmm;

import lombok.Data;

import java.util.Map;

/**
 * Created by adrianbona on 1/13/16.
 */

@Data
public class ObservationProbabilities<O extends Observation, S extends State> {

    private final O observation;
    private final Map<Transition<S>, Double> transitionProbabilities;
    private final Map<S, Double> emissionProbabilities;
}
