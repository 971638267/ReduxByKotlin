package com.gan.redux

import com.gan.redux.core.Middleware
import com.gan.redux.core.MiddlewareInner
import com.gan.redux.state.*


class IStore {
    companion object {
        /**
         * 用于合并Reducer
         */
        fun MutableList<Reducer<out State>>.combineReducers(): CombineReducer {
            return CombineReducer(StateContainer.allState(), this)
        }


        val createStore = fun(plan: Reducer<out State>): Store {
            var mState: State = StateContainer.allState()
            if (!(plan is CombineReducer)) {
                plan.initStatedata.save(mState)
            } else {
                mState = plan.initStatedata
                plan.mutableList.forEach {
                    it.initStatedata.save(mState)
                }
            }
            val listeners = mutableListOf<StateListener>()
            val subscribe = fun(listener: StateListener): () -> Unit {
                listeners.add(listener)
                return fun() {
                    val index = listeners.indexOf(listener)
                    listeners.removeAt(index)
                }
            }
            val dispatch = fun(action: Action) {
                mState = plan(mState, action)
                /*当 count 改变的时候，我们要去通知所有的订阅者*/
                listeners.forEach {
                    it()
                }
            }
            return Store(subscribe, dispatch, mState!!)
        }


        fun applyMiddleware( middlewares: MutableList<Middleware>): RewriteCreateStoreFunc {
            return fun(oldCreateStore: CreateStore): CreateStore {
                return fun(reducer: Reducer<out State>): Store {
                    val store = oldCreateStore(reducer)
                    var dispatch = store.dispatch
                    /* 实现 exception(time((logger(dispatch))))*/
                    middlewares.reverse()
                    //创建中间件
                    //模拟  val exception = exceptionMiddleware(store)
                    //        val logger = logMiddleware(store)
                    //        val time = timeMiddleware(store)
                    val chain = mutableListOf<MiddlewareInner>()
                    middlewares.forEach {
                        chain.add(it(store))
                    }
                    //反转一下 实现 exception(time((logger(dispatch))))
                    chain.reverse()
                    chain.forEach {
                        dispatch = it(dispatch);
                    }
                    /*2. 重写 dispatch*/
                    store.dispatch = dispatch;
                    return store
                }
            }
        }

        fun createStore(
            middlewares: MutableList<Middleware>,
            plans:MutableList<Reducer<out State>>
        ): Store {
           return applyMiddleware(middlewares)(createStore)(plans.combineReducers())
        }
    }

}


typealias RewriteCreateStoreFunc = (oldCreateStore: CreateStore) -> CreateStore
typealias CreateStore = (plan: Reducer<out State>) -> Store

typealias StateListener = () -> Unit
typealias Subscribe = (listener: StateListener) -> () -> Unit
typealias Dispatch = (action: Action) -> Unit

abstract class Reducer<T : State>(val initStatedata: T) : InnerReducer {

    override fun invoke(state: State, action: Action): State {
        val data = initStatedata.getState(state)
        val next = nextState(data, action)
        next.save(state)
        return state
    }

    abstract fun nextState(stateData: T, action: Action): T

}

class CombineReducer(var initState: State, val mutableList: MutableList<Reducer<out State>>) :
    Reducer<State>(initState) {
    override fun nextState(stateData: State, action: Action): State {
        mutableList.forEach {
            val state = it.initStatedata.getState(initState)
            val next = it.invoke(state, action)
            next.save(initState)
        }
        return stateData
    }
}
typealias  InnerReducer = (state: State, action: Action) -> State

data class Store(val subscribe: Subscribe, var dispatch: Dispatch, val state: State)
data class Action(val type: String, var data: Any? = null)

inline fun <reified T : State> Store.subscribe(crossinline callBack: (state: T) -> Unit): () -> Unit {
    return this.subscribe {
        val data = state.getData(T::class.java) as T
        callBack(data)
    }
}



