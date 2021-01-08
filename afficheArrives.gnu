set terminal x11
set xrange [0:20000]
set yrange [0:100000]
set xlabel "x"
set ylabel "temps"
set key Left reverse

plot "arrivees.dat" using 1:2 t "Requetes" with linesp lt 1 pt 1