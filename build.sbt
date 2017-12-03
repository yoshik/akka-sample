name := "twitter"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "com.atilika.kuromoji" %  "kuromoji-ipadic"  % "0.9.0",
    "com.typesafe.akka"    %% "akka-actor"       % "2.4.17",
    "com.typesafe.akka"    %% "akka-http"        % "10.0.6",
    "com.typesafe.play"    %% "play-json"        % "2.5.6",
    "com.twitter"          %  "hbc-core"         % "2.2.0",
    "org.apache.commons"   %  "commons-lang3"    % "3.4",
    "commons-io"           %  "commons-io"       % "2.4"
)

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
SbtScalariform.scalariformSettings
ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignArguments, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
