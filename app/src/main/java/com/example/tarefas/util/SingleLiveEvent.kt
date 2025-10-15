package com.example.tarefas.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T> : MutableLiveData<T>() {

    // Uma flag atômica para controlar se há um evento pendente a ser consumido.
    // Usamos AtomicBoolean para garantir que as operações sejam seguras entre múltiplas threads (thread-safe).
    private val pending = AtomicBoolean(false)


     // Sobrescrevemos o metodo observe para controlar quando o observador deve ser notificado.

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w("SingleLiveEvent", "Múltiplos observadores foram registrados, mas apenas um será notificado sobre as mudanças.")
        }

        // Observa o MutableLiveData interno, mas com uma lógica customizada.
        super.observe(owner, Observer { t ->
            // Só notifica o observador se houver um evento pendente.
            // A função 'compareAndSet' verifica se o valor atual é 'true' e, se for, o define como 'false' em uma única operação atômica.
            // Isso garante que o evento seja consumido apenas uma vez.
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    /**
     * Sobrescrevemos o setValue para indicar que um novo evento está pendente para ser consumido.
     */
    @MainThread
    override fun setValue(t: T?) {
        pending.set(true) // Define que há um evento pendente.
        super.setValue(t)
    }
}