push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

new $a0// put new address in a0

push $a0

push $fp
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function1// jump to start of function and put in $ra next instruction

halt
//Creating function:
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id a

lw $a0 0($a0)
print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

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

//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
li $a0 1

mv $fp $al //put in $a1 (al) actual fp
addi $al $al 1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 a=exp

mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id a

lw $a0 0($a0)
print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in a void function without return 
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
// END OF h

//Ending function.
