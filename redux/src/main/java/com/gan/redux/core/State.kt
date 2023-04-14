package com.gan.redux.state



interface State {
    fun getTag(): String {
        return this::class.java.name
    }

    fun <T> getData(clazz: Class<T>): T {
        if (this is StateContainer) {
            return states[clazz.name] as T
        } else {
            return this as T
        }
    }
}

class StateContainer : State {
    val states = mutableMapOf<String, State>()

    companion object {
        fun allState(): State {
            return StateContainer()
        }
    }
}

fun <T : State> T.getState(all: State): T {
    if (this.getTag() == StateContainer::class.java.name) {
        return this
    }
    if (all is StateContainer) {
        return all.states[this.getTag()] as T
    } else {
        return this
    }

}

fun <T : State> T.save(all: State) {
    if (all is StateContainer) {
        all.states[this.getTag()] = this
    }
}


