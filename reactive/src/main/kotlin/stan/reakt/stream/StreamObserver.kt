package stan.reakt.stream

interface StreamObserver<T> {
    fun next(t: T)
    fun complete()
    fun error(t: Throwable)
}

fun <T> StreamObserver(n: (t: T) -> Unit, c: () -> Unit) = object: StreamObserver<T> {
    override fun next(t: T) {
        n(t)
    }
    override fun complete() {
        c()
    }
    override fun error(t: Throwable) {
    }
}
fun <T> StreamObserver(n: (t: T) -> Unit, c: () -> Unit, e: (t: Throwable) -> Unit) = object: StreamObserver<T> {
    override fun next(t: T) {
        n(t)
    }
    override fun complete() {
        c()
    }
    override fun error(t: Throwable) {
        e(t)
    }
}