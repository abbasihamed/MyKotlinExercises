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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
    val sItem = remember { mutableStateOf(listOf<ShoppingItem>()) }
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
        Box(modifier = Modifier.background(color = Color.Red)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItem.value) {
                    ShoppingListItem(item = it, {}, {})
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
                                id = sItem.value.size + 1,
                                name = itemName,
                                quantity = itemQuantity,
                            )
                            sItem.value += item
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
    ) {
        Text(item.name, modifier = Modifier.padding(8.dp))
    }
}