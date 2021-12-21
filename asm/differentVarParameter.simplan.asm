push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

li $a0 1

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

push $fp
li $a0 2

push $a0
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next instruction

halt
//Creating function:
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.LhsExpNode+ast.node.exp.IdExpNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id a

lw $a0 0($a0)push $a0 // push e1
mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id b
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

print $a0

subi $sp $fp 1 //Restore stack pointer as before block creation in a void function without return 
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
// END OF sum

//Ending function.
