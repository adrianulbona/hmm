package io.github.adrianulbona.hmm;

import java.util.List;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface ReachableStateFinder<S extends State, O extends Observation> {

	List<S> reachableFor(O observation);
}
