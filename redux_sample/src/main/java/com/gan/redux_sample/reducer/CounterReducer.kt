package com.gan.redux.reducer

import com.gan.redux.Action
import com.gan.redux.Reducer
import com.gan.redux_sample.data.Counter


class CounterReducer(initState: Counter) : Reducer<Counter>(initState) {

    override fun nextState(stateData: Counter, action: Action): Counter {
        when (action.type) {
            "INCREMENT" ->
                return stateData.apply {
                    count = (count ?: 0) + 1
                }
            "DECREMENT" ->
                return stateData.apply {
                   count = (count ?: 0) - 1
                }
            else -> return stateData
        }
    }

}
