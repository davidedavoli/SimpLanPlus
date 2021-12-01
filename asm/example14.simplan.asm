push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // No value assigned

new $a0// put new address in a0

push $a0

push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next instruction

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id global

free $a0 //free address in $a0

li $a0 2

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

halt
//Creating function:
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
new $a0// put new address in a0

mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 global=exp

li $a0 0

bc $a0 LABELthen1
push 0
push $fp //loading new block
mv $sp $fp //Load new $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
lw $a0 -1($al) //put in $a0 value of Id global

lw $a0 0($a0)
lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stack pointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

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
lw $a0 -1($al) //put in $a0 value of Id global

lw $a0 0($a0)
lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stack pointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:


endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF newGlobal

//Ending function.
