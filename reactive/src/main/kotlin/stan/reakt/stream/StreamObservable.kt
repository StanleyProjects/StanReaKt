package stan.reakt.stream

import stan.reakt.single.SingleObservable
import stan.reakt.single.SingleObserver

abstract class StreamObservable<T> {
    abstract fun subscribe(o: StreamObserver<T>)
    fun subscribe(n: (t: T) -> Unit, c: () -> Unit) {
        subscribe(StreamObserver(n, c))
    }
    fun subscribe(n: (t: T) -> Unit, c: () -> Unit, e: (t: Throwable) -> Unit) {
        subscribe(StreamObserver(n, c, e))
    }

    fun <U> map(m: (t: T) -> U): StreamObservable<U> = object : StreamObservable<U>() {
        override fun subscribe(o: StreamObserver<U>) {
            this@StreamObservable.subscribe({
                o.next(m(it))
            }, {
                o.complete()
            }, {
                o.error(it)
            })
        }
    }

    fun filter(f: (t: T) -> Boolean): StreamObservable<T> = object : StreamObservable<T>() {
        override fun subscribe(o: StreamObserver<T>) {
            this@StreamObservable.subscribe({
                if (f(it)) {
                    o.next(it)
                }
            }, {
                o.complete()
            }, {
                o.error(it)
            })
        }
    }

    fun single(): SingleObservable<List<T>> = object : SingleObservable<List<T>>() {
        override fun subscribe(o: SingleObserver<List<T>>) {
            val list = ArrayList<T>()
            this@StreamObservable.subscribe({
                list.add(it)
            }, {
                o.success(list)
            }, {
                o.error(it)
            })
        }
    }
}

fun <T> StreamObservable(s: (o: StreamObserver<T>) -> Unit): StreamObservable<T> = object : StreamObservable<T>() {
    override fun subscribe(o: StreamObserver<T>) {
        s(o)
    }
}
fun <T> StreamObservable(t1: T,vararg array: T): StreamObservable<T> = object : StreamObservable<T>() {
    override fun subscribe(o: StreamObserver<T>) {
        o.next(t1)
        for (t in array) {
            o.next(t)
        }
        o.complete()
    }
}
fun <T> StreamObservable(list: List<T>): StreamObservable<T> = object : StreamObservable<T>() {
    override fun subscribe(o: StreamObserver<T>) {
        for (t in list) {
            o.next(t)
        }
        o.complete()
    }
}
fun <T> StreamObservable(apply: () -> List<T>): StreamObservable<T> = object : StreamObservable<T>() {
    override fun subscribe(o: StreamObserver<T>) {
        val list: List<T>
        try {
            list = apply()
        } catch (t: Throwable) {
            o.error(t)
            return
        }
        StreamObservable(list).subscribe(o)
    }
}
