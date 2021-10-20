push 0
mv $sp $fp //Load new $fp
li $a0 1

push $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
