package ro.ab.hmm;

import lombok.RequiredArgsConstructor;
import ro.ab.hmm.probability.ProbabilityCalculator;

import java.util.List;

import static java.util.stream.Collectors.toSet;


/**
 * Created by adrianbona on 1/13/16.
 */

@RequiredArgsConstructor
public class Model<S extends State, O extends Observation> {

    private final List<O> observations;
    private final ProbabilityCalculator<S, O> probabilityCalculator;
    private final ReachableStateFinder<S, O> reachableStateFinder;

    public int observationCount() {
        return observations.size();
    }

    public int numberOfDistinctStates() {
        return observations.stream().map(reachableStateFinder::findFor).flatMap(List::stream).collect(toSet()).size();
    }

    public int numberOfDistinctObservations() {
        return observations.stream().collect(toSet()).size();
    }
}
