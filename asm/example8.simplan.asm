push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

new $a0// put new address in a0

push $a0

li $a0 8

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

li $a0 5

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id

free $a0 //free address in $a0

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

li $a0 9

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

halt
