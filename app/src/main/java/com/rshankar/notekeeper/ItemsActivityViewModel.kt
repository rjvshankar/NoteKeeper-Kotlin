@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.rshankar.notekeeper

import android.arch.lifecycle.ViewModel
import android.os.Bundle

/**
Created by rajiv on 4/6/20
 */
class ItemsActivityViewModel : ViewModel() {
    var isNewlyCreated = true
    var navDrawerDisplaySelectionName = this::class.simpleName + "::navDrawerDisplaySelection"
    var recentlyViewedNotesName = this::class.simpleName + "::recentlyViewedNotes"

    var navDrawerDisplaySelection = DEFAULT_NAV_ITEM_SELECTED

    private val maxRecentlyViewedNotes = 5
    var recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

    fun saveState(outState: Bundle) {
        outState.putInt(this.navDrawerDisplaySelectionName, this.navDrawerDisplaySelection)
        val noteIds = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState.putIntArray(this.recentlyViewedNotesName, noteIds)
    }

    fun restoreState(savedInstanceState: Bundle) {
        this.navDrawerDisplaySelection = savedInstanceState.getInt(this.navDrawerDisplaySelectionName)
        val noteIds = savedInstanceState.getIntArray(this.recentlyViewedNotesName)
        val noteList = DataManager.loadNotes(*noteIds)
        this.recentlyViewedNotes.addAll(noteList)
    }
}