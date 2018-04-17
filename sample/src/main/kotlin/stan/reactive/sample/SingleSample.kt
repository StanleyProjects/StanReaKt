package stan.reactive.sample

import stan.reakt.Tuple
import stan.reakt.single.SingleObservable
import stan.reakt.single.stream
import stan.reakt.stream.StreamObservable

fun sampleSingleObservable() {
    successObserveAnimals().subscribe {
        println("subscribe to animals SingleObservable success: $it")
    }
    errorSingleObservable().subscribe({
        println("subscribe to plants SingleObservable success: $it")//Not possible in this case!
    }, { error ->
        println("subscribe to plants SingleObservable with error: \"" + error.message + "\"")
    })
}

private fun successObserveAnimals(): SingleObservable<String> = SingleObservable("Cat Dog Penguin Platypus Elephant")

private fun successObservePlants(): SingleObservable<String> = SingleObservable { observer ->
    println("subscribe to plants SingleObservable in process...")
    observer.success("Oak Raspberries Bamboo Potatoes Corn Mango")
}

private fun errorSingleObservable(): SingleObservable<String> = SingleObservable { ->
    println("subscribe to SingleObservable in process...")
    throw Exception("something wrong :(")
}

fun sampleSingleObservableMap() {
    map().subscribe { println("animals: " + it) }
}

private fun map(): SingleObservable<List<String>> = successObserveAnimals().map { animals -> animals.split(" ") }

fun sampleSingleObservableFlat() {
    flat().subscribe { println("animals count = $it") }
}

private fun flat(): SingleObservable<Int> = map().flat { animals -> SingleObservable { -> animals.size } }

fun sampleSingleObservableChain() {
    chain().subscribe { println("time spent = $it") }
}

private fun chain(): SingleObservable<Long> {
    val time = System.nanoTime()
    return successObserveAnimals().map { it.split(" ") }.flat { animals -> SingleObservable { -> animals.size } }
            .chain({ println("flat $it animals success, move on...") },
                    successObservePlants().map { it.split(" ") }.flat { plants -> SingleObservable { -> plants.size } })
            .chain({ println("flat $it plants success now, move on...") }, {
                println("subscribe chain in process...")
                it.success(System.nanoTime() - time)
            })
}

fun sampleSingleObservableMerge() {
    mergeAnimalsWithPlants().subscribe { tuple ->
        println("merge animals with plants in process...")
        val list = ArrayList<String>(tuple.first())
        list.addAll(tuple.second())
        println("complete list: " + list)
    }
}

private fun mergeAnimalsWithPlants(): SingleObservable<Tuple<List<String>, List<String>>> {
    return successObserveAnimals().map { animals -> animals.split(" ") }.merge(successObservePlants().map { plants -> plants.split(" ") })
}

fun sampleSingleObservableToStreamObservable() {
    println("single to stream animals in process...")
    successObserveAnimals().stream { animals -> StreamObservable { -> animals.split(" ")}
    }.subscribe({
        print(it + " ")
    }, {
        print("\nsingle to stream animals complete success")
    })
}

fun sampleSingleObservableListToStreamObservable() {
    println("list to stream animals in process...")
    successObserveAnimals().map { it.split(" ") }.stream().subscribe({
        print(it + " ")
    }, {
        print("\nlist to stream animals complete success")
    })
}
fun sampleSingleObservableToNotifyObservable() {
    println("single to notify animals in process...")
    successObserveAnimals().notify().subscribe({
        println("single to notify animals complete success")
    })
}

fun sampleSingleObservableSchedulersDemonstration() {
    println("invoke on thread: " + Thread.currentThread().name)
    SingleObservable { ->
        println("subscribe on thread: " + Thread.currentThread().name)
        "Test"
    }.subscribeOn { Thread(it).start() }.observeOn { Thread(it).start() }.subscribe {
        println("observe on thread: " + Thread.currentThread().name)
        println("result: " + it)
    }
}