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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private lateinit var appUpdateService: AppUpdateService
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateService = AppUpdateService(this)
        checkAndRoute()
    }

    private fun checkAndRoute() {
        val service = RetrofitClient.instance
        val currentVersionCode = getCurrentAppVersionCode()

        service.getAppUpdateDetails().enqueue(object : Callback<AppUpdateResponse> {
            override fun onResponse(
                call: Call<AppUpdateResponse>,
                response: Response<AppUpdateResponse>
            ) {
                if (response.isSuccessful) {
                    val appUpdateResponse = response.body()
                    val remoteVersionCode =
                        appUpdateResponse?.elements?.getOrNull(0)?.versionCode ?: -1

                    if (remoteVersionCode > currentVersionCode) {
                        appUpdateService.checkForAppUpdate()
                    } else {
                        routeToCastActivity()
                    }
                } else {
                    routeToCastActivity()
                }
            }

            override fun onFailure(call: Call<AppUpdateResponse>, t: Throwable) {
                routeToCastActivity()
            }
        })
    }

    private fun routeToCastActivity() {
        startActivity(Intent(this@MainActivity, CastActivity::class.java))
        finish()
    }

    private fun getCurrentAppVersionCode(): Int {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: Exception) {
            -1
        }
    }
}
