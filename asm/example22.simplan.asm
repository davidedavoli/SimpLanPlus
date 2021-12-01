push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function2// jump to start of function and put in $ra next instruction

halt
//Creating function:
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
li $a0 1

subi $sp $fp 1 //Restore stack pointer as before block creation in return 
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

//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
li $a0 2

subi $sp $fp 1 //Restore stack pointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1


endFunction1:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF g

//BEGIN FUNCTION Function2
Function2:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.IdExpNode==ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id a
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen1
push 0
push $fp //loading new block
mv $sp $fp //Load new $fp
push $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function0// jump to start of function and put in $ra next instruction

print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
push 0
push $fp //loading new block
mv $sp $fp //Load new $fp
push $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function1// jump to start of function and put in $ra next instruction

print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:

subi $sp $fp 1 //Restore stack pointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction2

endFunction2:
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
