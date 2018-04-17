package stan.reakt.notify

interface NotifyObserver {
    fun notice()
    fun error(t: Throwable)
}

fun NotifyObserver(n: () -> Unit) = object: NotifyObserver {
    override fun notice() {
        n()
    }
    override fun error(t: Throwable) {
    }
}
fun NotifyObserver(n: () -> Unit, e: (t: Throwable) -> Unit) = object: NotifyObserver {
    override fun notice() {
        n()
    }
    override fun error(t: Throwable) {
        e(t)
    }
}