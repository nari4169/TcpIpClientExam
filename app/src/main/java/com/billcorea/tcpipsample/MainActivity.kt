package com.billcorea.tcpipsample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.billcorea.tcpipsample.ui.theme.TcpIpSampleTheme
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.UnknownHostException
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {

    private var ipAddress : String = "192.168.0.2"
    lateinit var socket : Socket

    /**
     * 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
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TcpIpSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column( modifier = Modifier.fillMaxSize()) {
                        Greeting("Android")
                        IconButton(onClick = {
                            doTcpIpOpen()
                        }) {
                            Icon(imageVector = Icons.Outlined.Start, contentDescription = "Start")
                        }
                        IconButton(onClick = {
                            doSendToServer()
                        }) {
                            Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send")
                        }
                        IconButton(onClick = {
                            doQuitToServer()
                        }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close")
                        }
                        IconButton(onClick = {
                            doClose()
                        }) {
                            Icon(imageVector = Icons.Outlined.Stop, contentDescription = "Stop")
                        }
                    }
                }
            }
        }
    }

    private fun doQuitToServer() {
        Log.e("", "doQuitToServer ...")
        GlobalScope.launch {
            try {
                val writer = OutputStreamWriter(socket.getOutputStream())
                writer.write("finished")
                writer.flush()
            } catch (e : IOException) {
                Log.e("", "네트워크 응답 없음")
            } catch (e : UnknownHostException) {
                Log.e("", "알 수 없는 호스트 IP")
            } catch (e : SecurityException) {
                Log.e("", "보안 접속 오류 proxy 접속 거부")
            } catch (e : IllegalArgumentException) {
                Log.e("", "server port range 0 ~ 65535 ")
            } catch (e : Exception) {
                Log.e("", "error ${e.localizedMessage}")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun doSendToServer() {
        Log.e("", "doSendToServer ...")
        GlobalScope.launch {
            try {
                val writer = OutputStreamWriter(socket.getOutputStream())
                writer.write("hello server")
                writer.flush()
            } catch (e : IOException) {
                Log.e("", "네트워크 응답 없음")
            } catch (e : UnknownHostException) {
                Log.e("", "알 수 없는 호스트 IP")
            } catch (e : SecurityException) {
                Log.e("", "보안 접속 오류 proxy 접속 거부")
            } catch (e : IllegalArgumentException) {
                Log.e("", "server port range 0 ~ 65535 ")
            } catch (e : Exception) {
                Log.e("", "error ${e.localizedMessage}")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun doClose() {
        Log.e("", "doClose ...")
        GlobalScope.launch {
            try {
                socket.close()
            } catch (e : IOException) {
                Log.e("", "네트워크 응답 없음")
            } catch (e : UnknownHostException) {
                Log.e("", "알 수 없는 호스트 IP")
            } catch (e : SecurityException) {
                Log.e("", "보안 접속 오류 proxy 접속 거부")
            } catch (e : IllegalArgumentException) {
                Log.e("", "server port range 0 ~ 65535 ")
            } catch (e : Exception) {
                Log.e("", "error ${e.localizedMessage}")
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun doResponse() {
        var ip = ""

        Log.e("", "doResponse ...")

        GlobalScope.launch {
            try {

                val byteArr = ByteArray(100)

                while(true) {

                    val input = withContext(Dispatchers.IO) {
                        socket.getInputStream()
                    }
                    ip = socket.inetAddress.hostAddress as String
                    val byte = withContext(Dispatchers.IO) {
                        input.read(byteArr)
                    }

                    val data = String(byteArr, 0, byte, Charset.forName("UTF-8"))

                    Log.e("", "ip=$ip readData=$byte $data" )
                }
            } catch (e : IOException) {
                Log.e("", "read error = ${e.localizedMessage}")
            } catch (e : StringIndexOutOfBoundsException) {
                Log.e("", "read error = ${e.localizedMessage}")
                doClose()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun doTcpIpOpen() {

        Log.e("", "doTcpIpOpen ...")

        GlobalScope.launch {

            try{
                socket = Socket( ipAddress, 3000)

                doResponse()

            } catch (e : IOException) {
                Log.e("", "네트워크 응답 없음")
            } catch (e : UnknownHostException) {
                Log.e("", "알 수 없는 호스트 IP")
            } catch (e : SecurityException) {
                Log.e("", "보안 접속 오류 proxy 접속 거부")
            } catch (e : IllegalArgumentException) {
                Log.e("", "server port range 0 ~ 65535 ")
            } catch (e : Exception) {
                Log.e("", "error ${e.localizedMessage}")
            }

            Log.e("", "doTcpIpOpen end...")
        }
    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TcpIpSampleTheme {
        Greeting("Android")
    }
}