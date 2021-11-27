push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

li $a0 11

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

free $a0 //free address in $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI
