
<div align="center">


  <h1>LoginWithDone</h1>

  <p>
    Get user data with one click
  </p>

</div>

<!-- About the Library -->
## :star2: About the Project
 <p>
    This library let users to share their login information with your application,
    Your application can get user Data. 
This library is provided for Done platform and wont work for other platforms.
 after setting up library user can click on LoginWithDoneButton and share their information with your application
  </p>


<!-- TechStack -->
### :space_invader: How to use


Installation
-----------
add this dependency in your build.gradle(app level)
```
dependencies {
...
	        implementation 'com.github.humatv:LoginWithHumaLib:4.0.0'
	...
	}
```

and add repository in your project level

```
 repositories {
        google()
       ...
        maven { url 'https://jitpack.io' }
    }
```

sync and build your project then follow below steps:

you implement this by two method as mention below: 

1. Method 1 (Use LoginWithDoneButton):

Add LoginWithDoneButton in your XML Code like below
```
<ir.huma.loginwithhuma.LoginWithDoneButton
        android:id="@+id/loginWithButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" 
        app:clientKey="YourClientKey"
         />
```



- and you can use it in your code like below:

```

 button?.apply {
            setClientKey("YourClientKey") 
            setOnClickListener {
               // Do What ever when user click on button
            }
            setOnLoginListener(object :OnLoginListener{
                override fun onLogin(code: String?) {
                   // user successfully share the information with your application 
                   // use this temporary code to request user data from api
                   // you can find API documentation at end of this tutorial   
                }

                override fun onFail(
                    message: String?,
                    status: TemporaryCodeResponse.ResponseStatus?,
                ) {
                    // login has been failed by an issue
                }
            })
        }

```
- replace "YourClientKey" with proper string (call Done Support for getting it)
- you can set the clientId both in your code or in XML layout file
- 

. 

1. Method 2 (Use YourButton):

You can use your button style and call LoginWithDone when ever you need,
You can Login when ever user click on a button or at the first interact with user, it depends on your app situation.

For this You can Use a Button and use "@style/DoneButton" and also you can override or customize style attributes based on your UI
```
 <Button
        android:id="@+id/loginWithButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        style="@style/DoneButton"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="10dp" />
```
and in your code you can call LoginWithDone when ever you need like below:
```
 val button = findViewById<Button>(R.id.loginWithButton)
 button?.setOnClickListener {
            
            LoginWithDone(
                context
            ).setClientKey("YourClientKey")
                .setOnLoginListener(object : OnLoginListener {
                    override fun onLogin(code: String?) {
                        // user successfully share the information with your application 
                        // use this temporary code to request user data from api
                        // you can find API documentation at end of this tutorial 
                    }

                    override fun onFail(
                        message: String?,
                        status: TemporaryCodeResponse.ResponseStatus?
                    ) {
                       // login has been failed by an issue
                    }
                }).send()
        }

```

- replace "YourClientKey" with proper string (call Done Support for getting it)
- you can set the clientId both in your code or in XML layout file
-

.


Follow Documentation 
-----
for getting user Data you can request an API which documented in below link
<a href="https://drive.google.com/file/d/1yZ7WwgV6gA59YbQUQs3ub0pUCPqGHOLf/view?usp=sharing">
Api Documentation Link
</a>

<!-- Contact -->
## :handshake: Contact

BaseTeam Done
feel free to call our support center in Base team when ever you need


