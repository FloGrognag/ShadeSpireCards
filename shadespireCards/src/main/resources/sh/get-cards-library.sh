#!/bin/bash
todir="/Users/<>/Downloads/shadespire/"
lang="FRE"

show_help () {
    echo "get-cards-library -i inputFile -f toDir"
}

download () {
    cd $todir
    
   # if $verbose; then echo "commande: for card in $(grep .png $inputFile | grep /uploads | cut -d \" \" -f2 | tr -d \"\'\"); do"; fi
    #if $verbose ; then echo "commande: "; fi    

    for card in $(grep .png $inputFile | grep /uploads | grep $lang | cut -d " " -f2 | tr -d "'"); do
        if [ $verbose ]; then echo "curl -O '$card'"; fi
        curl -O -J "${card}"
    done
}

while getopts "h?vi:f:" opt; do
    case "$opt" in
    h|\?)
        show_help
        exit 0
        ;;
    v)  verbose=1
        ;;
    i)  inputFile=$OPTARG
        ;;
    f)  todir=$OPTARG
        ;;
    l)  lang=$OPTARG
        ;;
    esac
done

if [ -z $inputFile ]; then
    echo "inputFile is not set"
    show_help
    exit 0
fi

if [ ! -f $inputFile ]; then
    echo "inputFile is not a file"
    exit 0
fi

if [ ! -d $todir ]; then
    mkdir $todir
fi

echo "Input file: $inputFile"
echo "Output dir: $todir"
echo "Langue: $lang"
echo "verbose is: $verbose"

download