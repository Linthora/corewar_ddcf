#!/bin/sh

# Eclipse workspace, to be adapted to your own configuration
#workspace_dir="corewar_ddcf"
#output_dir="renduTest"
#cd ..
#
#[ -d ${output_dir} ] || mkdir ${output_dir}
#rm -rf ${output_dir}/*
#
## Copies source files
#basedir=$(pwd)
#cd ${workspace_dir}
#echo AHAHAHAHAHAHAHAHTEST@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
#echo $basedir
# 
#find . -name "*.java" -exec cp --parents {} ${basedir}/${output_dir}/ \;
#cd - > /dev/null
#echo ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
#
## Copies exempleProg
#mkdir ${output_dir}/progs
#cp -f ${workspace_dir}/progs/*.red ${output_dir}/progs/
#
## Copies readme (here we'll use task.txt, cause no readme has been created yet)
#cp ${workspace_dir}/task.txt ${output_dir}/
#
##Copie for ant uses
#cp ${workspace_dir}/build.xml ${output_dir}/
#
## Makes all files in dist readonly so as to avoid editing them
#find ${output_dir} -type f -exec chmod u-w {} \;

OUTPUT=corewar.tar.gz
if [ -f $OUTPUT ]; then
	echo "$OUTPUT already exists" 1>&2
	exit 1
fi
git archive HEAD -o $OUTPUT && echo "Archive written to $OUTPUT"
