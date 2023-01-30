#!/bin/bash

FILES_NAME=files_name.txt
DATASETS="./datasets"
TEMP="temp"

if [ -f "$FILES_NAME" ]; then
    rm -r $FILES_NAME
fi

if [ -d "$DATASETS" ]; then
    rm -r $DATASETS
fi

if [ -d "$TEMP" ]; then
    rm -r $TEMP
fi

mkdir $TEMP
mkdir $DATASETS
touch $FILES_NAME

j=0

# for folder files zip
for entry in "./datasets_zip"/*; do
    nome_file=$(basename $entry)

    # check file zip
    if [[ ${nome_file: -4} = ".zip" ]]; then
    
        nome=${nome_file%????}
        echo ""
        echo $nome_file
        
        # unzip file 
        yes | unzip $entry -d "./temp"
        
        # remove __MACOSX folder 
        if [ -d "./temp/__MACOSX/" ]; then
            rm -r "./temp/__MACOSX/"
        fi

        count=0
        # number of files unzipped 
        for i in "./temp"/*; do
            count=$((count+1))
        done

        if [[ $count -gt 1 ]]; then
            folder_name=$nome
            mkdir $folder_name
            mv $folder_name $DATASETS
        fi

        # for files unzipped
        for i in "./temp"/*; do
            j=$((j+1))

            n_file=$(basename $i)
            
            # check file is an folder
            if [ -d "./temp/$n_file" ]; then
                n_folder="./temp/$n_file"
            
                for d in $n_folder/*; do
                    n_file=$(basename $d)
                    mv $n_folder/$n_file "./temp"
                done
                rm -r $n_folder
            fi

            if [[ ${n_file: -5} = ".json" ]]; then
                ext=${n_file: -5}
                echo File .json

            elif [[ ${n_file: -4} = ".csv" ]]; then
                ext=${n_file: -4}
                echo File .csv

            elif [[ ${n_file: -6} = ".jsonl" ]]; then
                ext=${n_file: -6}
                echo File .jsonl

            elif [[ ${n_file: -5} = ".xlsx" ]]; then
                ext=${n_file: -5}
                echo File .xlsx

            elif [[ ${n_file: -4} = ".xls" ]]; then
                ext=${n_file: -4}
                echo File .xlsx

            else
                echo File error: $n_file
                exit 1
            fi

            if [[ $count -gt 1 ]]; then
                nome=${n_file%????}
                yes | mv "./temp/"$n_file "./temp/"$nome$ext
                mv "./temp/"$nome$ext "$DATASETS/$folder_name"
                echo $nome$ext >> $FILES_NAME
            else
                yes | mv "./temp/"$n_file "./temp/"$nome$ext
                mv "./temp/"$nome$ext $DATASETS
                echo $nome$ext >> $FILES_NAME
            fi

        done
    fi
done
echo "" >> $FILES_NAME
echo "Datasets totali: $j" >> $FILES_NAME
rm -r $TEMP