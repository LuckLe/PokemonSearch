@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.rocketreserver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rocketreserver.ui.theme.RocketReserverTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenRepository.init(this)
        setContent {
            RocketReserverTheme {
                Scaffold { paddingValues ->
                    Box(
                        Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        SplashUI(this@SplashActivity)
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch{
            val isFirstOpen  = TokenRepository.getIsFirstOpen()
            if (isFirstOpen){
                delay(3000)
            }
            TokenRepository.setIsFirstOpen(false)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }
}

@Composable
private fun SplashUI(context:Context) {
    Column {
        Text(text = context.getString(R.string.welcome), fontSize = 40.sp,color= Color.Black)
        Image(
            modifier = Modifier.padding(start = 10.dp, top = 40.dp),
            painter = painterResource(R.mipmap.hilton_logo), contentDescription = "")
    }
}
