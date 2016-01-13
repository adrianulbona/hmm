package ro.ab.hmm;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toSet;

/**
 * Created by adrianbona on 1/13/16.
 */

@RequiredArgsConstructor
public class Model<O extends Observation,S extends State> {

    private final List<ObservationProbabilities<O, S>> probabilities;

    public int observationCount() {
        return probabilities.size();
    }

    public int numberOfDistinctStates() {
        return probabilities.stream()
                .flatMap(op -> op.getEmissionProbabilities().keySet().stream())
                .collect(toSet())
                .size();
    }

    public int numberOfDistinctObservations() {
        return probabilities.stream().map(ObservationProbabilities::getObservation).collect(toSet()).size();
    }
}
