push 0
mv $sp $fp //Load new $fp
li $a0 1

push $a0

li $a0 4

push $a0

li $a0 10

push $a0

push $fp
li $a0 1

push $a0
li $a0 1

push $a0
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
li $a0 1000

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id

print $a0

push $fp
li $a0 3

push $a0
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function1// jump to start of function and put in $ra next istruction

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al 1 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

//Start codegen of ast.node.types.IntTypeNode+ast.node.types.IntTypeNode
//Start codegen of ast.node.types.IntTypeNode+ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
mv $fp $al //put in $a1 (al) actual fp
lw $a0 2($al) //put in $a0 value of Id
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0
push $a0 // push e1
mv $fp $al //put in $a1 (al) actual fp
lw $a0 3($al) //put in $a0 value of Id
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

//CREO FUNZIONI
//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
li $a0 1

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 2($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1


endFunction1:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
jr $ra
// END OF dentro

//FINE FUNZIONI

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 3// pop parameters3
pop
lw $fp 0($sp)
jr $ra
// END OF easy

//FINE FUNZIONI
