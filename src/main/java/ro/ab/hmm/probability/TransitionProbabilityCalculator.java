package ro.ab.hmm.probability;

import ro.ab.hmm.Observation;
import ro.ab.hmm.State;
import ro.ab.hmm.Transition;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface TransitionProbabilityCalculator<S extends State> {

    Double probability(Transition<S> transition);
}
