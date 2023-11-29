package com.example.assignment4

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment4.ui.theme.Assignment4Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContent {
            Assignment4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignUpPage(this, auth = auth)
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()
            val intent = Intent(this, LoginActivity::class.java)

            // Add any extra data you want to pass to SecondActivity
            //intent.putExtra("auth", user)

            // Start SecondActivity
            ContextCompat.startActivity(this, intent, null)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(signUpActivity: SignUpActivity, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            // ... do anything you want here with `isKeyboardOpen`
            if (!isKeyboardOpen){
                focusManager.clearFocus()
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text="Sign Up Page",
            fontSize=40.sp,
            modifier = Modifier
                .padding(16.dp)
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester)
        )

        val cPasswordVisibility = remember { mutableStateOf(true) }
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            trailingIcon = {
                IconButton(onClick = {
                    cPasswordVisibility.value = !cPasswordVisibility.value
                }) {
                    Icon(
                        imageVector = if (cPasswordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "visibility"
                    )
                }
            },
            visualTransformation = if (cPasswordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester)
        )
        // SignUp button
        Button(
            onClick = {
                // Perform login logic here
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(signUpActivity, "Please enter Email.", Toast.LENGTH_SHORT).show()
                    return@Button;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(signUpActivity, "Please enter Password.", Toast.LENGTH_SHORT).show()
                    return@Button;
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(signUpActivity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            //val user = auth.currentUser
                            // Create an explicit intent to start SecondActivity
                            val intent = Intent(signUpActivity, LoginActivity::class.java)

                            // Add any extra data you want to pass to SecondActivity
                            //intent.putExtra("auth", user)

                            // Start SecondActivity
                            ContextCompat.startActivity(signUpActivity, intent, null)
                            //updateUI(user)
                            Toast.makeText(
                                signUpActivity,
                                "Account Creation: Success.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            signUpActivity.finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                signUpActivity,
                                "Authentication failed. " + task.exception?.message.toString(),
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign Up")
        }

        // Login Page button
        Button(
            onClick = {
                signUpActivity.finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Go back to Login Page")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Assignment4Theme {
        //Greeting2("Android")
    }
}