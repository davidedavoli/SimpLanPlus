rm -rf bin
javac -classpath "antlr-4.9.2-complete.jar" -source 10 -sourcepath src src/SimplanPlus/Compiler.java -d bin
jar xf antlr-4.9.2-complete.jar 
mv org bin/
rm -rf javax
rm -rf META-INF
cd bin
find -name "*.class" > classes.txt
jar cvfm SimpLanPlus.jar ../manifest.txt @classes.txt
mv SimpLanPlus.jar ../
