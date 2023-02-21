# corewar_ddcf
Corewar implementation in JAVA with 3 collegues

/!\ Important note: Currently studying language theory after this project and we would surely do this differently.

/!\ There might be issues with the JS script engine used in Parser

/!\ The repport (in french) of this project isn't satisfying and would need to be partly rewritten.

#

Pour compiler il suffit d'exécuter "ant" dans le répértoire racine du projet. Si c'est la première compilation, il faut préalablement avoir exécuté "ant init".

Pour lancer les tests: "ant test"

Un validateur de programme est disponible. Il vérifie que tout les programmes du dossier "progs" sont corrects. Pour le lancer: "ant checker". Attention, un shell sh doit être disponible.
Il est aussi possible de vérifier seulement certains fichiers en éxécutant le script "checker.sh":
"scripts/checker.sh <programe 1> <programe2> ... <programe n>"

Pour générer la documentation: "ant doc"

Pour créer une archive JAR: "ant jar"

Pour lancer une partie avec un affichage dans le terminal:
"java -cp build corewar.Main <programe 1> <programe2> ... <programe n>"

Pour lancer une partie avec un affichage graphique:
"java -cp build interfaces.CorewarWindow <programe 1> <programe2> ... <programe n>"

Pour lancer le menu principal:
"java -cp build interfaces.MainMenu"

L'algorithme génétique prends plusieurs paramètres: Le nombre de génération, le mode de séléction et les programmes initiaux. Les modes de séléction disponibles sont l'élitisme ("e") et la roue ("w").
"scripts/algo_gen.sh <nombre de générations> <mode de séléction> <programe 1> <programe2> ... <programe n>"
Il est également possible de lancer l'algorithme génétique sans lui donner de programmes initiaux. Il faut cependant spécifier le nombre de programme qui doivent être séléctionnés à chaque génération.
"scripts/algo_gen.sh <nombre de programme à séléctionner> <nombre de générations> <mode de séléction>"

L'algorithme génétique écrit le meilleur programme de la dernière génération en "progs/best.red". Il génére aussi un fichier CSV détaillant l'évolution de certaines metriques en fonction du numéro de la génération. Pour générer les graphiques correspondants à ces données on peut exécuter le script "scripts/plotmaker.py".
