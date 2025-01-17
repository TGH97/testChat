package com.winter.chatsystem.components

import android.annotation.SuppressLint

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.winter.chatsystem.logic.*
import java.util.*


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneToOne(
    navController: NavHostController,
    chatId: String
) {
    var textFieldValue by remember { mutableStateOf("") }
    val context = LocalContext.current
    val chatViewModel = ChatViewModel(context)
    val auth = Firebase.auth
    val user = auth.currentUser
    val currentUserEmail = user?.email

    if (chatViewModel.loading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navController.popBackStack()
                                }
                            )
                    )
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(45.dp)
                            .padding(6.dp)
                            .clip(CircleShape)
                            .border(
                                width = 4.dp,
                                MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = if (chatId.substringAfter("-") != currentUserEmail!!.split("@")[0]) {
                            chatId.substringAfter("-").capitalize()
                        } else {
                            chatId.substringBefore("-").capitalize()
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 27.sp,
                        modifier = Modifier
                    )
                }
                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 59.dp)
                )
            },
            content = {

                ChatScreen(chatId)


            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .padding(bottom = 80.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(69.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedTextField(
                            value = textFieldValue,
                            onValueChange = { textFieldValue = it },
                            placeholder = {
                                Text(
                                    text = "Write your message",
                                )

                            },
                            modifier = Modifier
                                .width(420.dp)
                                .padding(bottom = 4.5.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(30.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Msg",
                                    //tint = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                val message = textFieldValue
                                                val currentUser =
                                                    FirebaseAuth.getInstance().currentUser
                                                if (!message.isBlank()) {
                                                    chatViewModel.sendMessage(
                                                        chatId,
                                                        message,
                                                        currentUser!!.uid,
                                                    )
                                                    textFieldValue = ""

                                                }

                                                // chatViewModel.printMessages(chatId)


                                            }
                                            //tint = MaterialTheme.colorScheme.onPrimary,
                                        ))
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                //focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                //focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Icon(
                            Icons.Default.AddCircle, contentDescription = "Mic",
                            modifier = Modifier
                                .padding(start = 6.dp, end = 6.dp)
                                .size(35.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    //BottomNavBar(navController)
                }
            }
        )
    }
}

