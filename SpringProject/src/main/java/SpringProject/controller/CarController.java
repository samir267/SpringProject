package SpringProject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.nio.file.Path;


import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import SpringProject.models.Car;
import SpringProject.service.CarService;


@Controller
@RequestMapping("/cars")
public class CarController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

     @Autowired
    private CarService carService;
    @GetMapping("/allCars")
    public String getAllCarsPage(Model model) {
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
        return "dashboard";
    }
    

    @GetMapping("/byId/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id).orElse(null);
    }

    @GetMapping("/newCarForm")
public String showNewCarForm(Model model) {
    Car car = new Car();
    car.setEtat(1);
    model.addAttribute("car", car);
    return "newCarForm";
}



@PostMapping("/newCar")
public String createNewCar(@ModelAttribute("car") Car car, @RequestParam("image") MultipartFile file) throws IOException {
    if (!file.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);
        Files.write(path, file.getBytes());
    
        car.setImage(fileName);
    }
    
     
    carService.saveCar(car);
    return "redirect:/cars/allCars";
}




    @GetMapping("/editCarForm/{id}")
    public String showEditCarForm(@PathVariable Long id, Model model) throws Exception {
        try {
            Optional<Car> existingCar = carService.getCarById(id);
    
            model.addAttribute("car", existingCar);
    
            return "editCarForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Car not found");
            return "errorView"; 
        }
    }
    
    @PostMapping("/updateCar")
    public String updateCar(@ModelAttribute("car") Car updatedCar, Model model) throws Exception {
        try {
            carService.updateCar(updatedCar.getId(), updatedCar);
            return "redirect:/cars/allCars";
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", "Car not found");
            return "errorView"; 
        }
    }
    
    


    @GetMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable("id")Long id){
            if (carService.getCarById(id).isPresent()) {
            carService.deleteCar(id);
                }
        return "redirect:/cars/allCars";
    }

    

}

