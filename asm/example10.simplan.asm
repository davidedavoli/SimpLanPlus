push 0
mv $sp $fp //Load new $fp
push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function1// jump to start of function and put in $ra next istruction

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
li $a0 2

print $a0

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
// END OF g

//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
li $a0 0

print $a0

push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function2// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1
//CREO FUNZIONI
//BEGIN FUNCTION Function2
Function2:
mv $sp $fp
push $ra
li $a0 1

print $a0

push $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function1// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction2

endFunction2:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF d

//FINE FUNZIONI

endFunction1:
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
