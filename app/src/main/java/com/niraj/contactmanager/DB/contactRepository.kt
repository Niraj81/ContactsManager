package com.niraj.contactmanager.DB

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class contactRepository @Inject constructor(
    private val dao: ContactDao
) {

    suspend fun deleteContact(contact: Contact) {
        dao.deleteContact(contact)
    }
    suspend fun upsertContact(contact: Contact) {
        dao.upsertContact(contact)
    }

    fun getContactsByFirstName() : Flow<List<Contact>> {
        return dao.getContactsByFirstName()
    }
    fun getContactsByLastName() : Flow<List<Contact>> {
        return dao.getContactsByLastName()
    }
    fun getContactsByPhoneNumber() : Flow<List<Contact>> {
        return dao.getContactsByPhoneNumber()
    }

}