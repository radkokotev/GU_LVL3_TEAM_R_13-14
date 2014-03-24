#!/bin/bash
for problem in 4
do
	for moves in {6..9}
	do
		echo "Working on problem ${problem} with ${moves} moves."
		java -jar experiments_runner.jar ${HOSTNAME} ${problem} ${moves} ${moves}
	done
done

