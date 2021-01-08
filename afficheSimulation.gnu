set terminal x11
set xrange [0:100]
set yrange [0:10000]
set xlabel "x"
set ylabel "temps"
set key Left reverse

plot "serveur0.dat" using 1:2 t "traitement S1 " with l lt 2,\
 "serveur1.dat" using 1:2 t "Traitement S2 " with l lt 2


