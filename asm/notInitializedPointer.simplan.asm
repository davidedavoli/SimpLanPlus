push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // No value assigned

subi $sp $sp 1 // No value assigned

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

halt
