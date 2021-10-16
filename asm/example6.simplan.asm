push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
lw $al 0($al)
sw $a0 0($al) // 0($a1) = $a0 id=exp 

li $a0 5

//RITORNATO DA CGEN EXP
lw $al 0($al)
sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

lw $a0 0($a0)
lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

free $a0 //free address in $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

halt
