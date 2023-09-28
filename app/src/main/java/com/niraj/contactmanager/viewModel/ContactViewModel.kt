package com.niraj.contactmanager.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niraj.contactmanager.DB.Contact
import com.niraj.contactmanager.DB.ContactDao
import com.niraj.contactmanager.DB.contactRepository
import com.niraj.contactmanager.handlers.ContactEvent
import com.niraj.contactmanager.handlers.ContactState
import com.niraj.contactmanager.handlers.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: contactRepository,
    private val dao: ContactDao
) : ViewModel() {

    private val _state = MutableStateFlow(ContactState())
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts = _sortType.flatMapLatest { sortType ->
        when(sortType) {
            SortType.FIRST_NAME -> dao.getContactsByFirstName()
            SortType.LAST_NAME -> dao.getContactsByLastName()
            SortType.PHONE_NUMBER -> dao.getContactsByPhoneNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when(event) {
            is ContactEvent.deleteContact -> {
                viewModelScope.launch {
                    repository.deleteContact(event.contact)
                }
            }
            is ContactEvent.SaveContact -> {
                val firstname = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if(firstname.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
                    return
                }
                val contact = Contact(
                    firstName = firstname,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    repository.upsertContact(contact)
                }
                _state.update {it ->
                    it.copy(
                        firstName = "",
                        lastName = "",
                        phoneNumber = "",
                        isAddingContact = false
                    )
                }

            }
            is ContactEvent.SortContacts -> {
                _sortType.update {
                    event.sortType
                }
            }
            is ContactEvent.hideDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = false
                    )
                }
            }
            is ContactEvent.setFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }
            is ContactEvent.setLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            is ContactEvent.setPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            is ContactEvent.showDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }
        }
    }

}