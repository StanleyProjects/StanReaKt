package stan.reactive.sample

import stan.reakt.notify.NotifyObservable

fun sampleNotifyObservable() {
    successNotifyObservable().subscribe {
        println("subscribe to NotifyObservable success")
    }
    errorNotifyObservable().subscribe({
        println("subscribe to NotifyObservable success")//Not possible in this case!
    }, {t: Throwable ->
        System.out.println("subscribe to NotifyObservable with error: \"" + t.message + "\"")
    })
}
private fun successNotifyObservable(): NotifyObservable = NotifyObservable { ->
    println("subscribe to NotifyObservable in process...")
}
private fun errorNotifyObservable(): NotifyObservable = NotifyObservable { ->
    println("subscribe to NotifyObservable in process...")
    throw Exception("something wrong :(")
}