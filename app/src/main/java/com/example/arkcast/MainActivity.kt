package com.example.arkcast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arkcast.ui.theme.ArkCastTheme
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!launchScreenCast()){
            setContent {
                ScreenCastHelperUI(
                    onTryAgainClick = { launchScreenCast() },
                    onOpenSettingsClick = { openSettings()}
                )
            }
        }
    }

    private fun launchScreenCast(): Boolean {
        val intents = listOf(
            //Google/AOSP Cast Intent
            Intent("android.settings.CAST_SETTINGS"),

            //Fallback to standard settings
            Intent(Settings.ACTION_CAST_SETTINGS),
            Intent(Settings.ACTION_DISPLAY_SETTINGS),
            Intent(Settings.ACTION_WIRELESS_SETTINGS),
        )

        // Try each intent until one works
        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Close app after launching
                return true
            } catch (e: Exception) {
                //Try the next one if the intent didn't work
                continue
            }
        }

        return false
    }

    private fun openSettings(){
        try {
            val settingsIntent = Intent(Settings.ACTION_SETTINGS)
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(settingsIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Couldn't open settings", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun ScreenCastHelperUI(onTryAgainClick: () -> Unit, onOpenSettingsClick: () -> Unit) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Couldn't launch screen cast settings",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onTryAgainClick){
                    Text("Try Again")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = onOpenSettingsClick) {
                    Text("Open Settings")
                }
            }
        }
    }
}
