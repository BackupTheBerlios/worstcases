#!/bin/sh
# Entfernt überflüssige Kommentare von TogetherJ, Jindent und Forte.
for i in *.java
do cp $i $i-tmp
done

for i in *.java-tmp
do sed -e '/Jindent/d' -e '/Generated by Together/d' $i > ${i%java-tmp}java
done

rm *.java-tmp
