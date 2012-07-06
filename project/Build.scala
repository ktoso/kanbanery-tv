import sbt._
import sbt.Keys._
import AndroidKeys._

object Resolvers {
  val smsserResolvers = Seq(
    "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
    "Twitter Releases" at "http://maven.twttr.com/",
    "Tinkerprop" at "http://tinkerpop.com/maven2", // for neo4j admin
    "SoftwareMill Releases" at "http://tools.softwaremill.pl/nexus/content/repositories/releases",
    "SoftwareMill Snapshots" at "http://tools.softwaremill.pl/nexus/content/repositories/snapshots"
  )
}

object Versions {
  val smack = "3.2.1"
  val akka = "2.0"
  val jersey = "1.9"
  val protobuf = "2.4.1"
  val guava = "12.0"
  val neo4j = "1.8.M01"
  val jackson = "1.8.3"
  val aws = "1.3.8"
}

object Dependencies {
  import Resolvers._
  import Versions._

  val scalaTest               = "org.scalatest"           %% "scalatest"                 % "1.7.RC1" % "test"
  val mockito                 = "org.mockito"              % "mockito-core"              % "1.8.5" % "test"

  // json
  val liftJson                = "net.liftweb"             %% "lift-json"                 % "2.0"

  val guava                   = "com.google.guava"         % "guava"                     % Versions.guava
  val protoBufJava            = "com.google.protobuf"      % "protobuf-java"             % Versions.protobuf
  val jerseyClient            = "com.sun.jersey"           % "jersey-client"             % Versions.jersey

  val jerseyCore              = "com.sun.jersey"           % "jersey-core"               % "1.9"
  val jacksonCore             = "org.codehaus.jackson"     % "jackson-core-asl"          % Versions.jackson
  val jacksonMapper           = "org.codehaus.jackson"     % "jackson-mapper-asl"        % Versions.jackson
  val groovy                  = "org.codehaus.groovy"      % "groovy"                    % "1.8.5"
  val gremlinGroovy           = "com.tinkerpop.gremlin"    % "gremlin-groovy"            % "1.5"
  val blueprintsNeo4j         = "com.tinkerpop.blueprints" % "blueprints-neo4j-graph"    % "1.2"
  val neo4jScala              = "org.neo4j"                % "neo4j-scala"               % "0.2.0-SNAPSHOT" from "http://up.project13.pl/files/neo4j-scala-0.2.0-SNAPSHOT.jar" // gh: https://github.com/FaKod/neo4j-scala
  val commonsConfiguration    = "commons-configuration"    % "commons-configuration"     % "1.8"

  val zxingAndroid            = "com.google.zxing"         % "android-integration"       % "2.0"
  val softwaremillCommon      = "pl.softwaremill.common"   % "softwaremill-util"         % "65-SNAPSHOT"
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

-keep class pl.project13.smsser.**.*

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
    zxingAndroid
  )

  val buildSettings = Defaults.defaultSettings ++
    Seq(
      organization := "pl.project13",
      name         := "kanbanery-tv",
      version      := "0.2",
      versionCode  := 1,
      scalaVersion := "2.9.1",
      resolvers    ++= Resolver.withDefaultResolvers(smsserResolvers, mavenCentral = true, scalaTools = false),
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
    "smsser",
    file("."),
    settings = buildSettings ++
      Seq (
      )
  ) aggregate(android)

  lazy val android = Project (
    "android",
    file("android"),
    settings = fullAndroidSettings ++
      Seq (
        name := "android",
        libraryDependencies ++= generalDependencies ++ androidDependencies ++ Seq(
          softwaremillCommon % "test"
        )
      )
  )

}
