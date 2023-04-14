package com.gan.redux.reducer

import com.gan.redux.Action
import com.gan.redux.Reducer
import com.gan.redux_sample.data.Info

class InfoReducer(val initState: Info)  : Reducer<Info>(initState) {

    override fun nextState(stateData: Info, action: Action): Info {
        when (action.type) {
            "SET_NAME" ->
                return stateData.apply {
                    name = action?.data as String
                }
            else -> return stateData
        }
    }
}
