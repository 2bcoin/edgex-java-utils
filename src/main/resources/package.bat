@echo off
:: 1. 切换到项目根目录（可选，确保路径正确）
cd /d %~dp0

:: 2. 执行 jpackage 命令
"C:\Users\rieca\.jdks\graalvm-jdk-17.0.12\bin\jpackage" ^
--type app-image ^
--verbose ^
--name "edgexTrade" ^
--app-version "1.0.2" ^
--input "E:\gitcode\edgexDemojMaven\target" ^
--main-jar "edgexDemojMaven-1.0-SNAPSHOT-jar-with-dependencies.jar" ^
--main-class com.riecardx.edgex.MainApp ^
--dest "./dist"
--icon "E:\gitcode\edgexDemojMaven\src\main\resources\edgex.ico"

:: 3. 暂停，防止窗口一闪而过看不到报错
echo 打包任务已完成，请检查 dist 目录。
pause