package ro.ab.hmm.probability;

import lombok.RequiredArgsConstructor;
import ro.ab.hmm.Emission;
import ro.ab.hmm.Observation;
import ro.ab.hmm.State;
import ro.ab.hmm.Transition;


/**
 * Created by adrian.bona on 13/01/16.
 */

@RequiredArgsConstructor
public class ProbabilityCalculator<S extends State, O extends Observation>
        implements EmissionProbabilityCalculator<S, O>, TransitionProbabilityCalculator<S> {

    private final EmissionProbabilityCalculator<S, O> emissionProbabilityCalculator;
    private final TransitionProbabilityCalculator<S> transitionProbabilityCalculator;

    @Override
    public Double probability(Emission<S, O> emission) {
        final Double probability = emissionProbabilityCalculator.probability(emission);
        return probability == null ? 0.0 : probability;
    }

    @Override
    public Double probability(Transition<S> transition) {
        final Double probability = transitionProbabilityCalculator.probability(transition);
        return probability == null ? 0.0 : probability;
    }
}
