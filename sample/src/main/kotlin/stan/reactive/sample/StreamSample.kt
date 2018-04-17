package stan.reactive.sample

import stan.reakt.stream.StreamObservable

fun sampleStreamObservable() {
    successObserveAnimals().subscribe({ animal ->
        println(animal)
    }, {
        println("subscribe to StreamObservable complete success")
    })
    errorStreamObservable().subscribe({ integer ->
        println(integer)
    }, {
        println("subscribe to StreamObservable complete success")//Not possible in this case!
    }, { error ->
        System.out.println("subscribe to StreamObservable with error: \"" + error.message + "\"")
    })
}

private fun successObserveAnimals(): StreamObservable<String> = StreamObservable("Cat", "Dog", "Penguin", "Platypus", "Elephant", "Camel", "Goat", "Lion", "Turtle", "Crab")

private fun errorStreamObservable(): StreamObservable<Int> = StreamObservable { observer ->
    println("subscribe to StreamObservable in process...\nvalues:")
    observer.next(1)
    observer.next(2)
    observer.next(3)
    observer.next(4)
    observer.next(5)
    observer.next(6)
    observer.error(Exception("something wrong :("))
}

fun sampleStreamObservableMap() {
    print("map animals in process...\n")
    successObserveAnimals().map { it.length }.subscribe({ print(it) }, { print("\nmap animals complete success") })
}

fun sampleStreamObservableFilter() {
    print("filter animals in process...\n")
    successObserveAnimals().filter { it.isNotEmpty() && it.toCharArray()[0]
            .toLowerCase()
            .compareTo('c') == 0 }.subscribe({
        print(it + " ")
    }, {
        print("\nfilter animals complete success")
    })
}

fun sampleStreamObservableToSingleObservableList() {
    println("stream to list animals in process...")
    successObserveAnimals().single().subscribe {
        println("animals: " + it)
    }
}