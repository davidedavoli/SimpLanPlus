push 0
mv $sp $fp //Load new $fp
li $a0 5

push $a0

li $a0 0

push $a0

subi $sp $sp 1 // non assegnato nulla

li $a0 1

push $a0

li $a0 6

push $a0

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 c=exp

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 c=exp

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
subi $sp $sp 1 // non assegnato nulla

mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id x

print $a0

mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id y

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI
