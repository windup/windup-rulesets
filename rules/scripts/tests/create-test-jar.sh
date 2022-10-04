# execute $ ./create-test-jar.sh io/quarkus/jackson
# It'll create a test.jar file in this folder.

mkdir -p _tmp_/$1;
touch -d 20000101 _tmp_/$1/Sample.class;
find ./_tmp_/ -exec touch -d 20000101 {} \;
jar cvMf test.jar -C ./_tmp_/ .;
rm -r _tmp_;
