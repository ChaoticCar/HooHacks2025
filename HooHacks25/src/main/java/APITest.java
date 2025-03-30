import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class APITest {

    public static void run () {

        System.out.println("We got here!");

        StreamingChatLanguageModel gemini = GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(APIKey.APIKey)
                .modelName("gemini-1.5-flash")
                .build();

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        gemini.chat("Tell me a joke about Java", new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        futureResponse.join();

        try {
            ChatResponse response = futureResponse.get();
            System.out.println(response.aiMessage().text());
        } catch (InterruptedException exception) {

            System.out.println("Thread was interrupted!");
        } catch (ExecutionException exception) {
            System.out.println("Chat message was aborted before it completed");
        }
    }
}
