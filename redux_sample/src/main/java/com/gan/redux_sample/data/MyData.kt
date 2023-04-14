package com.gan.redux_sample.data

import com.gan.redux.state.State

data class Info(var name: String) : State
data class Counter(var count: Int) : State
