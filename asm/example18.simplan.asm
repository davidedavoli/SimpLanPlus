push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // No value assigned

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id a

push $a0

halt
