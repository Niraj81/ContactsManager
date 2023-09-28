package com.niraj.contactmanager.handlers

import com.niraj.contactmanager.DB.Contact

data class ContactState (
    val contacts: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
    val sortType: SortType = SortType.FIRST_NAME
)
