push 0
mv $sp $fp //Load new $fp
li $a0 10

push $a0

push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 5

push $a0

li $a0 1

push $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

print $a0

push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 6

push $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -2 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
