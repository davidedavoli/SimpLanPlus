push 0
mv $sp $fp //Load new $fp
li $a0 1

push $a0

subi $sp $sp 1 // non assegnato nulla

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

push $a0

halt
