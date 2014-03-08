#!/bin/bash
for problem in 2
do
	for moves in {11..14}
	do
		echo "Working on problem ${problem} with ${moves} moves."
		java -jar experiments_7.jar ${HOSTNAME} ${problem} ${moves} ${moves}
	done
done

