# AndroidMailer

## A Java Android library to send email

This project is mainly use <b>javax.mail</b> and <b>javax.activation</b> packages to configure mail plugin and send email to target email address.

Also, this project uses <b>android.os</b> and <b>java.util.concurrent</b> to utilize effiecient background work processing.


## Implementation

You need to add following code in your project level build.gradle file:

<pre>
<code>  	
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
		
    }

}
</code>
</pre>

<br />

<br />

Note:Newer gradle android project prefer settings.gradle repository configuration over project level build.gradle configuration. In that case, you should add following code in your settings.gradle file:

<pre>
<code>  
pluginManagement {
	repositories {
        ...
        maven { url 'https://jitpack.io' }
              
    }
}
</code>
</pre>

<br />
<br />


After that, you should add following code in your app module's <b>build.gradle</b> file:

<pre>
<code> 	dependencies {
	        implementation 'com.github.nurujjamanpollob:AndroidMailer:1.0'
	}

</code>
</pre>

So, as far, your project configuration is done.
