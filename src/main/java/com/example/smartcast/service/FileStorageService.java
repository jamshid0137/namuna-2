package com.example.smartcast.service;

import com.example.smartcast.dto.FileStorage;
import com.example.smartcast.enums.FileStorageStatus;
import com.example.smartcast.repository.FileStorageRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileStorageService {
    private final FileStorageRepository fileStorageRepository;

    private final Hashids hashids;

    public FileStorageService(FileStorageRepository fileStorageRepository){
        this.fileStorageRepository=fileStorageRepository;
        this.hashids=new Hashids(getClass().getName(),6);//hashlash shifrlashga o'xshash
    }
    @Value("${upload.folder}")
    private String uploadFolder;
    public void save(MultipartFile multipartFile){
        FileStorage fileStorage=new FileStorage();
        fileStorage.setName(multipartFile.getOriginalFilename());
        fileStorage.setExtension(getExt(multipartFile.getOriginalFilename()));
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorageRepository.save(fileStorage);

        //faylni yil,oy kun nomi bn fayl ochib o'shani ichiga saqlaymiz tartib uchun
        Date now=new Date();
        File uploadFolder=new File(String.format("%s/upload_files/%d/%d/%d",this.uploadFolder,
                1900+now.getYear(),
                1+now.getMonth(),now.getDate()));
        //agar uploadFolder file mavjud bo'lmasa va file yaratilmagan bo'lsa
        if(!uploadFolder.exists() && uploadFolder.mkdirs()){
            System.out.println("Aytilgan papka yaratildi !");
        }
        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        fileStorage.setUploadPath(String.format("upload_files/%d/%d/%d/%s.%s",1900+now.getYear(),
                1+now.getMonth(),now.getDate()
                ,fileStorage.getHashId()
                ,fileStorage.getExtension()
        ));
        fileStorageRepository.save(fileStorage);
        uploadFolder=uploadFolder.getAbsoluteFile();
        File file=new File(uploadFolder,String.format("%s.%s",fileStorage.getHashId(),fileStorage.getExtension()));
        try {
            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private String getExt(String fileName){
        String ext=null;
        if(fileName!=null && fileName.isEmpty() ){
            int dot=fileName.lastIndexOf('.');
            if(dot>0 && dot<=fileName.length()-2){
                ext=fileName.substring(dot+1);
            }
        }
        return ext;
    }
}
