package ro.ab.hmm;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ro.ab.hmm.probability.ProbabilityCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;


/**
 * Created by adrianbona on 1/13/16.
 */

@RequiredArgsConstructor
public class Model<S extends State, O extends Observation> {

    private final List<O> observations;
    private final ProbabilityCalculator<S, O> probabilityCalculator;
    private final ReachableStateFinder<S, O> reachableStateFinder;

    private final Map<O, List<S>> reachableStatesCache = new HashMap<>();

    public int observationCount() {
        return observations.size();
    }

    public int numberOfDistinctStates() {
        return observations.stream().map(reachableStateFinder::reachableFor).flatMap(List::stream).collect(toSet())
                .size();
    }

    public Map<Emission<S, O>, Double> emissionProbabilitiesFor(O observation) {
        return getReachableStatesFor(observation).stream().map(s -> new Emission<>(s, observation))
                .map(e -> new EmissionProbability(e, probabilityCalculator.probability(e)))
                .collect(toMap(EmissionProbability::getEmission, EmissionProbability::getProbability));
    }

    public Map<Transition<S>, Double> transitionProbabilitiesFor(O observation, O nextObservation) {
        final List<S> reachableStatesObservation = getReachableStatesFor(observation);
        final List<S> reachableStatesNextObservation = getReachableStatesFor(nextObservation);

        final List<Transition<S>> transitions = reachableStatesObservation.stream()
                .flatMap(s -> reachableStatesNextObservation.stream().map(ns -> new Transition<>(s, ns)))
                .collect(toList());

        return transitions.stream().map(t -> new TransitionProbability(t, probabilityCalculator.probability(t)))
                .collect(toMap(TransitionProbability::getTransition, TransitionProbability::getProbability));
    }

    public List<S> getReachableStatesFor(O observation) {
        if (!reachableStatesCache.containsKey(observation)) {
            reachableStatesCache.put(observation, reachableStateFinder.reachableFor(observation));
        }
        return reachableStatesCache.get(observation);
    }

    @Data
    private class EmissionProbability {

        private final Emission<S, O> emission;
        private final Double probability;
    }


    @Data
    private class TransitionProbability {

        private final Transition<S> transition;
        private final Double probability;
    }
}
