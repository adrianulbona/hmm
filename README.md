## HMM abstractions in Java 8 

[![Build Status](https://travis-ci.org/adrianulbona/hmm.svg)](https://travis-ci.org/adrianulbona/hmm)

Besides the basic abstractions, a most probable state sequence solution is implemented based on the Viterbi algorithm.

### The library is hosted on Maven Central:

#### Maven
```xml
<dependency>
    <groupId>io.github.adrianulbona</groupId>
    <artifactId>hmm</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Gradle
```groovy
compile 'io.github.adrianulbona:hmm:0.1.0'
```

### How to use it:

#### Getting the most probable sequence of states based on a sequence of observations:

```java
Model<MedicalState, Symptom> model = WikipediaViterbi.INSTANCE.model;
List<Symptom> symptoms = asList(NORMAL, COLD, DIZZY);
List<MedicalState> evolution = new MostProbableStateSequenceFinder<>(model).basedOn(symptoms);
```

#### How to define a model: 

```java
public enum WikipediaViterbi {
	INSTANCE;

	public final Model<MedicalState, Symptom> model;

	WikipediaViterbi() {
		model = new Model<>(probabilityCalculator(), reachableStatesFinder());
	}

	public enum MedicalState implements State {
		HEALTHY,
		FEVER;
	}
	
	public enum Symptom implements Observation {
		NORMAL,
		COLD,
		DIZZY;
	}
	
	private ProbabilityCalculator<MedicalState, Symptom> probabilityCalculator() {
		return new ProbabilityCalculator<>(StartProbabilities.INSTANCE.data::get,
				EmissionProbabilities.INSTANCE.data::get, 
				TransitionProbabilities.INSTANCE.data::get);
	}

	private ReachableStateFinder<MedicalState, Symptom> reachableStatesFinder() {
		return observation -> asList(MedicalState.values());
	}

	private enum StartProbabilities {
		INSTANCE;

		public final Map<MedicalState, Double> data;

		StartProbabilities() {
			data = new HashMap<>();
			data.put(MedicalState.HEALTHY, 0.6);
			data.put(MedicalState.FEVER, 0.4);
		}
	}

	private enum TransitionProbabilities {
		INSTANCE;

		public final Map<Transition<MedicalState>, Double> data;

		TransitionProbabilities() {
			data = new HashMap<>();
			data.put(new Transition<>(MedicalState.HEALTHY, MedicalState.HEALTHY), 0.7);
			data.put(new Transition<>(MedicalState.HEALTHY, MedicalState.FEVER), 0.3);
			data.put(new Transition<>(MedicalState.FEVER, MedicalState.HEALTHY), 0.4);
			data.put(new Transition<>(MedicalState.FEVER, MedicalState.FEVER), 0.6);
		}
	}

	private enum EmissionProbabilities {
		INSTANCE;

		public final Map<Emission<MedicalState, Symptom>, Double> data;

		EmissionProbabilities() {
			data = new HashMap<>();
			data.put(new Emission<>(MedicalState.HEALTHY, Symptom.NORMAL), 0.5);
			data.put(new Emission<>(MedicalState.HEALTHY, Symptom.COLD), 0.4);
			data.put(new Emission<>(MedicalState.HEALTHY, Symptom.DIZZY), 0.1);
			data.put(new Emission<>(MedicalState.FEVER, Symptom.NORMAL), 0.1);
			data.put(new Emission<>(MedicalState.FEVER, Symptom.COLD), 0.3);
			data.put(new Emission<>(MedicalState.FEVER, Symptom.DIZZY), 0.6);
		}
	}
}
```
