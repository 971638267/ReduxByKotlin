package com.gan.redux

import android.util.Log
import com.gan.redux.IStore.Companion.applyMiddleware
import com.gan.redux.IStore.Companion.combineReducers
import com.gan.redux.middleware.*
import com.gan.redux.reducer.CounterReducer
import com.gan.redux.reducer.InfoReducer
import com.gan.redux.state.*
import com.gan.redux_sample.data.Counter
import com.gan.redux_sample.data.Info

class Test {
    fun test() {

        val counterReducer = CounterReducer(Counter(1))
        val infoReducer = InfoReducer(Info("name"))

        /*把定义好的plan函数传入*/
        val list = mutableListOf<Reducer<out State>>(counterReducer,infoReducer)


        /*没有中间件的 createStore*/
        val store = IStore.createStore(counterReducer)

        val store2= applyMiddleware(mutableListOf(exceptionMiddleware1, timeMiddleware1, logMiddleware1))(IStore.createStore)(list.combineReducers())

        val store3 = IStore.createStore(mutableListOf(exceptionMiddleware1, timeMiddleware1, logMiddleware1),mutableListOf(counterReducer,infoReducer))

        store2.subscribe {
            val info = store2.state.getData(Info::class.java)
            Log.d("redux====1", "info.name=${info?.name}");
        }

        val unsubscribe = store2.subscribe<Counter> {
            Log.d("redux====2", "counter.count=${it?.count}");
        }
        /*自增*/
        store2.dispatch(Action("INCREMENT"))
        unsubscribe()
        /*自减*/
        store2.dispatch(Action("DECREMENT"))
        /*我想随便改 计划外的修改是无效的！*/
        store2.dispatch(Action("abc"))
        store2.dispatch(Action("SET_NAME", "我的新设置的名字"))


    }
}

