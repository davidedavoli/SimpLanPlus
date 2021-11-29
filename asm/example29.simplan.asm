push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 2

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
push $fp
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id a

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function1// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0
//CREO FUNZIONI
//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id b

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1

endFunction1:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF g

//FINE FUNZIONI

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI
