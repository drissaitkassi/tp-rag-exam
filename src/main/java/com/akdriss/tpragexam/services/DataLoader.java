package com.akdriss.tpragexam.services;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;



import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class DataLoader {

    @Value("classpath:/pdfs/assurance.pdf")
    private Resource pdfFile;
    @Value("vs1.json")
    private String vectoreStoreName;

    private JdbcClient jdbcClient;
    private VectorStore vectorStore;
    public DataLoader(JdbcClient jdbcClient,VectorStore vectorStore) {
        this.jdbcClient = jdbcClient;
        this.vectorStore=vectorStore;
    }

    //@Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore simpleVectorStore =new SimpleVectorStore(embeddingModel);

        String path=Path.of("src","main","resources","vectorstore").toFile().getAbsolutePath()+"/"+vectoreStoreName;
        File fileStore= new File(path);
        if (fileStore.exists()){
            log.info("vector store  already exists {}",path);
            simpleVectorStore.load(fileStore);

        }else {
            PagePdfDocumentReader pagePdfDocumentReader =new PagePdfDocumentReader(pdfFile);
            List<Document> documentList =pagePdfDocumentReader.get();
            TextSplitter textSplitter=new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documentList);
            simpleVectorStore.accept(chunks);
            simpleVectorStore.save(fileStore);
        }
        return simpleVectorStore;
    }

    @PostConstruct
    public void initStore(){
       Integer rowCount= jdbcClient.sql("select count(*) from vector_store").query(Integer.class).single();
    if (rowCount == 0){
        PagePdfDocumentReader pagePdfDocumentReader =new PagePdfDocumentReader(pdfFile);
        List<Document> documentList =pagePdfDocumentReader.get();
        TextSplitter textSplitter=new TokenTextSplitter();
        List<Document> chunks = textSplitter.split(documentList);

        vectorStore.accept(chunks);
    }

    }
}
