#!/bin/bash

##TODO go to the rigth folder
##TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO BOOT/Start label
cd progs

for i in $(seq 14); do
    #echo $i
    awk 'BEGIN { counter=0 }
    {
        if(counter < 1) {
            if($0 == "Score:") { counter ++; }
            print ";", $0;
        }
        else if($0 == "" || $0 ~ /^[ \t]*$/)
            print "";
        else
	        print $0;
    }' progsFromKoth/p$i.red | sed -r 's/,([^ ])/, \1/g' > t$i.red
    # awk 'BEGIN{
    #     counter=0
    # } 
    # /^$/ {
    #     print $
    # }
    # {
    #     if (counter < 7) print counter; counter ++; print '\;', $0;
    #     else if (length($0) == 0) print $0;
    #     else print $1;
        

    # }' progsFromKoth/p$i.red
done
# > t$i.red
# awk 'BEGIN{tfidf=ARGV[--ARGC];while((getline<tfidf)>0)tab[$1]=$2}{print $1, tab[$1]}' $i.tfidf
# awk '{if ($1 == precedent) compteur ++; else {print precedent, compteur; compteur = 1; precedent = $1 }  }END{print precedent, compteur}'