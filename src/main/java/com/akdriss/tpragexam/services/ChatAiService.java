package com.akdriss.tpragexam.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
@AnonymousAllowed
@BrowserCallable
public class ChatAiService {

    private ChatClient chatClient;
    private VectorStore vectorStore;
    @Value("classpath:/prompts/prompts.st")
    private Resource promptTemplate;

    public ChatAiService(ChatClient.Builder builder ,VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore=vectorStore;
    }

    public String ragChat(String question){

        List<Document> documents=vectorStore.similaritySearch(question);
         List<String> context=documents.stream().map(Document::getContent).toList();
        PromptTemplate template= new PromptTemplate(promptTemplate);
        Prompt prompt=template.create(Map.of("context",context,"question",question));
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
