# StanReaKt
Reactive realization observer pattern on kotlin (Redesign [StanReactive](https://github.com/StanleyProjects/StanReactive))

<img src="media/icon.png" width="128" height="128" />

# Pretty, simple, powerful

This project is based on:
- **NotifyObservable** - notify when event is triggered (not emit data)
- **SingleObservable** - emit only one object if work after the subscription was successful
- **StreamObservable** - emit stream data and notify about complete if work after the subscription was successful

Operators:
- **map** - convert observe object to other object
- **flat** - make other observable from observe object
- **chain** - run all observables in chain with order
- **merge** - observe tuple of two objects from two other observables when their subscription finish successful
- **filter** - emit only objects who satisfy condition

Observables can convert from\to other type observables + can create chains

see [sample](https://github.com/StanleyProjects/StanReaKt/tree/master/sample/src/main/kotlin/stan/reactive/sample)

All observables notify about error (with *Throwable*) if work after the subscription was not successful