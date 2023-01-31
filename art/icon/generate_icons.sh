#!/bin/bash

# requires librsvg

sizes=(16 24 32 48 64 72 80 96 128 144 152 167 168 180 192 256 512 1024)

for size in ${sizes[@]}
do
    filename="icon_$size.png"
    rsvg-convert -h $size mahjong-points.svg > $filename
done

# requires imagemagick

convert icon_256.png -define icon:auto-resize=256,64,48,32,16 favicon.ico

# copy icons to resource folder

sizesToCopy=(16 32 80 152 167 180)
for size in ${sizesToCopy[@]}
do
    filename="icon_$size.png"
    cp $filename ../../src/main/resources/icons/
done
cp icon_256.png ../../src/main/resources/apple-touch-icon.png
