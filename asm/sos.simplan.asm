push 0
mv $sp $fp //Load new $fp
li $a0 10

push $a0

new $a0// put new address in a0

push $a0

li $a0 11

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 y=exp

push $fp
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next instruction

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
print $a0

halt
//Creating function:
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
li $a0 0

bc $a0 LABELthen1
push 0
push $fp //loading new block
mv $sp $fp //Load new $fp
li $a0 10

print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
push 0
push $fp //loading new block
mv $sp $fp //Load new $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
lw $a0 -2($al) //put in $a0 value of Id y

free $a0 //free address in $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:

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
// END OF function

//Ending function.
