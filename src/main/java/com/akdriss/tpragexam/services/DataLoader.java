package com.akdriss.tpragexam.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
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
    @Bean
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
}
