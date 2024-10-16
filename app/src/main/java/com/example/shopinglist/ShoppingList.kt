package com.example.shopinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: String,
    var isEditing: Boolean = false
)


@Composable
fun ShoppingListApp() {
    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Add Item")
        }
        Box() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItem) { item ->
                    if (item.isEditing) {

                        ShoppingItemEditor(item, onEditComplete = { editedName, editedQuantity ->
                            sItem = sItem.map { it.copy(isEditing = false) }
                            val editedItem = sItem.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity.toString()
                            }
                        })
                    } else {
                        ShoppingListItem(item = item, onEditClick = {
                            sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                        }, onDeleteClick = {
                            sItem = sItem - item
                        })
                    }
                }
            }
        }
    }
    when {
        showDialog -> AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val item = ShoppingItem(
                                id = sItem.size + 1,
                                name = itemName,
                                quantity = itemQuantity,
                            )
                            sItem += item
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        singleLine = true,
                        keyboardActions = KeyboardActions(onNext = {}),
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onValueChange = {
                            itemName = it
                        },
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onValueChange = {
                            itemQuantity = it
                        },
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuality by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {
                    editedName = it
                },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuality,
                onValueChange = {
                    editedQuality = it
                },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuality.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, color = Color(0XFF018786)),
                shape = RoundedCornerShape(12),
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.name, modifier = Modifier.padding(8.dp))
        Text("Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}