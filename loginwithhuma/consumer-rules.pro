# Consumer rules shipped with loginwithhuma. They are applied to apps that depend on
# this library and build with R8 (minifyEnabled true).

# AuthV2 parses the Done service response into TemporaryCodeResponse with Jackson +
# jackson-module-kotlin via reflection. Without these rules R8 renames the class,
# makes it abstract, strips its constructor and kotlin.Metadata, and parsing fails with
# "Cannot construct instance of `X.y` (no Creators, like default constructor, exist)".
-keep class ir.huma.loginwithhuma.TemporaryCodeResponse { *; }
-keep enum ir.huma.loginwithhuma.TemporaryCodeResponse$ResponseStatus { *; }

# AIDL interfaces used to talk to Done Store / Done UI.
-keep interface ir.huma.humastore.ILoginWithHumaService { *; }
-keep class ir.huma.humastore.ILoginWithHumaService$* { *; }
-keep interface tech.done.user.ILoginWithDoneService { *; }
-keep class tech.done.user.ILoginWithDoneService$* { *; }
-keep interface tech.done.user.ILoginCallback { *; }
-keep class tech.done.user.ILoginCallback$* { *; }

# Jackson and jackson-module-kotlin.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
