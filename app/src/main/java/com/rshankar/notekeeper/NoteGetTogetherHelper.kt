package com.rshankar.notekeeper

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log

/**
Created by rajiv on 4/6/20
 */
class NoteGetTogetherHelper(
    val context: Context,
    val lifecycle: Lifecycle
    ) : LifecycleObserver
{
    init {
        lifecycle.addObserver(this)
    }

    private val tag = this::class.simpleName
    private var currentLat = 0.0
    private var currentLong = 0.0

    val locManager = PseudoLocationManager(context) { lat, long ->
        currentLat = lat
        currentLong = long
        Log.d(tag, "Location Callback Lat: $lat, Long: $long")
    }

    private val msgManager = PseudoMessagingManager(context)
    var msgConnection: PseudoMessagingConnection? = null

    fun sendMessage(note: NoteInfo) {
        val getTogetherMessage = "$currentLat | $currentLong | ${note.title} | ${note.course?.title}"
        msgConnection?.send(getTogetherMessage)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler() {
        Log.d(tag, "startHandler")
        locManager.start()
        msgManager.connect { connection ->
            Log.d(tag, "Connection callback - Lifecycle State: ${lifecycle.currentState}")
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                msgConnection = connection
            else connection.disconnect()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler() {
        Log.d(tag, "stopHandler")
        locManager.stop()
        msgConnection?.disconnect()
    }
}