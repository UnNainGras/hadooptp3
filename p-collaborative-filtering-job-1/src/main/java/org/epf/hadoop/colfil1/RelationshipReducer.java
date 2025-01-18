package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RelationshipReducer extends Reducer<Text, Text, Text, Text> {
    private Text outputValue = new Text();
    private Text cleanedKey = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Nettoyer la clé pour éliminer le timestamp si présent
        String originalKey = key.toString();
        String[] keyParts = originalKey.split(","); // Diviser la clé par la virgule
        String cleanedKeyStr = keyParts[0]; // Conserver uniquement la première partie de la clé
        cleanedKey.set(cleanedKeyStr + " : ");

        // Utiliser un Set pour éliminer les doublons dans les valeurs
        Set<String> uniqueFriends = new HashSet<>();
        for (Text value : values) {
            // Extraire uniquement l'ID si le format est correct (id,timestamp)
            String[] valueParts = value.toString().split(",");
            if (valueParts.length == 2) {
                uniqueFriends.add(valueParts[0].trim());
            }
        }

        // Construire la liste des amis séparés par des virgules
        String friendsList = String.join(",", uniqueFriends);
        outputValue.set(friendsList);

        // Écrire le résultat final avec la clé nettoyée
        context.write(cleanedKey, outputValue);
    }
}
