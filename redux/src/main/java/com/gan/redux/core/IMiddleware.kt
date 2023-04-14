package com.gan.redux.core

import com.gan.redux.Action
import com.gan.redux.Dispatch
import com.gan.redux.Store

typealias MiddlewareInner = ((next: Dispatch) -> Dispatch)
typealias Middleware = (store: Store) -> ((next: Dispatch) -> Dispatch)

fun registerWrapperMiddleware(
    before: ((store: Store, action: Action) -> Unit)? = null,
    after: ((store: Store, action: Action) -> Unit)? = null
): (store: Store) -> MiddlewareInner {
    return fun(store: Store): MiddlewareInner {
        return fun(next: Dispatch): Dispatch {
            return fun(action: Action) {
                before?.invoke(store, action)
                next(action)
                after?.invoke(store, action)
            }
        }
    }
}

fun registerAroundMiddleware(
    around: (store: Store, next: Dispatch, action: Action) -> Unit
): (store: Store) -> MiddlewareInner {
    return fun(store: Store): MiddlewareInner {
        return fun(next: Dispatch): Dispatch {
            return fun(action: Action) {
                around.invoke(store, next, action)
            }
        }
    }
}
