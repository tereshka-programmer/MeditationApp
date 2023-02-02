package com.example.tranyapp.resourcemanage

import java.util.concurrent.Executor

/**
 * Resource manager can hold 1 instance of some resource <R> at a time.
 * - A resource can be assigned/replaced by using [setResource] call.
 * - A resource can be unassigned by [clearResource] call.
 * - A resource can be accessed via [consumeResource] call.
 * - A [ResourceManager] instance can be destroyed by [destroy] call.
 * For more details: see comments for methods [setResource],
 * [clearResource], [consumeResource] and [destroy].
 */
class ResourceManager<R>(

    private val executor: Executor,

    private val errorHandler: ErrorHandler<R>

) {

    private val consumers = mutableListOf<Consumer<R>>()
    private var resource: R? = null
    private var destroyed = false

    fun setResource(resource: R) = synchronized(this) {
        if (destroyed) return
        var localConsumers: List<Consumer<R>>
        do {
            localConsumers = ArrayList(consumers)
            consumers.clear()
            localConsumers.forEach { consumer ->
                processResource(consumer, resource)
            }
        } while (consumers.isNotEmpty())
        this.resource = resource
    }

    fun clearResource() = synchronized(this) {
        if (destroyed) return
        this.resource = null
    }

    fun consumeResource(consumer: Consumer<R>) = synchronized(this) {
        if (destroyed) return@synchronized
        val resource = this.resource
        if (resource != null) {
            processResource(consumer, resource)
        } else {
            consumers.add(consumer)
        }
    }

    fun destroy() = synchronized(this) {
        destroyed = true
        consumers.clear()
        resource = null
    }

    private fun processResource(consumer: Consumer<R>, resource: R) {
        executor.execute {
            try {
                consumer.invoke(resource)
            } catch (e: Exception) {
                errorHandler.onError(e, resource)
            }
        }
    }

}