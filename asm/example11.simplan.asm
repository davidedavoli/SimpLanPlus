push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

new $a0// put new address in a0

push $a0

li $a0 56

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

new $a0// put new address in a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 b=exp

li $a0 1

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) // de referencing inner
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 b=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id b

lw $a0 0($a0)
lw $a0 0($a0)
print $a0

halt
