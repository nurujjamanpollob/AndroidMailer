-keep class com.sun.mail.imap.** {*;}
-keep class com.sun.mail.smtp.** {*;}
-keep class com.sun.mail.handlers.** {*;}
-keep class javax.activation.** {*;}

-dontwarn com.sun.mail.**
-dontwarn org.apache.harmony.awt.**
-dontwarn javax.security.sasl.**
-dontwarn java.awt.**
-dontwarn java.beans.**