package com.niraj.contactmanager

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.niraj.contactmanager.handlers.ContactEvent
import com.niraj.contactmanager.handlers.ContactState
import com.niraj.contactmanager.handlers.SortType
import com.niraj.contactmanager.ui.theme.ContactManagerTheme
import com.niraj.contactmanager.viewModel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val contactViewModel: ContactViewModel = viewModel()
                    val state by contactViewModel.state.collectAsState()
                    MainScreen(state = state, onEvent = contactViewModel::onEvent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    modifier: Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ContactEvent.hideDialog)
        },
        confirmButton = {
             Button(onClick = { 
                 onEvent(ContactEvent.SaveContact)
             }) {
                 Text(text = "Save")
             }
        },
        title = {
            Text(text = "Add Contact")
        },
        text = {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.firstName,
                    onValueChange = {
                        onEvent(ContactEvent.setFirstName(it))
                    },
                    placeholder = {
                        Text(text = "First Name")
                    }
                )
                TextField(
                    value = state.lastName,
                    onValueChange = {
                        onEvent(ContactEvent.setLastName(it))
                    },
                    placeholder = {
                        Text(text = "Last Name")
                    }
                )
                TextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onEvent(ContactEvent.setPhoneNumber(it))
                    },
                    placeholder = {
                        Text(text = "Phone Number")
                    }
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(ContactEvent.hideDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    ) 
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.showDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "Add Contact"
                )
            }
        }
    ) {it ->
        if(state.isAddingContact) {
            AddContactDialog(state = state, onEvent = onEvent, modifier = Modifier)
        }
        LazyColumn(
            contentPadding = it,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TopRow(state = state, onEvent = onEvent)
            }
            items(state.contacts) {contact ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column (
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${contact.firstName} ${contact.lastName}",
                            fontSize = 20.sp
                        )
                        Text(text = contact.phoneNumber, fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        onEvent(ContactEvent.deleteContact(contact))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Contact Number of ${contact.firstName}"
                        )
                    }
                }
            }
            
        }
    }
}



@Composable
fun TopRow(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortType.values().forEach { sortType ->
            Row(
                modifier = Modifier.clickable {
                    onEvent(ContactEvent.SortContacts(sortType))
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = sortType == state.sortType,
                    onClick = {
                        Log.d("CHANGE", sortType.name)
                        onEvent(ContactEvent.SortContacts(sortType))
                    }
                )
                Text(text = sortType.name)
            }
        }
    }
}