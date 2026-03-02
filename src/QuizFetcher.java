import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Fetches quiz data from the API
 */
public class QuizFetcher {
    /**
     * Fetches questions from API
     * @param amount of questions
     * @param categoryId of question subject
     * @param difficulty of questions
     * @return list of questions
     * @throws Exception if issue with API
     */
    public List<Question> fetchQuestions(int amount, int categoryId, String difficulty, String type) throws Exception {
        HttpResponse<String> response = getResponse(
                String.format("https://opentdb.com/api.php?amount=%d&category=%d&difficulty=%s&type=%s",
                        amount, categoryId, difficulty.toLowerCase(), type.toLowerCase()));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode resultsNode = getNodes(mapper, response, "results");

        if (!resultsNode.isEmpty()) {
            return mapper.readValue(resultsNode.toString(), new TypeReference<>(){});
        }
        return new ArrayList<>();
    }

    /**
     * Gets all quiz categories
     * @return hashmap of names and their ID's
     * @throws Exception if issue with API
     */
    public HashMap<String, Integer> getAllCategories() throws Exception {
        HttpResponse<String> response = getResponse("https://opentdb.com/api_category.php");

        JsonNode triviaCategories = getNodes(new ObjectMapper(), response, "trivia_categories");

        HashMap<String, Integer> categories = new HashMap<>();
        for (JsonNode node : triviaCategories) {
            categories.put(node.get("name").asText(), node.get("id").asInt());
        }
        return categories;
    }

    /**
     * Gets difficulty options
     * @return difficulty options
     */
    public List<String> getDifficultyOptions() {
        return List.of("Easy", "Medium", "Hard");
    }

    /**
     * Get question types
     * @return question types
     */
    public HashMap<String, String> getTypeOptions() {
        HashMap<String, String> types = new HashMap<>();
        types.put("Multiple Choice", "multiple");
        types.put("True / False", "boolean");
        return types;
    }

    /**
     * Get response from API for data
     * @param url of API
     * @return API response
     * @throws Exception if issue with API
     */
    private HttpResponse<String>  getResponse(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Get nodes from API response
     * @param mapper to map nodes
     * @param response to get data from
     * @param type of data
     * @return data of type
     * @throws Exception if issue with API
     */
    private JsonNode getNodes(ObjectMapper mapper, HttpResponse<String> response, String type) throws Exception {
        JsonNode rootNode = mapper.readTree(response.body());
        return rootNode.path(type);
    }
}