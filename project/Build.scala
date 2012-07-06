import sbt._
import sbt.Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import AndroidKeys._
import sbtprotobuf.ProtobufPlugin

object Resolvers {
  val smsserResolvers = Seq(
    "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
    "Twitter Releases" at "http://maven.twttr.com/",
    "Tinkerprop" at "http://tinkerpop.com/maven2", // for neo4j admin
    "Neo4j Public Releases" at "http://m2.neo4j.org/content/repositories/releases", // for neo4j admin
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

  val akkaActor               = "com.typesafe.akka"        % "akka-actor"                % Versions.akka withSources()
  val akkaSlf4j               = "com.typesafe.akka"        % "akka-slf4j"                % Versions.akka withSources()
  val akkaTestKit             = "com.typesafe.akka"        % "akka-testkit"              % Versions.akka % "test"
  val akkaDependencies        = Seq(akkaActor, akkaSlf4j, akkaTestKit)

  val aws                  = "com.amazonaws"               % "aws-java-sdk"              % Versions.aws

  val scalaTest               = "org.scalatest"           %% "scalatest"                 % "1.7.RC1" % "test"
  val mockito                 = "org.mockito"              % "mockito-core"              % "1.8.5" % "test"

  // mongodb
  val rogue                   = "com.foursquare"            %% "rogue"                     % "1.1.8" intransitive()
  val liftMongoRecord         = "net.liftweb"               %% "lift-mongodb-record"       % "2.4"

  // json
  val liftJson                = "net.liftweb"             %% "lift-json"                 % "2.0"

  val slf4s                   = "com.weiglewilczek.slf4s" %% "slf4s"                     % "1.0.7"
  val logbackClassic          = "ch.qos.logback"           % "logback-classic"           % "1.0.0"
  val guava                   = "com.google.guava"         % "guava"                     % Versions.guava
  val smack                   = "org.igniterealtime.smack" % "smack"                     % Versions.smack
  val smackX                  = "org.igniterealtime.smack" % "smackx"                    % Versions.smack
  val protoBufJava            = "com.google.protobuf"      % "protobuf-java"             % Versions.protobuf
  val jerseyClient            = "com.sun.jersey"           % "jersey-client"             % Versions.jersey
  val jerseyServer            = "com.sun.jersey"           % "jersey-server"             % Versions.jersey
  val jerseyGrizzly           = "com.sun.jersey"           % "jersey-grizzly"            % "1.9.1"
  val grizzlyServletWebserver = "com.sun.grizzly"          % "grizzly-servlet-webserver" % "1.9.18-i"

  val neo4j                   = "org.neo4j"                % "neo4j"                     % Versions.neo4j
  val neo4jShell              = "org.neo4j"                % "neo4j-shell"               % Versions.neo4j
  val neo4jServerApi          = "org.neo4j"                % "sever-api"                 % Versions.neo4j from "http://search.maven.org/remotecontent?filepath=org/neo4j/server-api/1.8.M01/server-api-"+Versions.neo4j+".jar"
  val neo4jServer             = "org.neo4j.app"            % "neo4j-server"              % Versions.neo4j // from "http://up.project13.pl/files/neo4j-server-"+Versions.neo4j+".jar"
  val neo4jStaticServer       = "org.neo4j.app"            % "neo4j-server-static-web"   % Versions.neo4j from "http://up.project13.pl/files/neo4j-server-static-web-"+Versions.neo4j+".jar" // from "http://m2.neo4j.org/content/repositories/releases/org/neo4j/app/neo4j-server/"+Versions.neo4j+"/neo4j-server-"+Versions.neo4j+"-static-web.jar"
  val jerseyCore              = "com.sun.jersey"           % "jersey-core"               % "1.9"
  val jacksonCore             = "org.codehaus.jackson"     % "jackson-core-asl"          % Versions.jackson
  val jacksonMapper           = "org.codehaus.jackson"     % "jackson-mapper-asl"        % Versions.jackson
  val groovy                  = "org.codehaus.groovy"      % "groovy"                    % "1.8.5"
  val gremlinGroovy           = "com.tinkerpop.gremlin"    % "gremlin-groovy"            % "1.5"
  val blueprintsNeo4j         = "com.tinkerpop.blueprints" % "blueprints-neo4j-graph"    % "1.2"
  val neo4jScala              = "org.neo4j"                % "neo4j-scala"               % "0.2.0-SNAPSHOT" from "http://up.project13.pl/files/neo4j-scala-0.2.0-SNAPSHOT.jar" // gh: https://github.com/FaKod/neo4j-scala
  val commonsConfiguration    = "commons-configuration"    % "commons-configuration"     % "1.8"
  val rrd4j                   = "org.rrd4j"                % "rrd4j"                     % "2.0.7"
  val jetty                   = "org.mortbay.jetty"        % "jetty"     % "6.1.25"
  val neo4jDependencies = Seq(
    neo4j, neo4jServer, neo4jStaticServer, jerseyCore, neo4jScala, neo4jShell, neo4jServerApi,
    commonsConfiguration, jetty, rrd4j, jacksonCore, jacksonMapper, groovy, gremlinGroovy, blueprintsNeo4j
  )

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
    slf4s,
    logbackClassic,
    guava,
    scalaTest,
    mockito
  )
  
  val xmppDependencies = Seq(
    smack,
    smackX
  )

  val protoDependencies = Seq(
    protoBufJava,
    jerseyClient
  )

  val protoServerDependencies = protoDependencies ++ akkaDependencies ++ Seq(
    jerseyGrizzly,
    grizzlyServletWebserver,
    jerseyServer
  ) ++ Seq (
    rogue,
    liftMongoRecord
  )

  val androidDependencies = Seq(
    jerseyCore,
    zxingAndroid
  )

  val buildSettings = Defaults.defaultSettings ++
    Seq(
      organization := "pl.project13",
      name         := "smsser",
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
  ) aggregate(protos, android, api, xmpp)

  lazy val protos = Project (
    "protos",
    file("protos"),
    settings = buildSettings ++
      ProtobufPlugin.protobufSettings ++
      assemblySettings ++ Seq(
        mainClass in assembly := Some("pl.project13.smsser.api.ApiServer"),
        test in assembly := {},
        mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
            case "application.conf" => MergeStrategy.concat
            case x => MergeStrategy.first
          }
        }) ++
      Seq (
        name := "protos",
        libraryDependencies ++= generalDependencies ++ protoDependencies
      ) ++
      Seq (
        javaSource in ProtobufPlugin.protobufConfig <<= (sourceDirectory in Compile)(_ / "java")
      )
  )

  lazy val android = Project (
    "android",
    file("android"),
    settings = fullAndroidSettings ++
      ProtobufPlugin.protobufSettings ++
      Seq (
        name := "android",
        libraryDependencies ++= generalDependencies ++ protoDependencies ++ androidDependencies ++ Seq(
          softwaremillCommon % "test"
        )
      )
  ) dependsOn(protos)

  lazy val api = Project (
    "api",
    file("api"),
    settings = buildSettings ++
      assemblySettings ++ Seq(
        mainClass in assembly := Some("pl.project13.smsser.api.ApiServer"),
        test in assembly := {},
        mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
            case "application.conf" => MergeStrategy.concat
            case x => MergeStrategy.first
          }
        }) ++
      Seq(
        name := "api",
        libraryDependencies ++= generalDependencies ++ protoServerDependencies ++ neo4jDependencies ++
          akkaDependencies ++ Seq(
            aws, liftJson
          )
      )
  ) dependsOn(protos, xmpp)

  lazy val xmpp = Project (
    "push-xmpp",
    file("push-xmpp"),
    settings = buildSettings ++
      ProtobufPlugin.protobufSettings ++
      assemblySettings ++ Seq(
        mainClass in assembly := Some("pl.project13.smsser.xmpp.runner.XmppPusher"),
        test in assembly := {},
        mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
            case x => MergeStrategy.first
          }
        }) ++
      Seq (
        name := "push-xmpp",
        libraryDependencies ++= generalDependencies ++ protoServerDependencies ++ xmppDependencies ++ Seq(
          aws
        )
      )
  ) dependsOn(protos)

}
