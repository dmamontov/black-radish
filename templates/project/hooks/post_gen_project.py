import os

package = "{{cookiecutter.package|replace('.', '/')}}"

main = "src/main/kotlin"
test = "src/test/kotlin"

mainWithPackage = main + "/" + package + "/specs"
testWithPackage = test + "/" + package

os.makedirs(mainWithPackage, exist_ok=True)
os.makedirs(testWithPackage, exist_ok=True)

os.rename(main + "/MainSpec.kt", mainWithPackage + "/MainSpec.kt")
os.rename(test + "/MainTest.kt", testWithPackage + "/MainTest.kt")

os.system('gradle wrapper')
os.system('./gradlew download')
os.system('./gradlew clean build')
