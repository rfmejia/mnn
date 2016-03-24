import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

object ScalariformSettings {

  val scalariformSettings = SbtScalariform.defaultScalariformSettings ++ Seq(
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(CompactControlReadability, true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
  )
}
