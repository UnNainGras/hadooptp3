# Hadoop TP Final

Ce document décrit les étapes et commandes nécessaires pour importer les données dans HDFS et exécuter les trois JARs du projet Hadoop TP3.

## Étape 1 : Construction et accès au conteneur Docker

1. Construisez l'image Docker :
```
   cd deploy/
   docker buildx build -t hadoop-tp3-img .
   cd ../
```
2. Accédez au conteneur Docker :
```
   docker exec -it hadoop-tp3-cont su - epfuser
```
## Étape 2 : Importation des données dans HDFS

1. Créez le dossier HDFS pour stocker les données :
```
   hadoop fs -mkdir -p /data/relationships
```
2. Importez le fichier de données dans HDFS :
```
   hadoop fs -put /tmp/data/relationships/data.txt /data/relationships/
```
## Étape 3 : Exécution des Jobs MapReduce

### Job 1 : Génération des relations de premier niveau

1. Supprimez les résultats existants du Job 1 (si nécessaires) :
```
   hadoop fs -rm -R /data/output/job1
```
2. Exécutez le Job 1 :
```
   hadoop jar /tmp/jars/tpfinal-florian_cabaret_job1.jar /data/relationships/data.txt /data/output/job1
```
3. Vérifiez les résultats du Job 1 :
```
   hadoop fs -cat /data/output/job1/part-r-00000
```
### Job 2 : Calcul des relations communes

1. Supprimez les résultats existants du Job 2 (si nécessaires) :
```
   hadoop fs -rm -R /data/output/job2
```
2. Exécutez le Job 2 :
```
   hadoop jar /tmp/jars/tpfinal-florian_cabaret_job2.jar /data/output/job1 /data/output/job2
```
3. Vérifiez les résultats du Job 2 :
```
   hadoop fs -cat /data/output/job2/part-r-00000
```
### Job 3 : Recommandations de relations

1. Supprimez les résultats existants du Job 3 (si nécessaires) :
```
   hadoop fs -rm -R /data/output/job3
```
2. Exécutez le Job 3 :
```
   hadoop jar /tmp/jars/tpfinal-florian_cabaret_job3.jar /data/output/job2 /data/output/job3
```
3. Vérifiez les résultats du Job 3 :
```
   hadoop fs -cat /data/output/job3/part-r-00000
```
