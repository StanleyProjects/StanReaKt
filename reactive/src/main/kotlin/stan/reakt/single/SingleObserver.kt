package stan.reakt.single

interface SingleObserver<T> {
    fun success(t: T)
    fun error(t: Throwable)
}

fun <T> SingleObserver(s: (t: T) -> Unit) = object: SingleObserver<T> {
    override fun success(t: T) {
        s(t)
    }
    override fun error(t: Throwable) {
    }
}
fun <T> SingleObserver(s: (t: T) -> Unit, e: (t: Throwable) -> Unit) = object: SingleObserver<T> {
    override fun success(t: T) {
        s(t)
    }
    override fun error(t: Throwable) {
        e(t)
    }
}