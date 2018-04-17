package stan.reakt.single

import stan.reakt.Tuple
import stan.reakt.notify.NotifyObservable
import stan.reakt.notify.NotifyObserver
import stan.reakt.stream.StreamObservable
import stan.reakt.stream.StreamObserver

abstract class SingleObservable<T> {
    abstract fun subscribe(o: SingleObserver<T>)
    fun subscribe(s: (t: T) -> Unit) {
        subscribe(SingleObserver(s))
    }

    fun subscribe(s: (t: T) -> Unit, e: (t: Throwable) -> Unit) {
        subscribe(SingleObserver(s, e))
    }

    fun <U> map(m: (t: T) -> U): SingleObservable<U> = object : SingleObservable<U>() {
        override fun subscribe(o: SingleObserver<U>) {
            this@SingleObservable.subscribe({
                o.success(m(it))
            }, {
                o.error(it)
            })
        }
    }

    fun <U> flat(f: (t: T) -> SingleObservable<U>): SingleObservable<U> = object : SingleObservable<U>() {
        override fun subscribe(o: SingleObserver<U>) {
            this@SingleObservable.subscribe({
                f(it).subscribe(o)
            }, {
                o.error(it)
            })
        }
    }
    fun <U> stream(f: (t: T) -> StreamObservable<U>): StreamObservable<U> = object : StreamObservable<U>() {
        override fun subscribe(o: StreamObserver<U>) {
            this@SingleObservable.subscribe({
                f(it).subscribe(o)
            }, {
                o.error(it)
            })
        }
    }
    fun notify(): NotifyObservable = object : NotifyObservable() {
        override fun subscribe(o: NotifyObserver) {
            this@SingleObservable.subscribe({
                o.notice()
            }, {
                o.error(it)
            })
        }
    }

    fun <U> chain(observer: SingleObserver<T>, observable: SingleObservable<U>): SingleObservable<U> = object : SingleObservable<U>() {
        override fun subscribe(o: SingleObserver<U>) {
            this@SingleObservable.subscribe({
                observer.success(it)
                observable.subscribe(o)
            }, {
                observer.error(it)
            })
        }
    }
    fun <U> chain(observer: SingleObserver<T>, observable: StreamObservable<U>): StreamObservable<U> = object : StreamObservable<U>() {
        override fun subscribe(o: StreamObserver<U>) {
            this@SingleObservable.subscribe({
                observer.success(it)
                observable.subscribe(o)
            }, {
                observer.error(it)
            })
        }
    }
    fun chain(observer: SingleObserver<T>, observable: NotifyObservable): NotifyObservable = object : NotifyObservable() {
        override fun subscribe(o: NotifyObserver) {
            this@SingleObservable.subscribe({
                observer.success(it)
                observable.subscribe(o)
            }, {
                observer.error(it)
            })
        }
    }
    fun <U> chain(s: (t: T) -> Unit, observable: SingleObservable<U>): SingleObservable<U> = chain(SingleObserver(s), observable)
    fun <U> chain(s: (t: T) -> Unit, observable: (o: SingleObserver<U>) -> Unit): SingleObservable<U> = chain(SingleObserver(s), SingleObservable(observable))
    fun <U> chain(s: (t: T) -> Unit, e: (t: Throwable) -> Unit, observable: SingleObservable<U>): SingleObservable<U> = chain(SingleObserver(s, e), observable)
    fun <U> chain(s: (t: T) -> Unit, e: (t: Throwable) -> Unit, observable: (o: SingleObserver<U>) -> Unit): SingleObservable<U> = chain(SingleObserver(s, e), SingleObservable(observable))

    fun <U> merge(observable: SingleObservable<U>): SingleObservable<Tuple<T, U>> = this@SingleObservable.flat { t ->
        observable.flat { u ->
            SingleObservable<Tuple<T, U>> { observer ->
                observer.success(Tuple(t, u))
            }
        }
    }

    fun subscribeOn(scheduler: (runnable: () -> Unit) -> Unit) = SingleObservable<T> { observer ->
        scheduler { this@SingleObservable.subscribe(observer) }
    }
    fun observeOn(scheduler: (runnable: () -> Unit) -> Unit) = SingleObservable<T> { observer ->
        this@SingleObservable.subscribe({
            scheduler { observer.success(it) }
        }, {
            scheduler { observer.error(it) }
        })
    }
}

fun <T> SingleObservable(s: (o: SingleObserver<T>) -> Unit): SingleObservable<T> = object : SingleObservable<T>() {
    override fun subscribe(o: SingleObserver<T>) {
        s(o)
    }
}
fun <T> SingleObservable(t: T): SingleObservable<T> = object : SingleObservable<T>() {
    override fun subscribe(o: SingleObserver<T>) {
        o.success(t)
    }
}
fun <T> SingleObservable(apply: () -> T): SingleObservable<T> = object : SingleObservable<T>() {
    override fun subscribe(o: SingleObserver<T>) {
        try {
            o.success(apply())
        } catch (t: Throwable) {
            o.error(t)
        }
    }
}
fun <T> SingleObservable<List<T>>.stream(): StreamObservable<T> = object : StreamObservable<T>() {
    override fun subscribe(o: StreamObserver<T>) {
        this@stream.subscribe({
            for (t in it) o.next(t)
            o.complete()
        }, {
            o.error(it)
        })
    }
}