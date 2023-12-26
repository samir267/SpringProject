package SpringProject.models;


import java.sql.Blob;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Car")
public class Car {

     @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String model;
    @Nullable
    private String name;
     @Nullable
    private String category;
    @Nullable
    private String price;
    @Nullable
    private String nb_places;
    @Nullable
    private int etat;
    @Nullable
    private String image;
   

    



    
}