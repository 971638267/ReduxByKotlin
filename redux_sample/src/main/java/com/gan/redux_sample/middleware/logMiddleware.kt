package com.gan.redux.middleware

import android.util.Log
import com.gan.redux.*
import com.gan.redux.core.MiddlewareInner
import com.gan.redux.core.registerAroundMiddleware
import com.gan.redux.core.registerWrapperMiddleware
import com.gan.redux_sample.data.Counter
import java.util.*

val logMiddleware = fun(store: Store): MiddlewareInner {
    return fun(next: Dispatch): Dispatch {
        return fun(action: Action) {
            Log.d("redux====修改之前", store.state.getData(Counter::class.java).toString())
            Log.d("redux====", action.type.toString())
            next(action)
            Log.d("redux====修改之后", store.state.getData(Counter::class.java).toString())
            Log.d("redux====","         \n")
        }
    }
}
val logMiddleware1 = registerWrapperMiddleware({ store, action ->
    Log.d("redux====修改之前", store.state.getData(Counter::class.java).toString())
    Log.d("redux====", action.type.toString())
}) { store, action ->
    Log.d("redux====修改之后", store.state.getData(Counter::class.java).toString())
    Log.d("redux====","         \n")
}


val timeMiddleware = fun(store: Store): MiddlewareInner {
    return fun(next: Dispatch): Dispatch {
        return fun(action: Action) {
            Log.d("redux====", Date().getTime().toString());
            next(action);
        }
    }
}

val timeMiddleware1 = registerWrapperMiddleware (after = {s, a ->
    Log.d("redux====", Date().getTime().toString());
})


//创建包裹类型的中间件方式1
val exceptionMiddleware = fun(store: Store): MiddlewareInner {
    return fun(next: Dispatch): Dispatch {
        return fun(action: Action) {
            try {
                next(action)
            } catch (e: Exception) {
                Log.e("redux====", "错误报告: ${e.message}")
            }
        }
    }
}

//创建包裹类型中间件方式2
val exceptionMiddleware1 = registerAroundMiddleware { store, next, action ->
    try {
        next(action)
    } catch (e: java.lang.Exception) {
        Log.e("redux====", "错误报告: ${e.message}")
    }
}





