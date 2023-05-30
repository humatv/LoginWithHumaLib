package ir.huma.loginwithhuma

import android.content.Context
import org.junit.Assert.*

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class LoginWithDoneTest {
    @Mock
    private val context: Context = Mockito.mock(Context::class.java)
    @Test
    fun testConvertJsonToObject() {
        val response = "{\"isSuccess\":true,\"temporaryCode\":\"ce340e3d5e52e244d50062c262f707cb\",\"status\":\"Success\"}"
       val result =  LoginWithDone(context).convertJsonToObject(response)
        println(result.errorMessage)
    }
}