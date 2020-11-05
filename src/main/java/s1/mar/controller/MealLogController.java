package s1.mar.controller;

import s1.mar.entities.Meal;
import s1.mar.services.MealLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
public class MealLogController {

    @Autowired
    private MealLogService mealLogService;

    @PostMapping("/")
    public void add(@RequestBody Meal meal) {
        mealLogService.saveMeal(meal);
    }

    @GetMapping("/")
    public List<Meal> findAll() {
        return mealLogService.findMeals();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        mealLogService.deleteMeals(id);
    }
}
