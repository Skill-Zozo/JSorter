rm -rf dummyfiles/
cd ..
./mybash.sh
cd JSorter
javac JSorter.java
java JSorter dummyfiles
rm -rf *.class
rm -rf *~
