#!/bin/bash

# requires librsvg

sizes=(16 24 32 48 64 72 96 128 144 152 167 168 180 192 256 512 1024)

for size in ${sizes[@]}
do
    filename="icon_$size.png"
    rsvg-convert -h $size mahjong-points.svg > $filename
done

# requires imagemagick

convert icon_256.png -define icon:auto-resize=256,64,48,32,16 favicon.ico
