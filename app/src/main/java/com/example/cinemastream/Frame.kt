package com.example.cinemastream

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface Container<S, I, E> {
    fun CoroutineScope.subscribe(onEvent: suspend (E) -> Unit)
    val states: StateFlow<S>
    fun intent(intent: I): Job
}


abstract class MVIViewModel<S, I, E>(initial: S) : ViewModel(), Container<S, I, E> {

    private val _states: MutableStateFlow<S> = MutableStateFlow(initial)
    private val _events = MutableLiveData<E>()

    final override val states: StateFlow<S> = _states
    final override fun intent(intent: I): Job = viewModelScope.launch { reduce(intent) }
    final override fun CoroutineScope.subscribe(onEvent: suspend (E) -> Unit) {
        _events.observeForever { event ->
            launch { onEvent(event) }
        }
        onSubscribe()
    }

    protected fun event(event: E) {
        _events.value = event
    }

    protected fun state(block: S.() -> S) {
        val newState = _states.value.block()
        _states.value = newState
    }

    protected open fun CoroutineScope.onSubscribe() = Unit

    protected abstract suspend fun reduce(intent: I)
}


@Composable
fun <S, A> Container<S, *, A>.subscribe(onEvent: suspend (A) -> Unit) {
    val state by states.collectAsState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribe(onEvent)
            }
        }
    }
}
