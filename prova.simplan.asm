push 0
addi $fp $sp 0
li $a0 5

push $a0

push 0
push $fp //loadind new block
addi $fp $sp 0 //Load new $fp
li $a0 4

push $a0

addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

//RITORNATO DA CGEN EXP
addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a1 0($a1) //go up to chain
addi $a1 $a1 -1 //put in $a1 address of Id

sw $a0 0($a1) // 0($a1) = $a0 id=exp 


lw $fp 1($sp) //Load old $fp pushed 
addi $sp $sp 2 //Restore stackpointer as before block creation 

addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

push 0
push $fp //loadind new block
addi $fp $sp 0 //Load new $fp
li $a0 1

push $a0

push 0
push $fp //loadind new block
addi $fp $sp 0 //Load new $fp
addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a1 0($a1) //go up to chain
lw $a0 -1($a1) //put in $a0 value of Id

push $a0

addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

li $a0 2

//RITORNATO DA CGEN EXP
addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a1 0($a1) //go up to chain
addi $a1 $a1 -1 //put in $a1 address of Id

sw $a0 0($a1) // 0($a1) = $a0 id=exp 

addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a1 0($a1) //go up to chain
lw $a0 -1($a1) //put in $a0 value of Id

print $a0


lw $fp 1($sp) //Load old $fp pushed 
addi $sp $sp 2 //Restore stackpointer as before block creation 

addi $a1 $fp 0 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0


lw $fp 1($sp) //Load old $fp pushed 
addi $sp $sp 2 //Restore stackpointer as before block creation 


halt
