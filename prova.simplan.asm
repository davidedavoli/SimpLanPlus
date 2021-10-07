push 0
subi $fp $sp 1
subi $sp $sp 1

li $a0 6

push $a0

lw $a1 0($fp) //put in $a1 (al) actual fp
lw $a0 -3($a1) //put in $a0 value of Id

lw $a1 0($fp) //put in $a1 (al) actual fp
addi $a1 $a1 -2 //put in $a1 address of Id

sw $a0 0($a1) // 0($a1) = $a0 id=exp 

lw $a1 0($fp) //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id

print $a0


