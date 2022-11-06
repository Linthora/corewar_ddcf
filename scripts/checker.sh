#!/bin/sh

cd $(dirname $(dirname $(realpath $0))) && java -cp build corewar.Checker $@
