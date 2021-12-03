push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

subi $sp $sp 1 // non assegnato nulla

subi $sp $sp 1 // non assegnato nulla

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 x=exp

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 y=exp

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 z=exp

li $a0 2

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 z=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id z

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

free $a0 //free address in $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id z

lw $a0 0($a0)
print $a0

halt
