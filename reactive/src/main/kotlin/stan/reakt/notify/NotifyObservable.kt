package stan.reakt.notify

abstract class NotifyObservable {
    abstract fun subscribe(o: NotifyObserver)
    fun subscribe(n: () -> Unit) {
        subscribe(NotifyObserver(n))
    }
    fun subscribe(n: () -> Unit, e: (t: Throwable) -> Unit) {
        subscribe(NotifyObserver(n, e))
    }
}

fun NotifyObservable(s: (o: NotifyObserver) -> Unit): NotifyObservable = object: NotifyObservable() {
    override fun subscribe(o: NotifyObserver) {
        s(o)
    }
}
fun NotifyObservable(r: () -> Unit): NotifyObservable = object: NotifyObservable() {
    override fun subscribe(o: NotifyObserver) {
        try {
            r()
            o.notice()
        } catch (t: Throwable) {
            o.error(t)
        }
    }
}