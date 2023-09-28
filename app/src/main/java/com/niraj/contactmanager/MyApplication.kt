package com.niraj.contactmanager

import android.app.Application
import androidx.room.util.copy
import com.niraj.contactmanager.DB.contactRepository
import com.niraj.contactmanager.handlers.ContactEvent
import com.niraj.contactmanager.handlers.ContactState
import com.niraj.contactmanager.handlers.SortType
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltAndroidApp
class MyApplication(): Application() {

}