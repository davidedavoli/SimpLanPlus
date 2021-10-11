push 0
mv $sp $fp //Load new $fp
li $a0 4

push $a0

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

//Start codegen of ast.node.types.IntTypeNode!=ast.node.types.IntTypeNode
mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id
push $a0 // push e1
li $a0 3
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0
not $a0 $a0 // $a0 = !$a0

bc $a0 LABELthen1
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 0

push $a0

li $a0 1

push $a0

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id

print $a0


subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 5

push $a0

li $a0 3

push $a0

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id

print $a0


subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:

mv $fp $a1 //put in $a1 (al) actual fp
lw $a0 -1($a1) //put in $a0 value of Id

print $a0


halt
