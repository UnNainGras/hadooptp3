package org.epf.hadoop.colfil3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RecommendationMapper extends Mapper<Object, Text, Text, Text> {

    private Text userKey = new Text();
    private Text relationValue = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Remplacer les virgules par des espaces pour un traitement uniforme
        String cleanedLine = value.toString().replace(",", " ");
        String[] fields = cleanedLine.split("\\s+");

        if (fields.length == 3) {
            String userA = fields[0];
            String userB = fields[1];
            String commonRelations = fields[2];

            try {
                Integer.parseInt(commonRelations); // Validation du nombre
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + value.toString());
                return; // Ignore les lignes mal formatées
            }

            // Émettre les clés/valeurs pour les utilisateurs A et B
            userKey.set(userA);
            relationValue.set(userB + "," + commonRelations);
            context.write(userKey, relationValue);

            userKey.set(userB);
            relationValue.set(userA + "," + commonRelations);
            context.write(userKey, relationValue);
        } else {
            System.err.println("Skipping invalid input: " + value.toString());
        }
    }
}
