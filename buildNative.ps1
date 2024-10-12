# NOTE: graalvm is required
# NOTE: run this script in the Native Tools Command Prompt for VS 2022 which provided by Visual Studio

# prepare
$moduleInfo="src/main/java/module-info.java"
Remove-Item $moduleInfo 2>$null
Write-Output "module java {
	requires javafx.controls;
    requires javafx.graphics;
    exports cn.itscloudy.joybox;
}" > $moduleInfo

$fxlibs=$env:JAVAFX_LIB_PATH
$sp="src/main"
$out="mods/out"

# build
javac -d $out --module-path $fxlibs `
--module-source-path $sp $(Get-ChildItem -Path $sp -Filter *.java -Recurse -Name | ForEach-Object{"$sp/$_"})
Remove-Item $moduleInfo 2>$null

# build mods
$arc="mods/arc"
jar --create --file $arc/java.jar --module-version=1.0 -C $out/java .

# run with agentlib
$cfg="mods/cfg"
$modulePath="$fxlibs/javafx.base.jar;$fxlibs/javafx.controls.jar;$fxlibs/javafx.graphics.jar;$arc/java.jar"
java -agentlib:native-image-agent=config-output-dir=$cfg `
--module-path $modulePath `
-m java/cn.itscloudy.joybox.JoyBox

# build native
native-image --module-path $modulePath --module java/cn.itscloudy.joybox.JoyBox `
-H:+UnlockExperimentalVMOptions `
-H:ReflectionConfigurationFiles=$cfg/reflect-config.json `
-H:ResourceConfigurationFiles=$cfg/resource-config.json `
-H:SerializationConfigurationFiles=$cfg/serialization-config.json