package org.epf.hadoop.colfil3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class RecommendationReducer extends Reducer<Text, Text, Text, Text> {

    private Text recommendations = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        PriorityQueue<Map.Entry<String, Integer>> topRecommendations = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue)
        );

        Map<String, Integer> recommendationMap = new HashMap<>();

        for (Text value : values) {
            System.out.println("Reducer value for key " + key.toString() + ": " + value.toString());
            String[] fields = value.toString().split(",");
            if (fields.length == 2) {
                String user = fields[0];
                try {
                    int commonRelations = Integer.parseInt(fields[1]);
                    recommendationMap.put(user, recommendationMap.getOrDefault(user, 0) + commonRelations);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number in reducer value: " + value.toString());
                }
            }
        }

        for (Map.Entry<String, Integer> entry : recommendationMap.entrySet()) {
            topRecommendations.offer(entry);
            if (topRecommendations.size() > 5) {
                topRecommendations.poll();
            }
        }

        List<String> sortedRecommendations = new ArrayList<>();
        while (!topRecommendations.isEmpty()) {
            Map.Entry<String, Integer> entry = topRecommendations.poll();
            sortedRecommendations.add(entry.getKey() + ":" + entry.getValue());
        }
        Collections.reverse(sortedRecommendations);

        recommendations.set(String.join(", ", sortedRecommendations));
        context.write(key, recommendations);
    }
}
