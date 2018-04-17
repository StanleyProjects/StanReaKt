package stan.reakt

interface Tuple<T, U> {
    fun first(): T
    fun second(): U
}

fun <T, U> Tuple(f: () -> T, s: () -> U) = object: Tuple<T, U> {
    override fun first(): T{
        return f()
    }
    override fun second(): U{
        return s()
    }
}
fun <T, U> Tuple(f: T, s: U) = object: Tuple<T, U> {
    override fun first(): T {
        return f
    }
    override fun second(): U {
        return s
    }
}