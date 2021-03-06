package stan.reactive.sample

fun main(args: Array<String>) {
    println("\n\tSample NotifyObservable")
    sampleNotifyObservable()
    println("\n\tSample SingleObservable")
    sampleSingleObservable()
    println("\n\tSample StreamObservable")
    sampleStreamObservable()
    //
    println("\n\tSample SingleObservable Map operator")
    sampleSingleObservableMap()
    println("\n\tSample SingleObservable Flat operator")
    sampleSingleObservableFlat()
    println("\n\tSample SingleObservable Chain operator")
    sampleSingleObservableChain()
    println("\n\tSample SingleObservable Merge operator")
    sampleSingleObservableMerge()
    println("\n\tSample StreamObservable Map operator")
    sampleStreamObservableMap()
    println("\n\tSample StreamObservable Filter operator")
    sampleStreamObservableFilter()
    println("\n\tSample SingleObservable to StreamObservable operator")
    sampleSingleObservableToStreamObservable()
    println("\n\tSample SingleObservable List to StreamObservable operator")
    sampleSingleObservableListToStreamObservable()
    println("\n\tSample SingleObservable to NotifyObservable operator")
    sampleSingleObservableToNotifyObservable()
    println("\n\tSample StreamObservable to SingleObservable List operator")
    sampleStreamObservableToSingleObservableList()
    println("\n\tSample StreamObservable Schedulers Demonstration")
    sampleSingleObservableSchedulersDemonstration()
}