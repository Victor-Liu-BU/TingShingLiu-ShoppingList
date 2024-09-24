package com.example.tingshingliu_shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tingshingliu_shoppinglist.ui.theme.TingShingLiuShoppingListTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TingShingLiuShoppingListTheme {
                    ShoppingList()
            }
        }
    }
}

@Composable
fun ShoppingList() {
    var itemName by remember { mutableStateOf(TextFieldValue("")) }
    var itemQuantity by remember { mutableStateOf(TextFieldValue("")) }
    var shoppingList by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = itemQuantity,
                onValueChange = { itemQuantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (itemName.text.isNotEmpty() && itemQuantity.text.isNotEmpty()) {
                    shoppingList = shoppingList + Pair(itemName.text, itemQuantity.text)
                    itemName = TextFieldValue("")
                    itemQuantity = TextFieldValue("")
                    coroutineScope.launch {
                        showSnackbar(snackbarHostState, "Added!")
                    }
                }
            }) {
                Text("Add Item")
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(shoppingList.size) { index ->
                    ShoppingListItem(
                        name = shoppingList[index].first,
                        quantity = shoppingList[index].second
                    )
                }
            }
        }
    }
}
@Composable
fun ShoppingListItem(name: String, quantity: String) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name)
            Text(text = "Quantity: $quantity")
        }
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}
suspend fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    snackbarHostState.currentSnackbarData?.dismiss()
    snackbarHostState.showSnackbar(message)
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TingShingLiuShoppingListTheme {
        ShoppingList()
    }
}