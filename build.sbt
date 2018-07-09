name := "ElasticseachIndexOps"

version := "1.0"

scalaVersion := "2.11.11"

// https://mvnrepository.com/artifact/net.liftweb/lift-json
libraryDependencies += "net.liftweb" %% "lift-json" % "3.0.1"

assemblyJarName in assembly := "something.jar"
test in assembly := {}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.fasterxml.jackson.core.**"       -> "shadedjackson.core.@1").inAll,
  ShadeRule.rename("com.fasterxml.jackson.annotation.**" -> "shadedjackson.annotation.@1").inAll,
  ShadeRule.rename("com.fasterxml.jackson.databind.**"   -> "shadedjackson.databind.@1").inAll
)
