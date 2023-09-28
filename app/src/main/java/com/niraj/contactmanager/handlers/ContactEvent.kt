package com.niraj.contactmanager.handlers

import com.niraj.contactmanager.DB.Contact

sealed interface ContactEvent {
    object SaveContact: ContactEvent
    data class setFirstName(val firstName: String) : ContactEvent
    data class setLastName(val lastName: String) : ContactEvent
    data class setPhoneNumber(val phoneNumber: String) : ContactEvent
    object showDialog : ContactEvent
    object hideDialog : ContactEvent
    data class SortContacts(val sortType: SortType) : ContactEvent
    data class deleteContact(val contact: Contact) : ContactEvent
}