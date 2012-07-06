import sbt._
import sbt.Keys._
import AndroidKeys._

object Resolvers {
}

object Versions {
  val janbanery = "1.2"
  val jersey = "1.9"
  val protobuf = "2.4.1"
  val guava = "12.0"
  val jackson = "1.8.3"
}

object Dependencies {
  import Resolvers._
  import Versions._

  val janbanery    = "pl.project13.janbanery" % "janbanery"     % Versions.janbanery

  val guava        = "com.google.guava"  % "guava"         % Versions.guava
  val liftJson     = "net.liftweb"      %% "lift-json"     % "2.0"
  val jerseyClient = "com.sun.jersey"    % "jersey-client" % Versions.jersey
  val jerseyCore   = "com.sun.jersey"    % "jersey-core"   % "1.9"

  // testing
  val scalaTest    = "org.scalatest"    %% "scalatest"     % "1.7.RC1" % "test"
  val mockito      = "org.mockito"       % "mockito-core"  % "1.8.5" % "test"

}

object BuildSettings {
  import Resolvers._
  import Dependencies._

  val proguardSettings = Seq (
    proguardOption in Android := Seq (
      """
-dontoptimize
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes *Annotation*

-keep class pl.project13.kanbanery.**.*

-keep class javax.ws.rs.core.**.*
-keep class com.sun.ws.**.*

-keepclasseswithmembernames class * {
    native <methods>;
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void get*(...);
    public void set*(...);
    public void writeTo(...);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Guava primitives lexicographicalComparator() references sun.misc.Unsafe dynamically
# which is obviously not provided in the Android Runtime
-dontwarn sun.misc.Unsafe

# Slf4j api is in libs for the server side stuff, not used in the app
-dontwarn org.slf4j.*

# Other dynamically referenced methods in Guava
-keepclassmembers class com.google.guava.* {
    void finalizeReferent();
    void startFinalizer(java.lang.Class,java.lang.Object);
}

# newBuilder() is referenced dynamically in generated ProtoBuf code
-keepclassmembers class * {
    ** newBuilder();
    void writeTo(...);
}
      """
    ).mkString("\n")
//    useProguard in Android := false
//    proguardOptimizations in Android := Seq(
//      "-optimizationpasses 8",
//      "-dontoptimize",
//      "-dontpreverify"
//      "-allowaccessmodification",
//      "-optimizations !code/simplification/arithmetic"
//    )
  )

//  val apiProguardSettings = ProguardPlugin.proguardSettings ++ Seq(
//    proguardOptions ++= Seq("-include api-proguard.cfg")
//  )


  val generalDependencies = Seq(
    guava,
    scalaTest,
    mockito
  )
  
  val androidDependencies = Seq(
    jerseyCore,
    janbanery
  )

  val buildSettings = Defaults.defaultSettings ++
    Seq(
      organization := "pl.project13",
      name         := "kanbanery-tv",
      version      := "0.2",
      versionCode  := 1,
      scalaVersion := "2.9.1",
      resolvers    ++= Resolver.withDefaultResolvers(kanbaneryResolvers, mavenCentral = true, scalaTools = false),
      libraryDependencies ++= generalDependencies
    )

  lazy val fullAndroidSettings = buildSettings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++ proguardSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me",
      platformName in Android := "android-10"
    )
}

object AndroidBuild extends Build {
  import Dependencies._
  import BuildSettings._

  compileOrder := CompileOrder.JavaThenScala

  lazy val root = Project (
    "kanbanery-tv",
    file("."),
    settings = fullAndroidSettings ++
      Seq (
        name := "kanbanery-tv",
        libraryDependencies ++= generalDependencies ++ androidDependencies ++ Seq()
      )
  )

}
